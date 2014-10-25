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


<div id="juegoWrapper">
    <div>
        <h1>${juego.nombre}</h1>
        <p>Pregunta <span class="preguntaActual"></span>/<span class="totalPreguntas"></span>. Aciertos: <span class="totalAciertos"></span></p>
    </div>
    <div class="pull-right">
        <g:link action="acabar" class="btn btn-danger btn-sm">Abandonar</g:link>
    </div>

    <div class="panel panel-default">
        <div class="panel-body">
            <div>
                <img src="" id="imagen" style="padding: 4px; border: 1px solid #AAA"/>
                <h4 id="pregunta"></h4>

            </div>

            <div id="respuestas" style="margin-left:30px;margin-top:5px">
                <hr/>
            </div>



            <div id="msg"></div>
        </div>
    </div>
</div>
<div id="finWrapper" style="display: none">

    <div>
        <h1>${juego.nombre}</h1>
    </div>
    <br/>
    <h3>La partida ha finalizado!</h3>
    <p>Has acertado <b><span class="totalAciertos"></span></b> preguntas de un total de <span class="totalPreguntas"></span>.</p>
    <p>Tu puntuación es: 78%</p>

    <br/>
    <g:link action="acabar" class="btn btn-info btn-sm">Continuar</g:link>

</div>

<r:script>
$(document).ready(function() {
    var status = ${raw(status)}
    loadStatus(status)

});
var respondida = false
function loadStatus(status) {
    respondida = false
    $("#msg").empty()
    $(".totalAciertos").html(status.partida.aciertos)
    $(".preguntaActual").html(status.partida.preguntaActual)
    $(".totalPreguntas").html(status.partida.preguntas)
    if (status.partida.fin == true) {
        $("#pregunta").empty()
        $("#respuestas").empty()

        $("#finWrapper").show()
        $("#juegoWrapper").hide()

    } else {
        $("#pregunta").html(status.pregunta.texto)
        $("#imagen").attr("src", status.pregunta.imagen)
        $("#respuestas").empty()
        $.each(status.respuestas, function(idx, respuesta) {
            $("#respuestas").append('<div id="respuesta_'+respuesta.id+'" style="padding: 2px"><a class="btn btn-sm btn-outline" href="javascript:void(responde('+respuesta.id+'));">'+respuesta.texto+'</a></div>')
        })
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
        console.log(json)
        if (!json.fatal) {
            var acertada = json.acertada
            if (acertada) {
//                $("#msg").html("Correcto!")
                $("#respuesta_"+id+" A").css("background-color", "#060").css("color", "#FFF")
                setTimeout(function() {
                    cargaSiguientePregunta()
                }, 2000)

            } else {
                $("#msg").html("Error")
                $("#respuesta_"+id+" A").css("background-color", "#F00").css("color", "#FFF")
                $("#respuesta_"+json.respuestaCorrectaId+" A").css("background-color", "#060").css("color", "#FFF").effect("pulsate", { times:2 }, 1500);
                setTimeout(function() {
                    cargaSiguientePregunta()
                }, 3000)
            }
        }

    })
}

function cargaSiguientePregunta() {
    var baseUrl = '${createLink(action:"status", controller:"partida")}'
    $.ajax({
        url: baseUrl
    }).always(function() {
    }).done(function(json) {
        console.log(json)
        loadStatus(json)
    })
}

</r:script>



</body>
</html>