package uoc.pfc.bbdd

class Juego {

    String nombre
    String descripcion
    Estado estado

    int preguntas

    static constraints = {
    }

    enum Estado {
        activo, desactivo
    }
}
