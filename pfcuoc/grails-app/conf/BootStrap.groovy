import grails.util.Environment
import uoc.pfc.bbdd.Juego
import uoc.pfc.bbdd.Pregunta
import uoc.pfc.bbdd.RespuestaPosible

class BootStrap {

    def usuarioService

    def init = { servletContext ->

        usuarioService.creaUsuarioAdministradorAlArrancar()

        if (Environment.isDevelopmentMode()) {
            // TODO: eliminar hasta que se puedan cargar mediante scrapping
            Juego juego = new Juego(preguntas: 2, nombre: "Prueba", descripcion: "Esto es la descripcion", estado: Juego.Estado.activo).save(flush: true, failOnError: true)

            Pregunta preguntaJuego = new Pregunta(juego: juego, texto: "Cual es la capital de España").save(flush: true)
            new RespuestaPosible(pregunta: preguntaJuego, texto: "Madrid", correcta: true).save(flush: true)
            new RespuestaPosible(pregunta: preguntaJuego, texto: "Francia", correcta: false).save(flush: true)
            new RespuestaPosible(pregunta: preguntaJuego, texto: "Luxemburgo", correcta: false).save(flush: true)
            new RespuestaPosible(pregunta: preguntaJuego, texto: "New York", correcta: false).save(flush: true)

            Pregunta preguntaJuego2 = new Pregunta(juego: juego, texto: "Cual es la capital de Francia").save(flush: true)
            new RespuestaPosible(pregunta: preguntaJuego2, texto: "Paris", correcta: true).save(flush: true)
            new RespuestaPosible(pregunta: preguntaJuego2, texto: "Venecia", correcta: false).save(flush: true)
            new RespuestaPosible(pregunta: preguntaJuego2, texto: "Texas", correcta: false).save(flush: true)
            new RespuestaPosible(pregunta: preguntaJuego2, texto: "Camerún", correcta: false).save(flush: true)

            // Crear siempre un jugador de prueba
            usuarioService.creaJugador("player", "player", "Jugador de prueba").save(flush: true)





        }

    }
    def destroy = {
    }
}
