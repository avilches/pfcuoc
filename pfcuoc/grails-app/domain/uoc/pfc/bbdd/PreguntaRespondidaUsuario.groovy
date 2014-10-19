package uoc.pfc.bbdd

class PreguntaRespondidaUsuario {

    Date fechaPregunta
    Date fechaRespuesta

    Partida partida
    Pregunta pregunta
    RespuestaPosible respuesta
    Boolean acertada = false  //

    static constraints = {
        respuesta(nullable: true)
        fechaRespuesta(nullable: true)
    }
}
