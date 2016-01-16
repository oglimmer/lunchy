<%@page import="de.oglimmer.lunchy.services.FileService"%>
<%@page import="de.oglimmer.lunchy.services.LunchyVersion"%>
<%@ taglib prefix="lunchy" uri="http://oglimmer.de/lunchy/tags" %>

<% String DEBUG = (LunchyVersion.INSTANCE.isRunsOnDev() ? "" : "min."); %>


        <script src="webjars/jquery/2.2.0/jquery.<%=DEBUG%>js"></script><!-- popover -->
        <script src="webjars/lodash/3.10.1/lodash.<%=DEBUG%>js"></script><!-- google-maps && my own code -->

        <script src="webjars/jquery-ui/1.11.4/jquery-ui.<%=DEBUG%>js"></script><!-- tag autocomplete -->
        <script src="webjars/angularjs/1.4.8/angular.<%=DEBUG%>js"></script><!-- base -->
        <script src="webjars/angularjs/1.4.8/angular-resource.<%=DEBUG%>js"></script><!-- $resource -->
        <script src="webjars/angularjs/1.4.8/angular-route.<%=DEBUG%>js"></script><!-- $route -->
        <script src="webjars/angularjs/1.4.8/angular-touch.<%=DEBUG%>js"></script><!-- touch support -->
        <script src="webjars/angularjs/1.4.8/angular-sanitize.<%=DEBUG%>js"></script><!-- linky -->
        <script src="webjars/angularjs/1.4.8/angular-cookies.<%=DEBUG%>js"></script><!-- cookies -->
        <script src="webjars/angularjs/1.4.8/angular-animate.<%=DEBUG%>js"></script><!-- image slider -->
        <script src="webjars/angular-ui-validate/1.2.1/dist/validate.<%=DEBUG%>js"></script><!-- validate -->
        <script src="webjars/angular-ui-router/0.2.15/angular-ui-router.<%=DEBUG%>js"></script><!-- ui-router -->
        <script src="webjars/angular-ui-bootstrap/0.14.3/ui-bootstrap-tpls.<%=DEBUG%>js"></script><!-- modal-dialog and more -->
        <script src="webjars/angular-google-maps/2.2.1/angular-google-maps.<%=DEBUG%>js"></script><!-- google-maps -->
        <script src="webjars/angular-simple-logger/0.1.5/angular-simple-logger.<%=DEBUG%>js"></script><!-- needed for google-maps -->
        <script src="webjars/ng-table/1.0.0-beta.9/dist/ng-table.<%=DEBUG%>js"></script><!-- ng-table -->
        <script src="webjars/ng-flow/2.6.1/ng-flow-standalone.<%=DEBUG%>js"></script><!-- file upload -->
        <script src="webjars/ng-sortable/1.3.2/dist/ng-sortable.<%=DEBUG%>js"></script><!--  -->
        <script src="webjars/modernizr/2.8.3/modernizr.<%=DEBUG%>js"></script>              

        <script>$(function() { $.ajax({ url: "webjars/zxcvbn/4.2.0/dist/zxcvbn.js", dataType: "script", cache: true }); });</script>
        
        <% if(LunchyVersion.INSTANCE.isRunsOnDev() || !FileService.exists("js/script.min.js", getServletContext()) ) { %>
		<lunchy:script src="js/index/app.js"/>
	    <lunchy:script src="js/portal/app.js"/>
	    <lunchy:script src="js/services.js"/>
	    <lunchy:script src="js/index/controllers.js"/>
	    <lunchy:script src="js/index/controllers/LunchyControllerAdd.js"/>
		<lunchy:script src="js/index/controllers/LunchyControllerBrowseLocations.js"/>
		<lunchy:script src="js/index/controllers/LunchyControllerFinder.js"/>
		<lunchy:script src="js/index/controllers/LunchyControllerListLocations.js"/>
		<lunchy:script src="js/index/controllers/LunchyControllerListLocationsConfig.js"/>
		<lunchy:script src="js/index/controllers/LunchyControllerLogin.js"/>
		<lunchy:script src="js/index/controllers/LunchyControllerLoginPassReset.js"/>
		<lunchy:script src="js/index/controllers/LunchyControllerLoginRegister.js"/>
		<lunchy:script src="js/index/controllers/LunchyControllerMain.js"/>
		<lunchy:script src="js/index/controllers/LunchyControllerMenu.js"/>
		<lunchy:script src="js/index/controllers/LunchyControllerOffice.js"/>
		<lunchy:script src="js/index/controllers/LunchyControllerOfficeEdit.js"/>
		<lunchy:script src="js/index/controllers/LunchyControllerPasswordReset.js"/>
		<lunchy:script src="js/index/controllers/LunchyControllerPictures.js"/>
		<lunchy:script src="js/index/controllers/LunchyControllerSettings.js"/>
		<lunchy:script src="js/index/controllers/LunchyControllerUpdates.js"/>
		<lunchy:script src="js/index/controllers/LunchyControllerUser.js"/>
		<lunchy:script src="js/index/controllers/LunchyControllerView.js"/>
		<lunchy:script src="js/index/controllers/LunchyControllerViewAddPicture.js"/>
		<lunchy:script src="js/index/controllers/LunchyControllerViewEditLocation.js"/>
		<lunchy:script src="js/index/controllers/LunchyControllerViewModifyReview.js"/>
		<lunchy:script src="js/index/controllers/LunchyControllerViewTab.js"/>
	    <lunchy:script src="js/portal/controllers.js"/>
	    <lunchy:script src="js/filters.js"/>
	    <lunchy:script src="js/directives.js"/>
	    <lunchy:script src="js/directives_coffee.js"/>
	    <% } else { %>
	    <lunchy:script src="js/script.min.js"/>
	    <% } %>
        