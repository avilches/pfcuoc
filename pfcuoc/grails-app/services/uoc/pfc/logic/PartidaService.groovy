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

    void revisaEstado(Partida partida) {
        if (!partida.preguntaRespondidaActual) {
            // Partida recien creada, no tiene ninguna pregunta, creamos una
            creaNuevaPregunta(partida)
            return
        }

        if (partida.preguntaRespondidaActual.respuesta == null) {
            // La partida tiene todavia una pregunta pendiente de responder
            return
        }

        // La partida tiene una pregunta ya respondida, por lo que creamos una nueva solo si quedan preguntas por hacer
        if (partida.preguntaActual < (partida.juego.preguntas*2)) {
            creaNuevaPregunta(partida)
        } else {
            partida.finalizada = true
            partida.save(flush: true)
        }
    }

    PreguntaRespondidaUsuario responde(Partida partida, Long idRespuesta) {
        if (!partida.preguntaRespondidaActual || partida.preguntaRespondidaActual.respuesta) {
            // La partida actual no tiene una respuesta pendiente de responder
            // O la tiene todavía respondida
            return null
        }

        RespuestaPosible respuestaUsuario = RespuestaPosible.get(idRespuesta)

        if (!respuestaUsuario) {
            // La respuesta no existe en bbdd, no hacemos nada
            return null
        } else if (respuestaUsuario.pregunta != partida.preguntaRespondidaActual.pregunta) {
            // La respuesta enviada no se corresponde con una respuesta valida a la pregunta actual, no hacemos nada
            return null
        }

        partida.preguntaRespondidaActual.respuesta = respuestaUsuario
        partida.preguntaRespondidaActual.fechaRespuesta = new Date()
        partida.preguntaRespondidaActual.acertada = (partida.preguntaRespondidaActual.pregunta.respuestaCorrecta.id == respuestaUsuario.id)
        if (partida.preguntaRespondidaActual.acertada) {
            partida.aciertos = partida.aciertos + 1
            partida.save(flush: true)
        }
        partida.preguntaRespondidaActual.save(flush: true)

        return partida.preguntaRespondidaActual

    }

    private Pregunta creaNuevaPregunta(Partida partida) {
        List<Long> preguntasTotales = Pregunta.createCriteria().list {
            eq("juego", partida.juego)
            projections {
                property("id")
            }
        }
        List<Long> preguntasYaHechas = PreguntaRespondidaUsuario.createCriteria().listDistinct {
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

    List cargaRespuestas(Partida partida) {
        def respuestas = RespuestaPosible.findAllByPregunta(partida.preguntaRespondidaActual.pregunta)
        Collections.shuffle(respuestas)
        respuestas
    }
}
