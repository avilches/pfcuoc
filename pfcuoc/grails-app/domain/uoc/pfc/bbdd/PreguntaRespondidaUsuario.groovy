package uoc.pfc.bbdd

class PreguntaRespondidaUsuario {

    Date fechaPregunta
    Date fechaRespuesta

    Partida partida
    Pregunta pregunta
    RespuestaPosible respuesta
    Boolean correcta = false  // Atajo para respuesta.corracta

    static constraints = {
        respuesta(nullable: true)
        fechaRespuesta(nullable: true)
    }
}
