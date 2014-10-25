package uoc.pfc.bbdd



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class JuegoController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Juego.list(params), model:[juegoInstanceCount: Juego.count()]
    }

    def show(Juego juegoInstance) {
        respond juegoInstance
    }

    def create() {
        respond new Juego(params)
    }

    @Transactional
    def save(Juego juegoInstance) {
        if (juegoInstance == null) {
            notFound()
            return
        }

        if (juegoInstance.hasErrors()) {
            respond juegoInstance.errors, view:'create'
            return
        }

        juegoInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'juego.label', default: 'Juego'), juegoInstance.id])
                redirect juegoInstance
            }
            '*' { respond juegoInstance, [status: CREATED] }
        }
    }

    def edit(Juego juegoInstance) {
        respond juegoInstance
    }

    @Transactional
    def update(Juego juegoInstance) {
        if (juegoInstance == null) {
            notFound()
            return
        }

        if (juegoInstance.hasErrors()) {
            respond juegoInstance.errors, view:'edit'
            return
        }

        juegoInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Juego.label', default: 'Juego'), juegoInstance.id])
                redirect juegoInstance
            }
            '*'{ respond juegoInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Juego juegoInstance) {

        if (juegoInstance == null) {
            notFound()
            return
        }

        juegoInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Juego.label', default: 'Juego'), juegoInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'juego.label', default: 'Juego'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
