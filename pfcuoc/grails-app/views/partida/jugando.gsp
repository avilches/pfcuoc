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

<h1>${partida.juego.nombre}</h1>

<h2>Pregunta: ${partida.preguntaRespondidaActual.pregunta.texto}</h2>

<ul>
<g:each in="${respuestas}" var="respuesta">
    <li id="respuesta_${respuesta.id}"><a href="javascript:void(responde('${respuesta.id}'));">${respuesta.texto}</a></li>
</g:each>
</ul>

<div id="msg"></div>

<r:script>
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
                    location.reload()
                }, 2000)

            } else {
                $("#msg").html("Error")
                $("#respuesta_"+id+" A").css("background-color", "#F00").css("color", "#FFF")
                $("#respuesta_"+json.respuestaCorrectaId+" A").css("background-color", "#060").css("color", "#FFF").effect("pulsate", { times:2 }, 1500);
                setTimeout(function() {
                    location.reload()
                }, 3000)
            }
        }

    })
}

</r:script>



</body>
</html>