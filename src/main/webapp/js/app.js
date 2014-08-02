'use strict';


// Declare app level module which depends on filters, and services
angular.module('LunchyApp', [
  'ngResource', 'ui.router', 'ui.validate', 'ui.bootstrap', 'google-maps', 
  'LunchyApp.filters',
  'LunchyApp.services',
  'LunchyApp.directives',
  'LunchyApp.controllers'
]).
config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
	$stateProvider.
	state('updates', {
	      url: '/updates',
	      templateUrl: 'partials/updates.html',
	      controller : 'LunchyControllerMain'
	    }).
		state('view', {
	      url: '/view/:locationId',
	      templateUrl: 'partials/view-location.html',
	      controller : 'LunchyControllerView'
		}).
		state('browse', {
	      url: '/browse',
	      templateUrl: 'partials/browse-locations.html',
	      controller : 'LunchyControllerBrowseLocations'
		}).
	    state('add', {
	      url: '/add',
	      templateUrl: 'partials/add-location.html',
	      controller : 'LunchyControllerAdd',
	      resolve: {
			    auth: ["$q", "Authetication", function($q, Authetication) {			    	
			      if (Authetication.loggedIn) {
			        return $q.when(Authetication);
			      } else {
			        return $q.reject({ authenticated: false });
			      }
			    }]
			  }
	    });
    $urlRouterProvider.otherwise('/updates');
    
}]).
config(function($logProvider){
	$logProvider.debugEnabled(true);
}).
config(['$tooltipProvider', function($tooltipProvider){
    $tooltipProvider.setTriggers({
        'mouseenter': 'mouseleave',
        'click': 'click',
        'focus': 'blur',
        'never': 'mouseleave',
        'show': 'hide'
    });
}]).
factory('LoginDao', ['$resource', function($resource) {
	return $resource('/lunchy/rest/login', null, {
		'login': {
			method: 'POST'
		},
		'logout': {
			method: 'DELETE'
		},
		'check': {
			method: 'GET'
		}
	});
}]).
factory('UserDao', ['$resource', function($resource) {
	return $resource('/lunchy/rest/users/:email', null, {
		'create': {
			method: 'POST'
		},
		'lookup': {
			method: 'GET'
		}
	});
}]).
factory('UpdatesDao', ['$resource', function($resource) {
	return $resource('/lunchy/rest/updates');
}]).
factory('LocationsDao', ['$resource', function($resource) {
	return $resource('/lunchy/rest/locations/:id', {id: '@id'});
}]).
factory('Authetication', ['$modal', function($modal) {
	return {
		loggedIn:false,
		showRegister: function() {
			var thiz = this;
			var modalInstance = $modal.open({
			  templateUrl: 'partials/register.html',
			  controller: 'LunchyControllerRegister'		  
			});
			modalInstance.result.then(function (result) {
				if(result) {
					thiz.Authetication.loggedIn = true;
				}
			}, function () {
				console.log('Modal dismissed at: ' + new Date());
			});
		}
	};
}]).
run(['$rootScope', '$location', 'Authetication', function($rootScope, $location, Authetication) {	
	$rootScope.$on('$stateChangeError', function(event, toState, toParams, fromState, fromParams, eventObj) {
		if (eventObj.authenticated === false) {
			$location.path("/");
			Authetication.showRegister();
		}
	});
}]);
