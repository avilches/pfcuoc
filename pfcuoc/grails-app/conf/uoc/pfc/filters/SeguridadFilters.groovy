package uoc.pfc.filters

import uoc.pfc.bbdd.Usuario

class SeguridadFilters {

    def filters = {
        all(controller: '*', action: '*') {
            before = {

            }
            after = { Map model ->
                if (model != null && session.idUsuario) {
                    model.usuarioActual = Usuario.get(session.idUsuario as Long)
                }

            }
            afterView = { Exception e ->

            }
        }
    }
}
