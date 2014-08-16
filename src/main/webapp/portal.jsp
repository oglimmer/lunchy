<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" import="de.oglimmer.lunchy.services.LunchyVersion" %>
<!DOCTYPE html>
<html lang="en" class="no-js" xmlns="http://www.w3.org/1999/xhtml" xmlns:ng="http://angularjs.org">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>Lunchy</title>
	<meta name="description" content="">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link href="webjars/jquery-ui/1.11.0/jquery-ui.min.css" rel="stylesheet" /><!-- tag autocomplete -->
	<link href="css/styles.css?noCache=<%=LunchyVersion.INSTANCE.getCommit()%>" rel="stylesheet" />
	<!-- <link href="webjars/angular-ui/0.4.0/angular-ui.min.css" rel="stylesheet" />  -->	
	<link href="webjars/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet"><!-- modal-dialog and more -->
	<link href="css/bootstrap-theme.min.css" rel="stylesheet"><!-- modal-dialog and more -->
	<link href="webjars/ng-table/0.3.3/ng-table.min.css" rel="stylesheet"><!-- ng-table -->
</head>
<body>

	<a href="https://github.com/oglimmer/lunchy" class="githubBadge"><img style="position: absolute; top: 0; left: 0; border: 0;z-index: 2001" src="https://s3.amazonaws.com/github/ribbons/forkme_left_orange_ff7600.png" alt="Fork me on GitHub"></a>
	
	<div class="navbar navbar-default" role="navigation">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse" >
					<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">Lunchy</a>
			</div>
			
			<!--/.navbar-collapse -->
		</div>
	</div>

	<!--[if lt IE 9]><p class="browsehappy">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p><![endif]-->
	
	<!-- Main jumbotron for a primary marketing message or call to action -->
	<div class="jumbotron">
		<div class="container">
			<h1>Welcome to Lunchy!</h1>
			<p>Lunchy not only makes lunch easier it also makes your life better! How does it do that? It collects and provides essential information about lunch places.</p>
		</div>
	</div>
	
	<div class="container">
	
		<footer>
			<p>&copy; oglimmer.de 2014 - <%=LunchyVersion.INSTANCE.getVersion()%></span></p>
		</footer>
	</div>
	<!-- /container -->


	<script src="webjars/jquery/2.1.1/jquery.min.js"></script><!-- popover -->
	<script src="webjars/jquery-ui/1.11.0/jquery-ui.min.js"></script><!-- tag autocomplete -->
	<script src="webjars/angularjs/1.3.0-beta.15/angular.min.js"></script><!-- base -->
	<script src="webjars/angularjs/1.3.0-beta.15/angular-resource.min.js"></script><!-- $resource -->
	<script src="webjars/angularjs/1.3.0-beta.15/angular-route.min.js"></script><!-- $route -->
	<script src="webjars/angularjs/1.3.0-beta.15/angular-touch.min.js"></script><!-- touch support -->
	<script src="webjars/angular-ui-utils/0.1.1/ui-utils.min.js"></script><!-- validate -->
	<script src="webjars/angular-ui-router/0.2.10/angular-ui-router.min.js"></script><!-- ui-router -->
	<!-- <script src="webjars/angular-ui/0.4.0/angular-ui.min.js"></script>  --><!-- needed? -->
	<script src="webjars/angular-ui-bootstrap/0.11.0/ui-bootstrap.min.js"></script><!-- modal-dialog and more -->
	<script src="webjars/angular-ui-bootstrap/0.11.0/ui-bootstrap-tpls.min.js"></script><!-- modal-dialog and more -->
	<script src="webjars/ng-table/0.3.3/ng-table.min.js"></script><!-- ng-table -->
	<script src="webjars/ng-flow/2.4.0/ng-flow-standalone.min.js"></script><!-- file upload -->
	
</body>
</html>
