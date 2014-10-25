<%--
  Created by IntelliJ IDEA.
  User: avilches
  Date: 19/10/14
  Time: 12:36
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="public"/>
    <title></title>
</head>

<body>

<div class="col-md-8 col-md-offset-2">
    <div id="juegoWrapper">
        <div>
            <div style="float:right; text-align: right">
                <span class="puntos" style="font: bold 50px Georgia, serif; color: #2aabd2"></span><br/><span class="totalAciertosPorcentaje badge">0%</span>
            </div>
            <h1>${juego.nombre}</h1>
            <p>Pregunta <span class="preguntaActual"></span> de <span class="totalPreguntas "></span> - Aciertos: <span class="totalAciertos"></span> (<span class="totalAciertosPorcentaje">0%</span>)
            </p>
        </div>
        <div class="panel panel-default" id="preguntaWrapper" style="display: none">
            <div class="panel-body">
                <div>
                    <img src="" id="imagen" style="padding: 4px; border: 1px solid #AAA; display: none"/>
                    <p style="color: #888">Por <span class="siguientePuntuacion"></span> puntos</p>
                    <h4 id="pregunta"></h4>


                </div>

                <div id="respuestas" style="margin-left:30px;margin-top:5px">
                    <hr/>
                </div>

                <br/>
                <div id="msg" style="color: #888;height: 20px"><span id="msgContent">&nbsp;</span></div>

                <div class="pull-right">
                    <a id="continuar" style="display:none" class="btn btn-primary btn-sm" href="javascript:void(cargaSiguientePreguntaYa())">Continuar <span id="counter">0</span></a>
                    <g:link action="acabar" class="btn btn-danger btn-sm">Abandonar</g:link>
                </div>

            </div>
        </div>
    </div>
    <div id="finWrapper" style="display: none">

        <div>
            <h1>${juego.nombre}</h1>
        </div>
        <br/>
        <h3>La partida ha finalizado!</h3>
        <p>Has acertado <b><span class="totalAciertos"></span></b> preguntas de un total de <b><span class="totalPreguntas"></span></b> lo que supone el <b><span class="totalAciertosPorcentaje">0%</span></b></p>
        <p>Tu puntuación es <span><span class="puntos" style="font: bold 41px Georgia, serif; color: #2aabd2"></span> puntos</span></p>

        <br/>
        <g:link action="acabar" class="btn btn-info btn-sm">Continuar</g:link>

    </div>
</div>
<r:script>
$(document).ready(function() {
    var status = ${raw(status)}
    loadStatus(status)

});
var respondida = false
var siguientePuntuacion
function loadStatus(status) {
    respondida = false
    $("#msgContent").empty();

    $(".totalAciertos").html(status.partida.aciertos)
    $(".preguntaActual").html(status.partida.preguntaActual)
    $(".totalPreguntas").html(status.partida.preguntas)
    $(".siguientePuntuacion").html(status.partida.siguientePuntuacion)
    $(".ultimaPuntuacion").html(status.partida.ultimaPuntuacion)
    $(".puntos").html(status.partida.puntos)
    siguientePuntuacion = status.partida.siguientePuntuacion

    if (status.partida.preguntaActual > 1) {
        var preguntas = status.partida.fin ? status.partida.preguntaActual : status.partida.preguntaActual-1
        var totalAciertosPorcentaje = parseInt((status.partida.aciertos / preguntas) * 100) + "%"
        $(".totalAciertosPorcentaje").html(totalAciertosPorcentaje)
    }

    if (status.partida.fin == true) {
        $("#pregunta").empty()
        $("#respuestas").empty()

        $("#finWrapper").show()
        $("#juegoWrapper").hide()

    } else {

        $("#pregunta").html(status.pregunta.texto)
        if (status.pregunta.imagen) {
            $("#imagen").show()
            $("#imagen").attr("src", status.pregunta.imagen)
        } else {
            $("#imagen").hide()
        }
        $("#respuestas").empty()
        $.each(status.respuestas, function(idx, respuesta) {
            $("#respuestas").append('<div style="padding: 3px"><span id="respuesta_'+respuesta.id+'"><a class="btn btn-sm btn-outline enlaceRespuesta" href="javascript:void(responde('+respuesta.id+'));">'+respuesta.texto+'</a></span><span style="padding-left:10px" id="respuesta_'+respuesta.id+'_extra"></span></div>')
        })
        $("#preguntaWrapper").show("slide", { direction: "left" }, 150)
    }
}

function responde(id) {
    if (respondida) return
    respondida = true
    var baseUrl = '${createLink(action:"responde", controller:"partida")}'
    $.ajax({
        url: baseUrl,
        data: {id: id}
    }).always(function() {
    }).done(function(json) {
        if (!json.fatal) {
            var acertada = json.acertada
            $(".enlaceRespuesta").css("color","#AAA").css("border-color","#AAA")

            if (acertada) {
                var status = json.status
                $(".puntos").html(status.partida.puntos).effect("pulsate", { times:2 }, 1500);
                $(".totalAciertos").html(status.partida.aciertos)


                $("#msgContent").html("Correcto! Has ganado "+siguientePuntuacion+" puntos y los acumulas para la próxima!")
                $("#msg").show("highlight")

                $("#respuesta_"+id+" A").css("background-color", "#449d44").css("color", "#FFF").css("border-color", "#000")
                $("#respuesta_"+id+"_extra").html("<b>+"+siguientePuntuacion+" puntos!</b>")
                $("#respuesta_"+id+"_extra").css("color", "#000").effect("pulsate", { times:2 }, 1500);
                cargaSiguientePreguntaEn(3)

            } else {
                $("#msgContent").html("Error! Has perdido la racha que llevabas, la próxima solo ganarás 1 punto.")
                $("#msg").show("highlight")

                $("#respuesta_"+id+" A").css("background-color", "#F00").css("color", "#FFF").css("border-color", "#000")
                $("#respuesta_"+json.respuestaCorrectaId+" A").css("background-color", "#449d44").css("color", "#FFF").effect("pulsate", { times:2 }, 1500);
                cargaSiguientePreguntaEn(4)
            }
        }

    })
}

function now() {
  return ((new Date()).getTime());
}

var timerContinuar

function cargaSiguientePreguntaEn(secs) {
    clearInterval(timerContinuar)
    $("#continuar").show();

    timerContinuar = iniciaCuentaAtras("counter", secs, function() {
        cargaSiguientePreguntaYa()
    })
}

function iniciaCuentaAtras(id, secs, fin) {
    $("#"+id).html(secs);
    var countAmt = secs * 1000;
    var startTime = now();
    var interval = setInterval(function() {
    tick(id, startTime, countAmt, interval, function() {
        clearInterval(interval);
        fin()
      })
    }, 1000);
    return interval
}

function tick(id, startTime, countAmt, interval, fin) {
    var elapsed = now() - startTime;
    var cnt = countAmt - elapsed;
    if (cnt > 0) {
        $("#"+id).html(Math.round(cnt / 1000))
    } else {
        clearInterval(interval)
        $("#"+id).html("0")
        fin()
    }
}

function cargaSiguientePreguntaYa() {
    clearInterval(timerContinuar)
    $("#counter").html("0");
    $("#continuar").hide();
    cargaSiguientePregunta();
}

function cargaSiguientePregunta() {
    var baseUrl = '${createLink(action:"status", controller:"partida")}'
    $("#preguntaWrapper").hide("slide", { direction: "right" }, 150)
    $.ajax({
        url: baseUrl
    }).always(function() {
    }).done(function(json) {
        loadStatus(json)
    })
}

</r:script>



</body>
</html>