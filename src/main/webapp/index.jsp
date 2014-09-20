<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %><%@ taglib prefix="lunchy" uri="http://oglimmer.de/lunchy/tags" %>
<!DOCTYPE html>
<html lang="en" ng-app="LunchyApp" class="no-js" xmlns="http://www.w3.org/1999/xhtml" xmlns:ng="http://angularjs.org">
<head>
    <jsp:include page="partials/jsp-includes/head.jsp" />
</head>
<body ng-controller="LunchyControllerMain" flow-prevent-drop>

	<a href="https://github.com/oglimmer/lunchy" class="githubBadge"><img style="position: absolute; top: 0; left: 0; border: 0;z-index: 2001" src="https://s3.amazonaws.com/github/ribbons/forkme_left_orange_ff7600.png" alt="Fork me on GitHub"></a>

	<div ng-include="'partials/head-menu.html'"></div>

	<!--[if lt IE 9]><p class="browsehappy">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p><![endif]-->

	<div ui-view></div>

    <jsp:include page="partials/jsp-includes/body.jsp" />

    <lunchy:script src="js/app.js"/>
    <lunchy:script src="js/services.js"/>
    <lunchy:script src="js/controllers.js"/>
    <lunchy:script src="js/filters.js"/>
    <lunchy:script src="js/directives.js"/>
    <lunchy:script src="js/directives_coffee.js"/>

</body>
</html>
