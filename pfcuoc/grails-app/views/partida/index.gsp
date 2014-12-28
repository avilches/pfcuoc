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
<div class="container">
    <div class="row">
        <div class="col-sm-8">
            <h1 style="color: #A00">Juegos</h1>
            <div class="row">
                <g:each in="${juegosActivos}" var="juego">
                    <div class="col-sm-4">
                        <h3>${juego.nombre}</h3>
                        <p>${juego.descripcion}</p>
                        <p>${juego.preguntas} preguntas. Puedes conseguir hasta ${juego.puntuacionMaximaPosible} puntos si aciertas todas.</p>
                        <g:link class="btn btn-outline btn" controller="partida" action="partidaNueva" id="${juego.id}">Jugar</g:link>
                    </div>
                </g:each>
            </div>
        </div>
        <g:if test="${usuarioActual}">
        <div class="col-sm-4">
            <h1 style="color: #A00">Premios</h1>

            <g:each in="${usuarioActual.premios.sort {it.conseguido}}" var="premio">
                <div style="padding:5px;clear: both">
                    <img style="float:left;margin-right: 5px" src="${g.resource(dir:'images', file: (premio.tipo.imagen) )}">
                    <div style="margin-top: 10px;"><b>${premio.tipo.descripcion}</b><br/>
                    Conseguido el ${premio.conseguido.format("dd/MMM/yyyy")}</div>
                </div>
            </g:each>

        </div>
            </g:if>
        <div class="col-sm-4">
            <h1 style="color: #A00">Ranking</h1>
            <table border="0" cellpadding="0">
                <tr style="border-bottom: 1px solid #777"><td><h4>Pos.</h4></td><td><h4 style="padding-left: 10px">Jugador</h4></td><td><h4 style="padding-left: 10px">Puntos</h4></td></tr>
            <g:each in="${ranking}" var="usuario" status="i">
                <tr><td><h4>#${i+1}</h4></td><td><h4 style="padding-left: 10px">${usuario.nombre}</h4></td><td><h4 style="padding-left: 10px">${usuario.total}</h4></td></tr>
            </g:each>
            </table>

        </div>
    </div>
</div>

<h1>Partidas anteriores</h1>
<ul>
    <g:each in="${partidas}" var="partida">
        <li>${partida.juego.nombre} (${partida.fechaInicio}): ${PreguntaRespondidaUsuario.findAllByPartidaAndAcertada(partida, true).size()}</li>

    </g:each>
</ul>
</body>
</html>