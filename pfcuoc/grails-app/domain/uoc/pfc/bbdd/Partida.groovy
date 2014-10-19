package uoc.pfc.bbdd

class Partida {

    Date fechaInicio
    Usuario jugadaPor
    Juego juego

    PreguntaRespondidaUsuario preguntaRespondidaActual
    Date preguntaValidaHasta
    int preguntaActual = 0
    boolean finalizada = false
    int aciertos = 0

    static constraints = {
        preguntaRespondidaActual(nullable: true)
        preguntaValidaHasta(nullable: true)
    }
}
