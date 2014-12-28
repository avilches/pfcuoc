package uoc.pfc.logic

import grails.transaction.*
import uoc.pfc.bbdd.*

class RankingService {

    static transactional = false

    Usuario getTop() {
        def primero = Usuario.createCriteria().list([sort:"puntos", order:"desc", max:1]) {}
        return primero?primero.first():null
    }

    List<Usuario> listTopTen() {
        Usuario.createCriteria().list([sort:"puntos", order:"desc", max:10]) {}
    }
}
