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
            new RespuestaPosible(pregunta: preguntaJuego, texto: "Francia").save(flush: true)
            new RespuestaPosible(pregunta: preguntaJuego, texto: "Luxemburgo").save(flush: true)
            new RespuestaPosible(pregunta: preguntaJuego, texto: "New York").save(flush: true)

            preguntaJuego.respuestaCorrecta = new RespuestaPosible(pregunta: preguntaJuego, texto: "Madrid").save(flush: true)
            preguntaJuego.save(flush: true)

            Pregunta preguntaJuego2 = new Pregunta(juego: juego, texto: "Cual es la capital de Francia").save(flush: true)
            new RespuestaPosible(pregunta: preguntaJuego2, texto: "Venecia").save(flush: true)
            new RespuestaPosible(pregunta: preguntaJuego2, texto: "Texas").save(flush: true)
            new RespuestaPosible(pregunta: preguntaJuego2, texto: "Camerún").save(flush: true)
            preguntaJuego2.respuestaCorrecta = new RespuestaPosible(pregunta: preguntaJuego2, texto: "Paris").save(flush: true)
            preguntaJuego2.save(flush: true)

            // Crear siempre un jugador de prueba
            usuarioService.creaJugador("player", "player", "Jugador de prueba").save(flush: true)





        }

    }
    def destroy = {
    }
}
