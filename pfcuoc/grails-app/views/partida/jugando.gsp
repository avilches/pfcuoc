<%--
  Created by IntelliJ IDEA.
  User: avilches
  Date: 19/10/14
  Time: 12:36
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title></title>
</head>

<body>


<h1>${juego.nombre}</h1>

<div id="juegoWrapper">
    <h2 id="pregunta"></h2>
    <p>Pregunta <span class="preguntaActual"></span>/<span class="totalPreguntas"></span></p>

    <ul id="respuestas">
    </ul>

    <g:link action="acabar">Abandonar</g:link>


    <div id="msg"></div>
</div>
<div id="finWrapper" style="display: none">
    <h1>La partida ha finalizado</h1>
    Aciertos: <span class="totalAciertos"></span> de <span class="totalPreguntas"></span>

    <g:link action="acabar">Finalizar</g:link>

</div>

<r:script>
$(document).ready(function() {
    var status = ${raw(status)}
    loadStatus(status)

});
function loadStatus(status) {
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
        $("#respuestas").empty()
        $.each(status.respuestas, function(idx, respuesta) {
            $("#respuestas").append('<li id="respuesta_'+respuesta.id+'"><a href="javascript:void(responde('+respuesta.id+'));">'+respuesta.texto+'</a></li>')
        })
    }

}
function responde(id) {
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
                $("#msg").html("Correcto!")
                $("#respuesta_"+id+" A").css("background-color", "#060").css("color", "#FFF")
                setTimeout(function() {
                    cargaPregunta()
                }, 2000)

            } else {
                $("#msg").html("Error")
                $("#respuesta_"+id+" A").css("background-color", "#F00").css("color", "#FFF")
                $("#respuesta_"+json.respuestaCorrectaId+" A").css("background-color", "#060").css("color", "#FFF").effect("pulsate", { times:2 }, 1500);
                setTimeout(function() {
                    cargaPregunta()
                }, 3000)
            }
        }

    })
}

function cargaPregunta() {
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