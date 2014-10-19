package uoc.pfc.bbdd

class RespuestaPosible {

    Pregunta pregunta

    String texto
    String imagen

    static constraints = {
        imagen(nullable: true)
    }
}
