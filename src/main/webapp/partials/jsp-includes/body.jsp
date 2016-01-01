<%@page import="de.oglimmer.lunchy.services.FileService"%>
<%@page import="de.oglimmer.lunchy.services.LunchyVersion"%>
<%@ taglib prefix="lunchy" uri="http://oglimmer.de/lunchy/tags" %>

<% String DEBUG = (LunchyVersion.INSTANCE.isRunsOnDev() ? "" : "min."); %>


        <script src="webjars/jquery/2.1.4/jquery.<%=DEBUG%>js"></script><!-- popover -->
        <script src="webjars/lodash/3.10.1/lodash.<%=DEBUG%>js"></script><!-- google-maps && my own code -->
        <script src="webjars/bootstrap/3.3.6/js/bootstrap.js"></script><!-- dropdown in menu -->

        <script src="webjars/jquery-ui/1.11.4/jquery-ui.<%=DEBUG%>js"></script><!-- tag autocomplete -->
        <script src="webjars/angularjs/1.4.8/angular.<%=DEBUG%>js"></script><!-- base -->
        <script src="webjars/angularjs/1.4.8/angular-resource.<%=DEBUG%>js"></script><!-- $resource -->
        <script src="webjars/angularjs/1.4.8/angular-route.<%=DEBUG%>js"></script><!-- $route -->
        <script src="webjars/angularjs/1.4.8/angular-touch.<%=DEBUG%>js"></script><!-- touch support -->
        <script src="webjars/angularjs/1.4.8/angular-sanitize.<%=DEBUG%>js"></script><!-- linky -->
        <script src="webjars/angularjs/1.4.8/angular-cookies.<%=DEBUG%>js"></script><!-- cookies -->
        <script src="webjars/angular-ui-validate/1.2.1/dist/validate.<%=DEBUG%>js"></script><!-- validate -->
        <script src="webjars/angular-ui-router/0.2.15/angular-ui-router.<%=DEBUG%>js"></script><!-- ui-router -->
        <script src="webjars/angular-ui-bootstrap/0.14.3/ui-bootstrap-tpls.<%=DEBUG%>js"></script><!-- modal-dialog and more -->
        <script src="webjars/angular-google-maps/2.1.1/angular-google-maps.<%=DEBUG%>js"></script><!-- google-maps -->
        <script src="webjars/ng-table/0.3.3/ng-table.<%=DEBUG%>js"></script><!-- ng-table -->
        <script src="webjars/ng-flow/2.6.1/ng-flow-standalone.<%=DEBUG%>js"></script><!-- file upload -->
        <script src="webjars/modernizr/2.8.3/modernizr.<%=DEBUG%>js"></script>              
        <script src='//maps.googleapis.com/maps/api/js'></script><!-- google-maps -->

        <script>$(function() { $.ajax({ url: "webjars/zxcvbn/1.0/zxcvbn.js", dataType: "script", cache: true }); });</script>
        
        <% if(LunchyVersion.INSTANCE.isRunsOnDev() || !FileService.exists("js/script.min.js", getServletContext()) ) { %>
		<lunchy:script src="js/index/app.js"/>
	    <lunchy:script src="js/portal/app.js"/>
	    <lunchy:script src="js/services.js"/>
	    <lunchy:script src="js/index/controllers.js"/>
	    <lunchy:script src="js/portal/controllers.js"/>
	    <lunchy:script src="js/filters.js"/>
	    <lunchy:script src="js/directives.js"/>
	    <lunchy:script src="js/directives_coffee.js"/>
	    <% } else { %>
	    <lunchy:script src="js/script.min.js"/>
	    <% } %>
        