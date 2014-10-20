package uoc.pfc.bbdd

class RespuestaPosible {


    Juego juego
    Pregunta pregunta

    String texto
    String imagen

    static constraints = {
        imagen(nullable: true)
        pregunta(nullable: true)
    }
}
