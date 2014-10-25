<%@ page import="uoc.pfc.bbdd.Juego" %>



<div class="fieldcontain ${hasErrors(bean: juegoInstance, field: 'descripcion', 'error')} required">
	<label for="descripcion">
		<g:message code="juego.descripcion.label" default="Descripcion" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="descripcion" required="" value="${juegoInstance?.descripcion}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: juegoInstance, field: 'estado', 'error')} required">
	<label for="estado">
		<g:message code="juego.estado.label" default="Estado" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="estado" from="${uoc.pfc.bbdd.Juego$Estado?.values()}" keys="${uoc.pfc.bbdd.Juego$Estado.values()*.name()}" required="" value="${juegoInstance?.estado?.name()}" />

</div>

<div class="fieldcontain ${hasErrors(bean: juegoInstance, field: 'nombre', 'error')} required">
	<label for="nombre">
		<g:message code="juego.nombre.label" default="Nombre" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="nombre" required="" value="${juegoInstance?.nombre}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: juegoInstance, field: 'preguntas', 'error')} required">
	<label for="preguntas">
		<g:message code="juego.preguntas.label" default="Preguntas" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="preguntas" type="number" value="${juegoInstance.preguntas}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: juegoInstance, field: 'respuestasPorPregunta', 'error')} required">
	<label for="respuestasPorPregunta">
		<g:message code="juego.respuestasPorPregunta.label" default="Respuestas Por Pregunta" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="respuestasPorPregunta" type="number" value="${juegoInstance.respuestasPorPregunta}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: juegoInstance, field: 'tipo', 'error')} required">
	<label for="tipo">
		<g:message code="juego.tipo.label" default="Tipo" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="tipo" from="${uoc.pfc.bbdd.Juego$Tipo?.values()}" keys="${uoc.pfc.bbdd.Juego$Tipo.values()*.name()}" required="" value="${juegoInstance?.tipo?.name()}" />

</div>

