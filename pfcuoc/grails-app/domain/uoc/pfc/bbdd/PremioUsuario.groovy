package uoc.pfc.bbdd

class PremioUsuario {

    static belongsTo = [usuario: Usuario, partida: Partida]
    Date conseguido
    TipoPremio tipo

    static constraints = {
    }

    enum TipoPremio {

        recluta("premio-mochila.png", "Has terminado tu primer juego"),
        veterano("premio-barco.png", "Has terminado diez juegos"),
        excelente("premio-rubik.png", "Has terminado un juego con la máxima puntuación"),
        cumlaude("premio-orla.png", "Has terminado cinco juegos con la máxima puntuación"),
        top("premio-escalera.png", "Has sido una vez el primero del ranking")

        String imagen
        String descripcion

        TipoPremio(String imagen, String descripcion) {
            this.imagen = imagen
            this.descripcion = descripcion
        }
    }
}

