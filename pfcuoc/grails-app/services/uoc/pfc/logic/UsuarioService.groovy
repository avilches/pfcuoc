package uoc.pfc.logic

import grails.transaction.Transactional
import org.codehaus.groovy.grails.plugins.codecs.HexCodec
import org.codehaus.groovy.grails.plugins.codecs.SHA256BytesCodec
import uoc.pfc.bbdd.Usuario

@Transactional
class UsuarioService {

    /**
     * Crea un usuario administrador para el arranque. Solo se usa desde BootStrap
     */
    void creaUsuarioAdministradorAlArrancar() {
        Usuario admin = Usuario.findByLogin("admin")
        if (!admin) {
            Usuario u = creaJugador("admin", "uoc123", "Admin")
            u.tipo = Usuario.Tipo.admin
            u.save(flush: true)
        }
    }

    /**
     * Verifica si un login ya existe en el sistema
     * @param login
     * @return
     */
    boolean existeLogin(String login) {
        Usuario.countByLogin(login.toLowerCase())>0
    }

    /**
     * Verifica si un usuario existe en el sistema dado su login y una password en texto plano.
     * @param login
     * @param password
     * @return
     */
    Usuario login(String login, String password) {
        if (login && password) {
            Usuario usuario = Usuario.findByLogin(login.toLowerCase())
            if (passwordHash(password) == usuario?.passwordHash) {
                return usuario
            }
        }
        return null
    }

    /**
     * Crea un objeto Usuario de tipo jugador con el login y password correctamente inicializados (en concreto, el login
     * en minusculas y la password hasheada)
     * @param login
     * @param password
     * @param nombre
     * @return
     */
    Usuario creaJugador(String login, String password, String nombre) {
        new Usuario(login: login.toLowerCase(), passwordHash: passwordHash(password), nombre: nombre, tipo: Usuario.Tipo.jugador)
    }

    /**
     * Crea una hash a partir de una password. Dado que es inseguro usar únicamente SHA o MD5, se hace 10 veces introduciendo
     * una palabra cualquiera como SALT entre medias.
     * @param password clave a hashear
     * @return hash de la clave
     */
    private String passwordHash(String password) {
        String hashed = password
        10.times {
            hashed = HexCodec.encode(SHA256BytesCodec.encode(hashed+"-@-SALT-@-UOC-@-PFC-@-1234567890"))
        }
        println "Hashear ${password} -> ${hashed}"
        return hashed
    }

}
