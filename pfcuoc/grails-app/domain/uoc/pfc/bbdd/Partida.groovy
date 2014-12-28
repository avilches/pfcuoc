package uoc.pfc.bbdd

class Partida {

    Date fechaInicio
    Usuario jugadaPor
    Juego juego

    PreguntaRespondidaUsuario preguntaRespondidaActual
    Date preguntaValidaHasta
    int preguntaActual = 0
    boolean finalizada = false
    boolean puntuacionMaximaPosibleObtenida = false
    int aciertos = 0
    int puntos = 0
    int total = 0
    int ultimaPuntuacion = 0 // Se usa para acumular puntos con respuesas consecutivas

    static hasMany = [premios: PremioUsuario]

    static constraints = {
        preguntaRespondidaActual(nullable: true)
        preguntaValidaHasta(nullable: true)
    }
}
