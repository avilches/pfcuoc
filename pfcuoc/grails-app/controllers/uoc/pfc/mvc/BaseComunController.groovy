package uoc.pfc.mvc

import uoc.pfc.bbdd.Usuario

/**
 * Clase común que comparten todos los controladores con operaciones compartidas.
 */
abstract class BaseComunController {

    /**
     * Para entrar con un usuario en la web. Tras autenticar correctamente el usuario, se introduce el usuario en la
     * sesión para que permanezca identificado
     * @param usuario Usuario a logarse
     * @return void
     */
    protected void registraUsuarioEnSesion(Usuario usuario) {
        session.idUsuario = usuario.id
    }

    /**
     * Para finalizar sesión en la web. Elimina el usuario actual de la sesión.
     * @return void
     */
    protected void eliminarUsuarioDeSesion() {
        session.idUsuario = null
        session.invalidate()
    }

    /**
     * Devuelve si hay un usuario identificado en la sesión.
     * @return verdadero o falso si hay usuario
     */
    protected boolean hayUsuario() {
        session.idUsuario != null
    }

    /**
     * Devuelve el objeto Usuario de la base de datos que se corresponde con el usuario identificado
     * @return objeto usuario (si está identificado) o null (si no lo está)
     */
    protected Usuario getUsuarioActual() {
        if (hayUsuario()) {
            return Usuario.get(session.idUsuario as Long)
        }
        return null
    }
}
