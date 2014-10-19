package uoc.pfc.bbdd

class RespuestaPosible {

    Pregunta pregunta

    String texto
    String imagen

    boolean correcta

    static constraints = {
        imagen(nullable: true)
    }
}
