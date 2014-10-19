package uoc.pfc.mvc

import uoc.pfc.bbdd.Juego
import uoc.pfc.bbdd.Partida
import uoc.pfc.bbdd.Pregunta
import uoc.pfc.bbdd.RespuestaPosible

class PartidaController extends BaseComunController {

    def partidaService

    def index() {
        def juegosActivos = partidaService.listaJuegosActivos()

        [juegosActivos: juegosActivos]
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
            flash.message = "Debes estar autenticado para empezar un juego"
            return redirect(action: "index")
        } else if (!hayPartidaActual()) {
            return redirect(action: "index")
        }

        Partida partida = partidaActual


        Pregunta pregunta = partidaService.cargaPregunta(partidaActual)
        if (!pregunta) {
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
        if (!hayUsuario()) {
            flash.message = "Debes estar autenticado para empezar un juego"
            return redirect(action: "index")
        } else if (!hayPartidaActual()) {
            return redirect(action: "index")
        }

        Partida partida = partidaActual

        Boolean correcta = partidaService.responde(partida, id)
        if (correcta == null) {
            // Respuesta invalida para el sistema
            return redirect(action: "jugando")
        }

        flash.correcta = correcta

        if (partidaService.cargaPregunta(partidaActual) == null) {
            return redirect(action: "fin")
        }

        return redirect(action: "jugando")
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
