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

    def doRegistro(UsuarioCommand cmd) {
        if (!cmd.hasErrors()) {
            Usuario nuevoUsuario = usuarioService.creaJugador(cmd.login, cmd.password, cmd.nombre).save(flush: true)
            flash.message = "Usuario creado con exito"
            registraUsuarioEnSesion(nuevoUsuario)
            return redirect(controller: "partida", action: "index")
        }
        render view:"registro", model: [cmd: cmd]

    }

    def registro() {

    }
}

class UsuarioCommand {
    String login
    String nombre
    String password
    String password2

    def usuarioService

    static constraints = {
        login(blank: false, validator: { val, obj -> !obj.usuarioService.existeLogin(val)})
        nombre(blank: false)
        password(blank: false)
        password2(blank: false, validator: { val, obj -> val == obj.password })

    }

}
