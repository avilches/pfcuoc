<%--
  Created by IntelliJ IDEA.
  User: avilches
  Date: 19/10/14
  Time: 12:36
--%>

<%@ page import="uoc.pfc.bbdd.PreguntaRespondidaUsuario" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="public"/>
    <title></title>
</head>

<body>

<h1>Juegos disponibles</h1>
<div class="container">
<g:each in="${juegosActivos}" var="juego">
    <div class="col-sm-4">
        <h3>${juego.nombre}</h3>
        <p>${juego.descripcion}</p>
        <g:link class="btn btn-outline btn" controller="partida" action="partidaNueva" id="${juego.id}">Jugar</g:link>
    </div>
</g:each>
</div>

<h1>Partidas anteriores</h1>
<ul>
    <g:each in="${partidas}" var="partida">
        <li>${partida.juego.nombre} (${partida.fechaInicio}): ${PreguntaRespondidaUsuario.findAllByPartidaAndAcertada(partida, true).size()}</li>

    </g:each>
</ul>
</body>
</html>