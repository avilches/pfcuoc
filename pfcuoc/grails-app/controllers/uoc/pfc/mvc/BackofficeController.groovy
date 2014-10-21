package uoc.pfc.mvc

import com.gargoylesoftware.htmlunit.*
import com.gargoylesoftware.htmlunit.html.*
import uoc.pfc.bbdd.Juego
import uoc.pfc.bbdd.Pregunta
import uoc.pfc.bbdd.RespuestaPosible

class BackofficeController {

    def index(String cont) {


        def client = new WebClient()

        HtmlPage html = client.getPage('http://es.wikipedia.org/wiki/Anexo:Pa%C3%ADses')

        // Europa, O. Atlántico, América, Asia, África, Oceanía

        Juego juego = new Juego(respuestasPorPregunta: 4, preguntas: 20, tipo:Juego.Tipo.homogeneo, nombre: "Paises de ${cont}", descripcion: "Juega a las capitales", estado: Juego.Estado.activo).save(flush: true, failOnError: true)

        def continentes = [] as HashSet
        html.getByXPath("//table[contains(@class,'wikitable')]/tbody/tr").eachWithIndex { r, rowCount ->
            if (rowCount >0) { // Ignoramos la primera fila
                HtmlTableRow row = (HtmlTableRow)r
                Pais p = new Pais()
                row.cells.eachWithIndex { c, cellCount ->
                    HtmlTableCell cell = (HtmlTableCell)c
                    if (cellCount == 0) {
                        p.bandera = cell.getElementsByTagName("img")[0].attributes.srcset.value.split(",").last().trim()
                    } else if (cellCount == 1) {
                        p.nombre = cell.asText()
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
                if (!p.rechazado && (!cont || p.continente.contains(cont))) {
                    println p
                    Pregunta preguntaJuego = new Pregunta(juego: juego, texto: "Cual es la capital de ${p.nombre}").save(flush: true)
                    preguntaJuego.respuestaCorrecta = new RespuestaPosible(juego: juego, pregunta: preguntaJuego, texto: p.capital).save(flush: true)
                    preguntaJuego.save(flush: true)
                }
            }
        }
        println continentes
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