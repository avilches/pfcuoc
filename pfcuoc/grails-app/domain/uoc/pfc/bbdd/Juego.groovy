package uoc.pfc.bbdd

class Juego {

    String nombre
    String descripcion
    Estado estado

    int preguntas
    int respuestasPorPregunta

    Tipo tipo

    static constraints = {
    }

    enum Tipo {
        homogeneo, heterogeneo
    }

    enum Estado {
        activo, desactivo
    }
}
