<%--
  Created by IntelliJ IDEA.
  User: avilches
  Date: 19/10/14
  Time: 12:36
--%>

<%@ page import="uoc.pfc.bbdd.PreguntaRespondidaUsuario" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title></title>
</head>

<body>

<h1>Juegos disponibles</h1>
<g:each in="${juegosActivos}" var="juego">
    <h2>${juego.nombre}</h2> <g:link controller="partida" action="partidaNueva" id="${juego.id}">JUGAR!</g:link>
    <p>${juego.descripcion}</p>
    <br/>
</g:each>

<h1>Partidas anteriores</h1>
<ul>
<g:each in="${partidas}" var="partida">
    <li>${partida.juego.nombre} (${partida.fechaInicio}): ${PreguntaRespondidaUsuario.findAllByPartidaAndAcertada(partida, true).size()}</li>

</g:each>
</ul>
</body>
</html>