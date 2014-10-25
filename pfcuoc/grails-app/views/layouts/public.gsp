<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <g:javascript library="application"/>
    <r:require module="bootstrap"/>
    <r:require module="jquery"/>
    <r:require module="jquery-ui"/>
    <g:layoutHead/>
    <r:layoutResources/>

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

<style type="text/css">
.btn-outline {
    color: #563d7c;
    background-color: transparent;
    border-color: #563d7c;
}
    h1 {
        font-weight: 300;
    }
</style>
</head>

<body>

<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">

    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">PFC Alberto Vilches Ratón</a>
        </div>

        <div class="navbar-collapse">
            <g:if test="${usuarioActual}">
                <ul class="nav navbar-nav pull-right">
                  <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">${usuarioActual.nombre} <span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                      <li><a href="#">Perfil</a></li>
                      <li><a href="#">Premios</a></li>
                      <li class="divider"></li>
                      <li><g:link controller="seguridad" action="logout">Cerrar sesión</g:link></li>
                    </ul>
                  </li>
                </ul>
            </g:if>
            <g:else>
                <g:form class="navbar-form navbar-right" role="form" controller="seguridad" action="login">
                    <div class="form-group">
                        <input type="text" name="login" placeholder="login" class="form-control">
                    </div>

                    <div class="form-group">
                        <input type="password" name="password" placeholder="clave" class="form-control">
                    </div>
                    <button type="submit" class="btn btn-success">Entrar</button>
                </g:form>
            </g:else>
        </div><!--/.navbar-collapse -->

    </div>
</div>

<div style="padding-top: 48px;">
</div>

<g:if test="${flash.message}">
    <div class="alert alert-warning alert-dismissible" role="alert">
      <button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
      <strong>Atención: </strong> ${flash.message}
    </div>
</g:if>

<!-- Main jumbotron for a primary marketing message or call to action -->
<div class="container">
    <!-- Example row of columns -->
    <div class="row">

        <g:layoutBody/>

    <div style="clear: both"></div>
    <hr>

        <footer>
            <p>&copy; Company 2014</p>
        </footer>
    </div>
</div>




<!-- Bootstrap core JavaScript
    ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<r:layoutResources/>
</body>
</html>







