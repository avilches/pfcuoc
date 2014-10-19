package uoc.pfc.bbdd

class Pregunta {

    Juego juego

    String texto
    String imagen
    String urlVideo

    static constraints = {
        imagen(nullable: true)
        urlVideo(nullable: true)
    }
}
