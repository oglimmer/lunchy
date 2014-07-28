'use strict';


// Declare app level module which depends on filters, and services
angular.module('LunchyApp', [
  'ngResource', 'ngRoute', 'ui.validate', 'ui.bootstrap',
  'LunchyApp.filters',
  'LunchyApp.services',
  'LunchyApp.directives',
  'LunchyApp.controllers'
]).
config(['$routeProvider', function($routeProvider) {
	$routeProvider.when('/updates', {
		templateUrl : 'partials/updates.html',
		controller : 'LunchyControllerMain'
	}).when('/add', {
		templateUrl : 'partials/add-location.html',
		controller : 'LunchyControllerMain',
		resolve: {
		    auth: ["$q", "Authetication", function($q, Authetication) {
		      if (Authetication.loggedIn) {
		        return $q.when(Authetication);
		      } else {
		        return $q.reject({ authenticated: false });
		      }
		    }]
		  }
	}).otherwise({
		redirectTo : '/updates'
	});
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
factory('ILogin', ['$resource', function($resource) {
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
factory('IUser', ['$resource', function($resource) {
	return $resource('/lunchy/rest/users/:email', null, {
		'create': {
			method: 'POST'
		},
		'lookup': {
			method: 'GET'
		}
	});
}]).
factory('IUpdates', ['$resource', function($resource) {
	return $resource('/lunchy/rest/updates');
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
run(['$rootScope', '$location', 'Authetication',	function($rootScope, $location, Authetication) {
	$rootScope.$on("$routeChangeSuccess", function(userInfo) {
		//console.log(userInfo);
	});

	$rootScope.$on("$routeChangeError", function(event, current, previous, eventObj) {
		if (eventObj.authenticated === false) {
			$location.path("/");
			Authetication.showRegister();
		}
	});
} ]);

