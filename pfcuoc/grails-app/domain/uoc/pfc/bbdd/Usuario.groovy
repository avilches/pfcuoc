package uoc.pfc.bbdd

class Usuario {

    String login
    String nombre
    String passwordHash

    int partidas = 0
    int puntos = 0
    int total = 0

    static hasMany = [premios: PremioUsuario ]

    Tipo tipo

    static constraints = {
    }

    enum Tipo {
        jugador, admin
    }
}

