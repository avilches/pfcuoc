package uoc.pfc.mvc

import grails.converters.JSON
import uoc.pfc.bbdd.Juego
import uoc.pfc.bbdd.Partida
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
            partidas = Partida.findAllByJugadaPorAndFinalizada(usuarioActual, true)
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

    def acabar() {
        if (!hayUsuario()) {
            flash.message = "Debes estar autenticado para jugar"
            return redirect(action: "index")
        } else if (!hayPartidaActual()) {
            return redirect(action: "index")
        }

        Partida partida = partidaActual
        partidaService.revisaEstado(partida)
        if (!partida.finalizada) {
            partida.finalizada = true
            partida.save(flush: true)
        }
        session.idPartida = null
        return redirect(action: "index")
    }

    def jugando() {
        if (!hayUsuario()) {
            flash.message = "Debes estar autenticado para jugar"
            return redirect(action: "index")
        } else if (!hayPartidaActual()) {
            return redirect(action: "index")
        }

        Partida partida = partidaActual
        partidaService.revisaEstado(partida)
        def status = creaJsonStatus(partida)
        String jsonString = (status as JSON) as String
        [juego: partida.juego, status: jsonString]
    }

    def responde(Long id) {
        def json = [:]
        if (!hayUsuario()) {
            json.fatal = "Debes estar autenticado para responder"

        } else if (!hayPartidaActual()) {
            json.fatal = "Debes haber empezado una partida"

        } else if (!id) {
            json.fatal = "Falta id de la respuesta"

        } else {

            Partida partida = partidaActual

            PreguntaRespondidaUsuario preguntaRespondidaUsuario = partidaService.responde(partida, id)

            if (preguntaRespondidaUsuario == null) {
                // Respuesta invalida para el sistema
                json.fatal = "Respuesta $id no pertenece a la pregunta actual"
            } else {
                json.acertada = preguntaRespondidaUsuario.acertada
                json.respuestaCorrectaId = preguntaRespondidaUsuario.pregunta.respuestaCorrecta.id
            }
        }
        render json as JSON
    }


    private Map creaJsonStatus(Partida partida) {
        List respuestas = partidaService.cargaRespuestas(partida)

        return [partida: [fin: partida.finalizada,
                          aciertos: partida.aciertos,
                          preguntas: partida.juego.preguntas,
                          preguntaActual: partida.preguntaActual],
                pregunta: [texto:partida.preguntaRespondidaActual.pregunta.texto],
                respuestas: respuestas.collect { RespuestaPosible r -> [texto:r.texto, id:r.id]}]
    }

    def status() {
        Partida partida = partidaActual
        partidaService.revisaEstado(partida)
        render creaJsonStatus(partida) as JSON
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
