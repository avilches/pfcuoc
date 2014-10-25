
<%@ page import="uoc.pfc.bbdd.Juego" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'juego.label', default: 'Juego')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-juego" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-juego" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="descripcion" title="${message(code: 'juego.descripcion.label', default: 'Descripcion')}" />
					
						<g:sortableColumn property="estado" title="${message(code: 'juego.estado.label', default: 'Estado')}" />
					
						<g:sortableColumn property="nombre" title="${message(code: 'juego.nombre.label', default: 'Nombre')}" />
					
						<g:sortableColumn property="preguntas" title="${message(code: 'juego.preguntas.label', default: 'Preguntas')}" />
					
						<g:sortableColumn property="respuestasPorPregunta" title="${message(code: 'juego.respuestasPorPregunta.label', default: 'Respuestas Por Pregunta')}" />
					
						<g:sortableColumn property="tipo" title="${message(code: 'juego.tipo.label', default: 'Tipo')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${juegoInstanceList}" status="i" var="juegoInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${juegoInstance.id}">${fieldValue(bean: juegoInstance, field: "descripcion")}</g:link></td>
					
						<td>${fieldValue(bean: juegoInstance, field: "estado")}</td>
					
						<td>${fieldValue(bean: juegoInstance, field: "nombre")}</td>
					
						<td>${fieldValue(bean: juegoInstance, field: "preguntas")}</td>
					
						<td>${fieldValue(bean: juegoInstance, field: "respuestasPorPregunta")}</td>
					
						<td>${fieldValue(bean: juegoInstance, field: "tipo")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${juegoInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
