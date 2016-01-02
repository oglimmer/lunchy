    <%@page import="de.oglimmer.lunchy.services.FileService"%>
	<%@page import="de.oglimmer.lunchy.services.LunchyVersion"%>
    <%@ taglib prefix="lunchy" uri="http://oglimmer.de/lunchy/tags" %>
    
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>Lunchy</title>
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="webjars/normalize.css/3.0.2/normalize.css" rel="stylesheet" />
        <link href="webjars/jquery-ui/1.11.4/jquery-ui.min.css" rel="stylesheet" /><!-- tag autocomplete -->
        <link href="webjars/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet"><!-- modal-dialog and more -->
        <link href="webjars/ng-table/1.0.0-beta.9/dist/ng-table.min.css" rel="stylesheet"><!-- ng-table -->
        <% if(LunchyVersion.INSTANCE.isRunsOnDev() || !FileService.exists("css/styles.min.js", getServletContext()) ) { %>
	        <link href="css/html5-boilerplate-main.css" rel="stylesheet" />
	        <link href="css/bootstrap-theme.min.css" rel="stylesheet"><!-- modal-dialog and more -->
	        <lunchy:style href="css/styles.css"/>
        <% } else { %>
		    <lunchy:style href="css/styles.min.css"/>
	    <% } %>
        <link rel="shortcut icon" type="image/x-icon" href="./favicon.ico" />
