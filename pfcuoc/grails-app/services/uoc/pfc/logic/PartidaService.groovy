package uoc.pfc.logic

import grails.transaction.Transactional
import uoc.pfc.bbdd.Juego
import uoc.pfc.bbdd.Partida
import uoc.pfc.bbdd.Pregunta
import uoc.pfc.bbdd.PreguntaRespondidaUsuario
import uoc.pfc.bbdd.RespuestaPosible
import uoc.pfc.bbdd.Usuario

@Transactional
class PartidaService {

    def random = new Random()

    List listaJuegosActivos() {
        return Juego.findAllByEstado(Juego.Estado.activo)
    }

    Juego cargaJuegoValido(Long id) {
        return Juego.findByIdAndEstado(id, Juego.Estado.activo)
    }

    Partida crearNuevaPartida(Juego juego, Usuario usuario) {
        Partida partida = new Partida(juego: juego, jugadaPor: usuario, fechaInicio: new Date())
        partida.save(flush: true)
        partida
    }

    PreguntaRespondidaUsuario responde(Partida partida, Long idRespuesta) {
        if (!partida.preguntaRespondidaActual || partida.preguntaRespondidaActual.respuesta) {
            // La partida actual no tiene una respuesta pendiente de responder
            // O la tiene todavía respondida
            return null
        }

        RespuestaPosible respuestaUsuario = RespuestaPosible.get(idRespuesta)

        if (!respuestaUsuario) {
            // Respuesta no existe, no hacemos nada
            return null
        }

        if (respuestaUsuario.pregunta != partida.preguntaRespondidaActual.pregunta) {
            // La respuesta enviada no se corresponde con una respuesta valida a la pregunta actual, no hacemos nada
            return null
        }

        partida.preguntaRespondidaActual.respuesta = respuestaUsuario
        partida.preguntaRespondidaActual.fechaRespuesta = new Date()
        partida.preguntaRespondidaActual.acertada = (partida.preguntaRespondidaActual.pregunta.respuestaCorrecta.id == respuestaUsuario.id)
        partida.preguntaRespondidaActual.save(flush: true)

        return partida.preguntaRespondidaActual

    }
    Pregunta cargaPregunta(Partida partida) {
        if (!partida.preguntaRespondidaActual) {
            // Partida recien creada, no tiene ninguna pregunta, creamos una
            return creaNuevaPregunta(partida)
        }

        if (partida.preguntaRespondidaActual.respuesta == null) {
            // La partida tiene una pregunta pendiente de responder
            return partida.preguntaRespondidaActual.pregunta
        }

        // La partida tiene una pregunta ya respondida, creamos una nueva solo si quedan preguntas por hacer


        if (partida.preguntaActual < (partida.juego.preguntas*2)) {
            return creaNuevaPregunta(partida)
        }

        return null // La partida ha acabado
    }

    private Pregunta creaNuevaPregunta(Partida partida) {
        List<Long> preguntasTotales = Pregunta.createCriteria().list {
            eq("juego", partida.juego)
            projections {
                property("id")
            }
        }
        List<Long> preguntasYaHechas = PreguntaRespondidaUsuario.createCriteria().list {
            delegate.partida {
                eq("jugadaPor", partida.jugadaPor)
                eq("juego", partida.juego)
            }
            delegate.pregunta {
                projections {
                    property("id")
                }
            }
        }
        List<Long> preguntasDisponibles = preguntasTotales - preguntasYaHechas
        Pregunta preguntaElegida
        if (preguntasDisponibles.size() > 0) {
            preguntaElegida = Pregunta.get(preguntasDisponibles[random.nextInt(preguntasDisponibles.size())])
        } else {
            preguntaElegida = Pregunta.get(preguntasTotales[random.nextInt(preguntasTotales.size())])
        }

        partida.preguntaRespondidaActual = new PreguntaRespondidaUsuario(partida: partida, fechaPregunta: new Date(), pregunta: preguntaElegida).save(flush: true)
        partida.preguntaActual++
        partida.save(flush: true)
        return partida.preguntaRespondidaActual.pregunta
    }
}
