package uoc.pfc.mvc

import com.gargoylesoftware.htmlunit.*
import com.gargoylesoftware.htmlunit.html.*
import uoc.pfc.bbdd.Juego
import uoc.pfc.bbdd.Pregunta
import uoc.pfc.bbdd.RespuestaPosible

class BackofficeController {

    def partidaService
    def demo() {
        Juego juego = new Juego(respuestasPorPregunta: 3, preguntas: 5, tipo:Juego.Tipo.heterogeneo, nombre: "Programación quiz",
                descripcion: "Este es un juego heterogeneo de demo, de solo 5 preguntas (aunque hay algunas más). Cada pregunta tiene sus propias respuestas, de las cuales se muestran cuatro como máximo y sólo una de ellas es la correcta. ¿Te atreves?", estado: Juego.Estado.activo).save(flush: true, failOnError: true)

        partidaService.añadePregunta(juego, "¿Quién inventó el lenguaje de programación Java?", "James Gosling", ["Richard Stallman", "Steve Wozniak", "Dennis Ritchie", "Linus Torvalds", "Brendan Eich", "Larry Page", "Steve Jobs", "Christopher Blizzard"])
        partidaService.añadePregunta(juego, "¿Qué sentencia NO sirve para iterar una colección en Groovy?", "list.findAll { }", ["for (a in list) { }", "list.each { }", "list.eachWithIndex { }"])
        partidaService.añadePregunta(juego, "¿Qué diferencia hay entre una constante y una variable", "Las variables pueden cambiar su valor, las constantes no", ["Las constantes son siempre numéricas pero las variables pueden tener cualquier tipo de valor", "No hay diferencia", "Las constantes son un tipo de variable que permite la introspección"])
        partidaService.añadePregunta(juego, "Pedir un número, pedir otro número, multiplicar los dos valores y mostrar el resultado. ¿Cómo llamarías a este conjunto de instrucciones?", "Un algoritmo", ["Una clase", "Un objeto", "Php", "Un compilador", "Ensamblador", "Función recursiva"])
        partidaService.añadePregunta(juego, "¿Cuál de los siguientes terminos no es un lenguaje de programación", "HTML", ["C", "C++", "Visual Basic", "Javascript", "Java", "Objective C", "OCalm"])
        partidaService.añadePregunta(juego, "¿A partir de que generación empezaron los lenguajes a usar palabras y comandos?", "3ª generación", ["1ª generación", "2ª generación", "4ª generación", "5ª generación"])
        partidaService.añadePregunta(juego, "¿Cómo se llama el software que traduce el codigo fuente de un programa en algo que un ordenador puede entender?", "Compilador", ["Ensamblador", "Enlazador", "Convertidor", "Traductor", "Transportador", "Conmutador"])
        partidaService.añadePregunta(juego, "Antes de que el código fuente sea compilado, debe ser:", "Parseado", ["Salvado en un fichero aparte", "Capitalizado", "Conmutado", "Ejecutado", "Verificado si tiene errores"])

        flash.message = "Juego demo creado correctamente"
        redirect action: "index"
    }

