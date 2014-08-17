<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" import="de.oglimmer.lunchy.services.LunchyVersion" %>
<!DOCTYPE html>
<html lang="en" class="no-js" xmlns="http://www.w3.org/1999/xhtml" xmlns:ng="http://angularjs.org">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>Lunchy</title>
	<meta name="description" content="Lunchy - the lunch break information platform">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link href="css/styles.css?noCache=<%=LunchyVersion.INSTANCE.getCommit()%>" rel="stylesheet" />
	<link href="webjars/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet"><!-- modal-dialog and more -->
	<link href="css/bootstrap-theme.min.css" rel="stylesheet"><!-- modal-dialog and more -->
</head>
<body>

	<a href="https://github.com/oglimmer/lunchy"><img style="position: absolute; top: 0; right: 0; border: 0;z-index: 2001" src="https://s3.amazonaws.com/github/ribbons/forkme_right_orange_ff7600.png" alt="Fork me on GitHub"></a>
	
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
	
		<div class="row">
			<div class="col-md-6">
				<h3>What is lunchy?</h3>		
				<p>Lunchy is platform to collect and share lunch information with your colleagues and co-workers.</p>
			</div>
			<div class="col-md-6">
				<h3>For whom does it make sense?</h3>		
				<p>For everybody working in a company where people go outside and grab something to eat during lunch break.</p>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<h3>How do I join?</h3>		
				<p>If there is already a space for your company then use the specific URL and create yourself an account.</p>
				<p>When you are the first person from your company then 
				<a href="mailto:&#111;&#108;&#105;&#064;&#122;&#105;&#109;&#112;&#097;&#115;&#115;&#101;&#114;&#046;&#100;&#101;">write me an email</a>
				and request a new space. I am always happy to on-board more companies.</p>
				<p>You are not sure if there is already a space for your company? 
				<a href="mailto:&#111;&#108;&#105;&#064;&#122;&#105;&#109;&#112;&#097;&#115;&#115;&#101;&#114;&#046;&#100;&#101;">Write me an email</a> and we'll figure out together</p>
			</div>
			<div class="col-md-6">
				<h3>Isn't this system not similar to yelp, tripadvisor or foursquare?</h3>		
				<p>Basically yes, but it focuses on information from you and your colleagues to share it with only you and your colleagues. It is not supposed to share with strangers all 
				over the internet. The idea is to share lunch information with people you know and trust.</p>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<h3>Can I test / just see it?</h3>		
				<p>There is a demo space: <a href="//demo.lunchylunch.com">http://demo.lunchylunch.com</a>. You can either register an account there and just <strong>guest/guest</strong>.</p>
			</div>
			<div class="col-md-6">
				<h3>Is it free?</h3>		
				<p>Yes. And it is open-source.</p>
			</div>
		</div>
	
		<hr/>
	
		<footer>
			<p>&copy; oglimmer.de 2014 - <%=LunchyVersion.INSTANCE.getVersion()%></p>
		</footer>
	</div>
	<!-- /container -->
	
</body>
</html>
