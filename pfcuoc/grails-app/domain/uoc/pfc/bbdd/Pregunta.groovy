package uoc.pfc.bbdd

class Pregunta {

    Juego juego

    String texto
    String imagen
    String urlVideo

    RespuestaPosible respuestaCorrecta

    static constraints = {
        imagen(nullable: true)
        urlVideo(nullable: true)
        respuestaCorrecta(nullable: true) // Dejamos a null para evitar la referencia ciruclar entre Pregunta y RespuestaPosible
    }
}