    def wikipedia() {

        // @TOdo: limite de tiempo por pregunta
        // @TOdo: rankings/estadisticas
        // @TOdo: registro de usuarios nuevos
        // @TOdo: jugar sin login, registro al final
        // @TOdo: premios
        // @TOdo: backoffice

        def continentes = ["Europa", "América", "Asia", "África", "Oceanía"]


        def client = new WebClient()

        HtmlPage html = client.getPage('http://es.wikipedia.org/wiki/Anexo:Pa%C3%ADses')

        def juegoCapitalesPorContinente = [:]
        continentes.each {
            juegoCapitalesPorContinente[it] = new Juego(respuestasPorPregunta: 4, preguntas: 20, tipo:Juego.Tipo.homogeneo, nombre: "Capitales de ${it}", descripcion: "Adivina cuales son las capitales de los países de ${it}", estado: Juego.Estado.activo).save(flush: true, failOnError: true)
        }

        def juegoBanderasPorContinente = [:]
        continentes.each {
            juegoBanderasPorContinente[it] = new Juego(respuestasPorPregunta: 4, preguntas: 20, tipo:Juego.Tipo.homogeneo, nombre: "Banderas de ${it}", descripcion: "Adivina cuales son las banderas de los países de ${it}", estado: Juego.Estado.activo).save(flush: true, failOnError: true)
        }

        Juego juegoCapitalesMundo   = new Juego(respuestasPorPregunta: 4, preguntas: 20, tipo:Juego.Tipo.homogeneo, nombre: "Capitales del mundo", descripcion: "Adivina cuales son las capitales de todos los países del mundo entro, más difícil!", estado: Juego.Estado.activo).save(flush: true, failOnError: true)
        Juego juegoBanderasMundo    = new Juego(respuestasPorPregunta: 4, preguntas: 20, tipo:Juego.Tipo.homogeneo, nombre: "Banderas del mundo", descripcion: "Adivina cuales son las banderas de todos los países del mundo entero, más difícil!", estado: Juego.Estado.activo).save(flush: true, failOnError: true)

        html.getByXPath("//table[contains(@class,'wikitable')]/tbody/tr").eachWithIndex { r, rowCount ->
            if (rowCount >0) { // Ignoramos la primera fila
                HtmlTableRow row = (HtmlTableRow)r
                Pais p = new Pais()
                row.cells.eachWithIndex { c, cellCount ->
                    HtmlTableCell cell = (HtmlTableCell)c
                    if (cellCount == 0) {
                        p.bandera = ((cell.getElementsByTagName("img")[0].attributes.srcset.value.split(",").last().trim())-" 2x").replaceAll("40px", "100px")
                    } else if (cellCount == 1) {
                        String pais = cell.asText()
                        if (pais.contains(",")) {
                            pais = pais.split(",").collect { it.trim() }.reverse().join(" ")
                        }
                        p.nombre = pais
                    } else if (cellCount == 3) {
                        p.continente = cell.asText()
                    } else if (cellCount == 4) {
                          if (cell.asText().count("(") > 1) {
//                              println "Rechazado: "+cell.asText()
                              p.rechazado = true
                          } else {
                              String original = cell.asText()
                              String nuevo = original.replaceAll("\\(.*\\)","").replaceAll("\\d","")
                              if (original != nuevo) {
//                                  println "Cambiado: $nuevo"
                              }
                              p.capital = nuevo
                          }
                    }
                }
                if (!p.rechazado) {
                    println p

                    [juegoCapitalesMundo, juegoCapitalesPorContinente[p.continente]].each {
                        if (it) {
                            Pregunta capital = new Pregunta(juego: it, texto: "¿Cuál es la capital de <b>${p.nombre}</b>?", imagen: p.bandera).save(flush: true)
                            capital.respuestaCorrecta = new RespuestaPosible(juego: it, pregunta: capital, texto: p.capital).save(flush: true)
                            capital.save(flush: true)
                        }
                    }

                    [juegoBanderasMundo, juegoBanderasPorContinente[p.continente]].each {
                        if (it) {
                            Pregunta pband = new Pregunta(juego: it, texto: "¿A qué país pertenece esta bandera?", imagen: p.bandera).save(flush: true)
                            pband.respuestaCorrecta = new RespuestaPosible(juego: it, pregunta: pband, texto: p.nombre).save(flush: true)
                            pband.save(flush: true)
                        }
                    }
                }
            }
        }
        flash.message = "Carga realizada correctamente. Se han creado juegos dos juegos (banderas y capitales) para ${continentes.join(", ")} y el mundo."
        redirect action: "index"
    }

    static class Pais {
        String continente
        String nombre
        String capital
        String bandera
        boolean rechazado = false

        @Override
        public String toString() {
            return "Pais{" +
                    "continente='" + continente + '\'' +
                    ", nombre='" + nombre + '\'' +
                    ", capital='" + capital + '\'' +
                    ", bandera='" + bandera + '\'' +
                    '}';
        }
    }
}
