package uoc.pfc.logic

import uoc.pfc.bbdd.*

class PremioService {

    static transactional = false

    def rankingService

    List<PremioUsuario> verificaPremiosAlAcabarPartida(Partida partida) {

        Usuario jugador = partida.jugadaPor
        Set<PremioUsuario.TipoPremio> premios = jugador.premios*.tipo as HashSet<PremioUsuario.TipoPremio>

        int partidasAcabadas = Partida.createCriteria().count() {
            eq("jugadaPor", jugador)
            eq("finalizada", true)
        }

        if (partidasAcabadas == 10) {
            añadePremioSiNoLoTiene(premios, partida, PremioUsuario.TipoPremio.veterano)
        } else if (partidasAcabadas == 1) {
            añadePremioSiNoLoTiene(premios, partida, PremioUsuario.TipoPremio.recluta)
        }

        int partidasAcabadasConLaMaximaPuntuacion = Partida.createCriteria().count() {
            eq("jugadaPor", jugador)
            eq("finalizada", true)
            eq("puntuacionMaximaPosibleObtenida", true)
        }
        if (partidasAcabadasConLaMaximaPuntuacion == 5) {
            añadePremioSiNoLoTiene(premios, partida, PremioUsuario.TipoPremio.cumlaude)
        } else if (partidasAcabadasConLaMaximaPuntuacion == 1) {
            añadePremioSiNoLoTiene(premios, partida, PremioUsuario.TipoPremio.excelente)
        }

        if (rankingService.top.id == jugador.id) {
            añadePremioSiNoLoTiene(premios, partida, PremioUsuario.TipoPremio.top)
        }

    }

    void añadePremioSiNoLoTiene(Set<PremioUsuario.TipoPremio> premios, Partida partida, PremioUsuario.TipoPremio tipoPremio) {
        if (!(tipoPremio in premios)) {
            new PremioUsuario(usuario: partida.jugadaPor, partida: partida, tipo: tipoPremio, conseguido: new Date()).save(flush: true)
        }
    }
}
