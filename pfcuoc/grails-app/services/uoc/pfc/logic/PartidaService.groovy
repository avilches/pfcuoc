package uoc.pfc.logic

import grails.transaction.Transactional
import uoc.pfc.bbdd.Juego
import uoc.pfc.bbdd.Partida
import uoc.pfc.bbdd.Pregunta
import uoc.pfc.bbdd.PreguntaRespondidaUsuario
import uoc.pfc.bbdd.RespuestaPosible
import uoc.pfc.bbdd.Usuario

class PartidaService {

    def random = new Random()

    static transactional = false

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

    @Transactional
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

    @Transactional
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
        Pregunta pregunta = partida.preguntaRespondidaActual.pregunta

        List<Long> respuestasIncorrectasIds
        if (partida.juego.tipo != Juego.Tipo.heterogeneo) {
            // Cada pregunta tiene sus propias respuestas, leemos todas las respuestas de la pregunta actual

            respuestasIncorrectasIds = RespuestaPosible.createCriteria().list {
                ne("id", pregunta.respuestaCorrecta.id)
                eq("pregunta", pregunta)
                projections {
                    property("id")
                }
            }

        } else {
            // Todas las respuestas son validas como respuesta incorrecta a cualquier pregunta, leemos todas las respuestas del juego

            respuestasIncorrectasIds = RespuestaPosible.createCriteria().list {
                ne("id", pregunta.respuestaCorrecta.id)
                eq("juego", partida.juego)
                projections {
                    property("id")
                }
            }
        }
        Collections.shuffle(respuestasIncorrectasIds)

        // Como puede haber más respuestas posibles que respuestas que se muestran al usuario, se recortan hasta
        // que tengan el tamaña deseado. El tamaño partida.juego.respuestasPorPregunta
        while (respuestasIncorrectasIds.size() > (partida.juego.respuestasPorPregunta-1)) {
            respuestasIncorrectasIds.pop()
        }

        List<Long> respuestas = respuestasIncorrectasIds + pregunta.respuestaCorrecta.id
        Collections.shuffle(respuestas)
        return respuestas.collect { RespuestaPosible.get(it) }
    }
}
