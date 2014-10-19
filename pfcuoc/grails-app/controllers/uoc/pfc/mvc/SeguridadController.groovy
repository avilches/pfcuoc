package uoc.pfc.mvc

import uoc.pfc.bbdd.Usuario

class SeguridadController extends BaseComunController {

    def usuarioService

    /**
     * Action para identificar al usuario en la aplicación
     * @param login login del usuario
     * @param password clave del usuario
     * @return no devuelve nada pues acaba con un redirect
     */
    def login(String login, String password) {
        usuarioService.creaUsuarioAdministradorAlArrancar()
        Usuario usuario = usuarioService.login(login, password)
        if (usuario) {
            registraUsuarioEnSesion(usuario)
        } else {
            flash.message = "Login incorrecto"
        }

        return redirect(controller: "partida", action: "index")
    }

    /**
     * Action para salir de la sesion
     * @return no devuelve nada pues acaba con un redirect
     */
    def logout() {
        eliminarUsuarioDeSesion()
        return redirect(controller: "partida", action: "index")
    }
}
