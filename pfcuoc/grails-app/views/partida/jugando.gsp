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

<g:if test="${flash.correcta != null}">
    <g:if test="${flash.correcta == true}">
        BRAVO!
    </g:if>
    <g:else>
        ERROR!
    </g:else>
</g:if>

<h1>${partida.juego.nombre}</h1>

<h2>Pregunta: ${partida.preguntaRespondidaActual.pregunta.texto}</h2>

<ul>
<g:each in="${respuestas}" var="respuesta">
    <li><g:link action="responde" id="${respuesta.id}">${respuesta.texto}</g:link></li>
</g:each>
</ul>



</body>
</html>