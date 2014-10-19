package uoc.pfc.bbdd

class Usuario {

    String login
    String nombre
    String passwordHash

    Tipo tipo

    static constraints = {
    }

    enum Tipo {
        jugador, admin
    }
}
