<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" import="de.oglimmer.lunchy.services.LunchyVersion" %>
<!DOCTYPE html>
<html lang="en" ng-app="LunchyApp" class="no-js" xmlns="http://www.w3.org/1999/xhtml" xmlns:ng="http://angularjs.org">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>Lunchy</title>
	<meta name="description" content="">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link href="webjars/jquery-ui/1.11.0/jquery-ui.min.css" rel="stylesheet" /><!-- tag autocomplete -->
	<!-- <link href="webjars/angular-ui/0.4.0/angular-ui.min.css" rel="stylesheet" />  -->
	<link href="webjars/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet"><!-- modal-dialog and more -->
	<link href="css/bootstrap-theme.min.css" rel="stylesheet"><!-- modal-dialog and more -->
	<link href="webjars/ng-table/0.3.3/ng-table.min.css" rel="stylesheet"><!-- ng-table -->
    <link href="css/styles.css?noCache=<%=LunchyVersion.INSTANCE.getCommit()%>" rel="stylesheet" />
</head>
<body ng-controller="LunchyControllerMain" flow-prevent-drop>

	<a href="https://github.com/oglimmer/lunchy" class="githubBadge"><img style="position: absolute; top: 0; left: 0; border: 0;z-index: 2001" src="https://s3.amazonaws.com/github/ribbons/forkme_left_orange_ff7600.png" alt="Fork me on GitHub"></a>

	<div ng-include="'partials/head-menu.html'"></div>

	<!--[if lt IE 9]><p class="browsehappy">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p><![endif]-->

	<div ui-view></div>

	<script src="webjars/jquery/2.1.1/jquery.min.js"></script><!-- popover -->
	<script src="webjars/jquery-ui/1.11.0/jquery-ui.min.js"></script><!-- tag autocomplete -->
	<script src="webjars/angularjs/1.3.0-beta.15/angular.min.js"></script><!-- base -->
	<script src="webjars/angularjs/1.3.0-beta.15/angular-resource.min.js"></script><!-- $resource -->
	<script src="webjars/angularjs/1.3.0-beta.15/angular-route.min.js"></script><!-- $route -->
	<script src="webjars/angularjs/1.3.0-beta.15/angular-touch.min.js"></script><!-- touch support -->
	<script src="webjars/angularjs/1.3.0-beta.15/angular-sanitize.min.js"></script><!-- linky -->
	<script src="webjars/angular-ui-utils/0.1.1/ui-utils.min.js"></script><!-- validate -->
	<script src="webjars/angular-ui-router/0.2.10/angular-ui-router.min.js"></script><!-- ui-router -->
	<!-- <script src="webjars/angular-ui/0.4.0/angular-ui.min.js"></script>  --><!-- needed? -->
	<script src="webjars/angular-ui-bootstrap/0.11.0/ui-bootstrap.min.js"></script><!-- modal-dialog and more -->
	<script src="webjars/angular-ui-bootstrap/0.11.0/ui-bootstrap-tpls.min.js"></script><!-- modal-dialog and more -->
	<script src="webjars/ng-table/0.3.3/ng-table.min.js"></script><!-- ng-table -->
	<script src="webjars/ng-flow/2.4.0/ng-flow-standalone.min.js"></script><!-- file upload -->
	
	<script src='//maps.googleapis.com/maps/api/js?sensor=false'></script><!-- google-maps -->
	<script src="webjars/lodash/2.4.1/lodash.underscore.min.js"></script><!-- google-maps && my own code -->
	<script src="js/ext/angular-google-maps.1.2.0.min.js"></script><!-- google-maps -->
	
	<script src="js/cookie.js?noCache=<%=LunchyVersion.INSTANCE.getCommit()%>"></script> <!-- my own cookie lib -->
	<script src="js/app.js?noCache=<%=LunchyVersion.INSTANCE.getCommit()%>"></script>
	<script src="js/services.js?noCache=<%=LunchyVersion.INSTANCE.getCommit()%>"></script>
	<script src="js/controllers.js?noCache=<%=LunchyVersion.INSTANCE.getCommit()%>"></script>
	<script src="js/filters.js?noCache=<%=LunchyVersion.INSTANCE.getCommit()%>"></script>
	<script src="js/directives.js?noCache=<%=LunchyVersion.INSTANCE.getCommit()%>"></script>
	<script src="js/compiled_coffee.js?noCache=<%=LunchyVersion.INSTANCE.getCommit()%>"></script>
</body>
</html>




