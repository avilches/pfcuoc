
<%@ page import="uoc.pfc.bbdd.Juego" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'juego.label', default: 'Juego')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-juego" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-juego" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list juego">
			
				<g:if test="${juegoInstance?.descripcion}">
				<li class="fieldcontain">
					<span id="descripcion-label" class="property-label"><g:message code="juego.descripcion.label" default="Descripcion" /></span>
					
						<span class="property-value" aria-labelledby="descripcion-label"><g:fieldValue bean="${juegoInstance}" field="descripcion"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${juegoInstance?.estado}">
				<li class="fieldcontain">
					<span id="estado-label" class="property-label"><g:message code="juego.estado.label" default="Estado" /></span>
					
						<span class="property-value" aria-labelledby="estado-label"><g:fieldValue bean="${juegoInstance}" field="estado"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${juegoInstance?.nombre}">
				<li class="fieldcontain">
					<span id="nombre-label" class="property-label"><g:message code="juego.nombre.label" default="Nombre" /></span>
					
						<span class="property-value" aria-labelledby="nombre-label"><g:fieldValue bean="${juegoInstance}" field="nombre"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${juegoInstance?.preguntas}">
				<li class="fieldcontain">
					<span id="preguntas-label" class="property-label"><g:message code="juego.preguntas.label" default="Preguntas" /></span>
					
						<span class="property-value" aria-labelledby="preguntas-label"><g:fieldValue bean="${juegoInstance}" field="preguntas"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${juegoInstance?.respuestasPorPregunta}">
				<li class="fieldcontain">
					<span id="respuestasPorPregunta-label" class="property-label"><g:message code="juego.respuestasPorPregunta.label" default="Respuestas Por Pregunta" /></span>
					
						<span class="property-value" aria-labelledby="respuestasPorPregunta-label"><g:fieldValue bean="${juegoInstance}" field="respuestasPorPregunta"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${juegoInstance?.tipo}">
				<li class="fieldcontain">
					<span id="tipo-label" class="property-label"><g:message code="juego.tipo.label" default="Tipo" /></span>
					
						<span class="property-value" aria-labelledby="tipo-label"><g:fieldValue bean="${juegoInstance}" field="tipo"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:juegoInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${juegoInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
