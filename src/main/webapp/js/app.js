'use strict';


// Declare app level module which depends on filters, and services
angular.module('LunchyApp', [
  'ngResource', 'ui.router', 'ui.validate', 'ui.bootstrap', 'google-maps', 'ngTable',
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
		state('list', {
		      url: '/list',
		      templateUrl: 'partials/list-locations.html',
		      controller : 'LunchyControllerListLocations'
			}).
		state('passwordReset', {
	      url: '/passwordReset',
	      templateUrl: 'partials/passwordReset.html',
	      controller : 'LunchyControllerPasswordReset'
		}).
	    state('add', {
	      url: '/add',
	      templateUrl: 'partials/add-location.html',
	      controller : 'LunchyControllerAdd',
	      resolve: {
	    	  auth: ["Authetication", function(Authetication) {			    	
			      return Authetication.checkLoggedIn();
			  }]
	      }
	    }).
	    state('settings', {
	      url: '/settings',
	      templateUrl: 'partials/settings.html',
	      controller : 'LunchyControllerSettings',
	      resolve: {
	    	  auth: ["Authetication", function(Authetication) {			    	
			      return Authetication.checkLoggedIn();
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
factory('LoginDao', ['$resource', '$http', function($resource, $http) {
	var ENDPOINT = 'rest/login';
	var LoginDao = $resource(ENDPOINT, null, {
		'login': {
			method: 'POST'
		},
		'logout': {
			method: 'DELETE'
		}
	});	
	// check needs to return a promise
	LoginDao.prototype.check = function() {
		return $http.get(ENDPOINT);
	}
	return LoginDao;
}]).
factory('UserDao', ['$resource', function($resource) {
	return $resource('rest/users/:id', {id: '@id'}, {
		'lookup': {
			method: 'OPTIONS'
		},
		'current': {
			method: 'GET',
			url: 'rest/users/current'
		},
		'sendPasswordLink': {
			method: 'POST',
			url: 'rest/users/:id/sendPasswordLink'			
		},
		'resetPassword': {
			method: 'POST',
			url: 'rest/users/:id/resetPassword'			
		}
	});
}]).
factory('UpdatesDao', ['$resource', function($resource) {
	return $resource('rest/updates');
}]).
factory('LocationsDao', ['$resource', function($resource) {
	return $resource('rest/locations/:id', {id: '@id'}, {
		'queryReviews': {
			method: 'GET',
			url: 'rest/locations/:id/reviews',
			isArray: true
		},
		'userHasReview': {
			method: 'GET',
			url: 'rest/locations/:id/userHasReview'
		}
	});
}]).
factory('ReviewDao', ['$resource', function($resource) {
	return $resource('rest/reviews/:id', {id: '@id'});
}]).
factory('Authetication', ['$modal', '$q', 'LoginDao', function($modal, $q, LoginDao) {
	return {
		loggedIn: false,
		checkLoggedIn: function() {
			var thiz = this;
			if(thiz.loggedIn) {
				return $q.when(thiz);
			} else {
				return new LoginDao().check()
					.then(function(successResp) {
						if(successResp.data.success) {
							thiz.loggedIn = true 
							return $q.when(thiz);
						} else {
							return $q.reject({ authenticated: false });
						}					
					}, function(errorResp) {
						return $q.reject({ authenticated: false });
					});		
			}
		},
		showRegister: function() {
			var thiz = this;
			var modalInstance = $modal.open({
			  templateUrl: 'partials/register.html',
			  controller: 'LunchyControllerRegister'		  
			});
			modalInstance.result.then(function (result) {
				if(result) {
					thiz.loggedIn = true
				}
			}, function () {
				console.log('Modal dismissed at: ' + new Date());
			});
		}
	};
}]).
run(['$rootScope', '$location', 'Authetication', 'LoginDao', function($rootScope, $location, Authetication) {	
	$rootScope.$on('$stateChangeError', function(event, toState, toParams, fromState, fromParams, eventObj) {
		if (eventObj.authenticated === false) {
			$location.path("/");
			Authetication.showRegister();
		}
	});
	Authetication.checkLoggedIn();
}]);
