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

<h1>Registro</h1>
<div class="container">

    <div style="color: #ff0000">
    <g:renderErrors bean="${cmd}"/>
    </div>

    <g:form action="doRegistro">
        Nombre: <g:textField name="nombre" value="${cmd?.nombre}"/><br/>
        Login: <g:textField name="login" value="${cmd?.login}"/><br/>
        Clave: <g:passwordField name="password" value="${cmd?.password}"/><br/>
        Repite clave: <g:passwordField name="password2" value="${cmd?.password2}"/><br/><br/>
        <g:submitButton class="btn btn-primary" name="submit" value="Crear usuario"/>
    </g:form>
    </div>

</body>
</html>