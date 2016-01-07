'use strict';


// Declare app level module which depends on filters, and services
angular.module('LunchyApp', [
  'ngResource', 'ui.router', 'ui.validate', 'ui.bootstrap', 'uiGmapgoogle-maps', 'ngTable', 'flow', 'ngTouch', 'ngSanitize', 'ngCookies', 'as.sortable',
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
            controller: 'LunchyControllerUpdates'
        }).
        state('view', {
            url: '/view/:locationId',
            templateUrl: 'partials/view-location.html',
            controller: 'LunchyControllerView'
        }).
        state('browse', {
            url: '/browse',
            templateUrl: 'partials/browse-locations.html',
            controller: 'LunchyControllerBrowseLocations'
        }).
        state('browse-by-office', {
            url: '/browse/:officeId',
            templateUrl: 'partials/browse-locations.html',
            controller: 'LunchyControllerBrowseLocations'
        }).
        state('list', {
            url: '/list',
            templateUrl: 'partials/list-locations.html',
            controller: 'LunchyControllerListLocations'
        }).
        state('pictures', {
            url: '/pictures/:startPos',
            templateUrl: 'partials/pictures.html',
            controller: 'LunchyControllerPictures'
        }).
        state('finder', {
            url: '/finder',
            templateUrl: 'partials/finder.html',
            controller: 'LunchyControllerFinder'
        }).
        state('passwordReset', {
            url: '/passwordReset',
            templateUrl: 'partials/passwordReset.html',
            controller: 'LunchyControllerPasswordReset'
        }).
        state('user', {
            url: '/user',
            templateUrl: 'partials/users.html',
            controller: 'LunchyControllerUser',
            resolve: {
                auth: ["Authetication", function (Authetication) {
                    return Authetication.checkIsAdmin();
                }]
            }
        }).
        state('office', {
            url: '/office',
            templateUrl: 'partials/offices.html',
            controller: 'LunchyControllerOffice',
            resolve: {
                auth: ["Authetication", function (Authetication) {
                    return Authetication.checkIsAdmin();
                }]
            }
        }).
        state('offices-edit', {
            url: '/office-edit/:officeId',
            templateUrl: 'partials/offices-edit.html',
            controller: 'LunchyControllerOfficeEdit'
        }).
        state('add', {
            url: '/add',
            templateUrl: 'partials/add-location.html',
            controller: 'LunchyControllerAdd',
            resolve: {
                auth: ["Authetication", function (Authetication) {
                    return Authetication.checkLoggedIn();
                }]
            }
        }).
        state('settings', {
            url: '/settings',
            templateUrl: 'partials/settings.html',
            controller: 'LunchyControllerSettings',
            resolve: {
                auth: ["Authetication", function (Authetication) {
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
config(['flowFactoryProvider', function (flowFactoryProvider) {
  flowFactoryProvider.defaults = {
    target: './upload',
    method:'octet',
    singleFile:true    
  };
  flowFactoryProvider.on('catchAll', function (event) {
    //console.log('catchAll', arguments);
  });
  // Can be used with different implementations of Flow.js
  // flowFactoryProvider.factory = fustyFlowFactory;
}]).
run(['$rootScope', 'Authetication', 'LoginDao', '$window', '$interval', function($rootScope, Authetication, LoginDao, $window, $interval) {

    $rootScope.$on('$stateChangeError', function(event, toState, toParams, fromState, fromParams, eventObj) {
		if (eventObj.authenticated === false) {
            $window.location = "./";
		}
	});

    // this is need for mobile devices. A user could open the page and then close the app.
    // When he comes back (maybe after days) the server-side session would be gone, so re-created it on "pageShow"
    $window.addEventListener("pageshow", function(evt){
        Authetication.login();
    }, false);

	Authetication.checkLoggedIn();

	
	function keepAlive() {
		if(Authetication.loggedIn){
			LoginDao.check();
		}
		
	}
	$interval(keepAlive, 1000*60*5);
	
}]);
