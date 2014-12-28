package uoc.pfc.bbdd

class Juego {

    String nombre
    String descripcion
    Estado estado

    int preguntas
    int puntuacionMaximaPosible
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

    def beforeValidate() {
        // Inspirado de: http://groovy.codehaus.org/Functional+Programming+with+Groovy
        def fact = { n -> n == 0 ? 0 : n + call(n - 1) }
        puntuacionMaximaPosible = fact(preguntas)*10
    }

}
