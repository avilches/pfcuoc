package uoc.pfc.bbdd

class PreguntaRespondidaUsuario {

    Date fechaPregunta
    Date fechaRespuesta

    Partida partida
    Pregunta pregunta
    RespuestaPosible respuesta
    boolean respondida = false
    Boolean acertada = false  //
    int iacertada = 0

    static constraints = {
        respuesta(nullable: true)
        fechaRespuesta(nullable: true)
    }
}
