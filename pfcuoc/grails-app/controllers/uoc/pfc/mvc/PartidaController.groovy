package uoc.pfc.mvc

import grails.converters.JSON
import uoc.pfc.bbdd.Juego
import uoc.pfc.bbdd.Partida
import uoc.pfc.bbdd.Pregunta
import uoc.pfc.bbdd.PreguntaRespondidaUsuario
import uoc.pfc.bbdd.RespuestaPosible

class PartidaController extends BaseComunController {

    def partidaService

    def index() {
        if (hayPartidaActual()) {
            return redirect(action: "jugando")
        }
        def juegosActivos = partidaService.listaJuegosActivos()
        def partidas = []
        if (hayUsuario()) {
            partidas = Partida.findAllByJugadaPor(usuarioActual)
        }

        [juegosActivos: juegosActivos, partidas: partidas]
    }

    def partidaNueva(Long id) {
        if (!hayUsuario()) {
            flash.message = "Debes estar autenticado para empezar un juego"
            return redirect(action: "index")
        } else if (hayPartidaActual()) {
            return redirect(action: "jugando")
        }

        Juego juego = partidaService.cargaJuegoValido(id)
        if (!juego) {
            flash.message = "Juego no existe o no es valido"
            return redirect(action: "index")
        }

        Partida partida = partidaService.crearNuevaPartida(juego, usuarioActual)
        session.idPartida = partida.id
        redirect action: "jugando"

    }

    def jugando() {
        if (!hayUsuario()) {
            flash.message = "Debes estar autenticado para jugar"
            return redirect(action: "index")
        } else if (!hayPartidaActual()) {
            return redirect(action: "index")
        }

        Partida partida = partidaActual


        Pregunta pregunta = partidaService.cargaPregunta(partidaActual)
        if (!pregunta) {
            session.idPartida = null
            return redirect(action: "fin")
        }

        def respuestas = RespuestaPosible.findAllByPregunta(pregunta)
        Collections.shuffle(respuestas)

        [partida: partida, pregunta: pregunta, respuestas: respuestas]

    }

    def fin() {
        render "Su partida ha acabado"
    }

    def responde(Long id) {
        def json = [:]
        if (!hayUsuario()) {
            json.fatal = "Debes estar autenticado para responder"
        } else if (!hayPartidaActual()) {
            json.fatal = "Debes haber empezado una partida"
        } else {

            Partida partida = partidaActual

            PreguntaRespondidaUsuario preguntaRespondidaUsuario = partidaService.responde(partida, id)
            if (preguntaRespondidaUsuario == null) {
                // Respuesta invalida para el sistema
                json.fatal = "Respuesta no pertenece a la pregunta actual"
            } else {
                json.acertada = preguntaRespondidaUsuario.acertada
                json.respuestaCorrectaId = preguntaRespondidaUsuario.pregunta.respuestaCorrecta.id
            }
        }
        render json as JSON
    }

    protected Partida getPartidaActual() {
        if (hayPartidaActual()) {
            return Partida.get(session.idPartida as Long)
        }
        return null
    }

    protected boolean hayPartidaActual() {
        session.idPartida != null
    }


}
