import grails.util.Environment
import uoc.pfc.bbdd.Juego
import uoc.pfc.bbdd.Pregunta
import uoc.pfc.bbdd.RespuestaPosible

class BootStrap {

    def usuarioService
    def partidaService

    def init = { servletContext ->

        usuarioService.creaUsuarioAdministradorAlArrancar()

        if (Environment.isDevelopmentMode()) {
            usuarioService.creaJugador("player", "player", "Jugador de prueba").save(flush: true)
        }

    }
    def destroy = {
    }
}
