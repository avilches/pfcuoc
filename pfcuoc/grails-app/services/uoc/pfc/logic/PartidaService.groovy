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
        if (partida.preguntaActual < partida.juego.preguntas) {
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
        }

        // Validamos que la respuesta pertenezca a la pregunta (si es heterogeneo) o al juego (si es homogeneo)
        if (partida.juego.tipo == Juego.Tipo.homogeneo) {
            if (respuestaUsuario.juego != partida.juego) {
                // La respuesta enviada no se corresponde con una respuesta valida a la pregunta actual, no hacemos nada
                return null
            }
        } else {
            if (respuestaUsuario.pregunta != partida.preguntaRespondidaActual.pregunta) {
                // La respuesta enviada no se corresponde con una respuesta valida a la pregunta actual, no hacemos nada
                return null
            }
        }

        partida.preguntaRespondidaActual.respuesta = respuestaUsuario
        partida.preguntaRespondidaActual.fechaRespuesta = new Date()
        partida.preguntaRespondidaActual.acertada = (partida.preguntaRespondidaActual.pregunta.respuestaCorrecta.id == respuestaUsuario.id)
        partida.preguntaRespondidaActual.iacertada = partida.preguntaRespondidaActual.acertada?1:0
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
            /*
            Obtenemos un array con los ids de las preguntas. Dado que el jugador ya las ha respondido todas,
            cada una de ellas podrá estar acertada o fallada al menos una vez y, con el paso del tiempo, varias veces.

            Lo que hacemos es contar el número de aciertos de una pregunta vs. el número total de respuestas y sacamos
            fallos - aciertos para dar un peso. El peso será como mínimo de 1, de manera que si la pregunta
            tiene más aciertos que fallos, en vez de dar un peso negativa, da un peso de 1.
            Si la pregunta tiene 4 fallos y 2 aciertos, tiene un peso de 2.
            Finalmente, para el array del que se elegirá la pregunta, se crea un nuevo array en el que cada id aparecerá
            repetido tantas veces como su peso (1 si tiene más aciertos que errores y >1 si tiene más errores que aciertos).

            De esta manera será más probable que las preguntas falladas incrementan la posibilidad de salir al estar
            más veces repetidas en el array de ids candidatos.

            Lo bueno es que con el paso del tiempo, si una pregunta que tenía más fallos que aciertos pasa a tener los mismos
            o menos, dejará de salir con más probabilidad.
             */
            List<Long> preguntasTotalesPonderizadasPorFallo = PreguntaRespondidaUsuario.createCriteria().list {
                createAlias("pregunta", "preguntaAlias", org.hibernate.criterion.CriteriaSpecification.LEFT_JOIN)

                delegate.partida {
                    eq("jugadaPor", partida.jugadaPor)
                    eq("juego", partida.juego)
                }
                projections {
                    groupProperty("preguntaAlias.id")
                    sum("iacertada")
                    count("id")
                }
            }.collect {
                def fallosMenosAciertos2 = Math.max(1, it[2] - (it[1] * 2))
                [it[0]] * fallosMenosAciertos2
            }.flatten()

            preguntaElegida = Pregunta.get(preguntasTotalesPonderizadasPorFallo[random.nextInt(preguntasTotalesPonderizadasPorFallo.size())])
        }

        partida.preguntaRespondidaActual = new PreguntaRespondidaUsuario(partida: partida, fechaPregunta: new Date(), pregunta: preguntaElegida).save(flush: true)
        partida.preguntaActual++
        partida.save(flush: true)
        return partida.preguntaRespondidaActual.pregunta
    }

    List cargaRespuestas(Partida partida) {
        Pregunta pregunta = partida.preguntaRespondidaActual.pregunta

        List<Long> respuestasIncorrectasIds
        if (partida.juego.tipo == Juego.Tipo.heterogeneo) {
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

    Pregunta añadePregunta(Juego juego, String textoPregunta, String respuestaCorrecta, List textoRespuestasIncorrectas) {
        Pregunta pr = new Pregunta(juego: juego, texto: textoPregunta).save(flush: true)
        textoRespuestasIncorrectas.each { String textoRespuestasIncorrecta ->
            new RespuestaPosible(juego: juego, pregunta: pr, texto: textoRespuestasIncorrecta).save(flush: true)
        }
        pr.respuestaCorrecta = new RespuestaPosible(juego: juego, pregunta: pr, texto: respuestaCorrecta).save(flush: true)
        pr.save(flush: true)
        return pr
    }
}
