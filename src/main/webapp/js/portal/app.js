'use strict';


// Declare app level module which depends on filters, and services
angular.module('LunchyPortalApp', [
  'ngResource', 'ui.router', 'ui.validate', 'ui.bootstrap', 'ngTable', 'flow', 'ngTouch', 'ngSanitize', 'ngCookies',
  'LunchyApp.filters',
  'LunchyApp.services',
  'LunchyApp.directives',
  'LunchyPortalApp.controllers'
]).
config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
    $stateProvider.
        state('greetings', {
            url: '/greetings',
            templateUrl: 'partials/portal/greetings.html',
            controller: 'LunchyControllerPortalGreetings'
        }).
        state('create-community', {
            url: '/create-community',
            templateUrl: 'partials/portal/create-community.html',
            controller: 'LunchyControllerPortalCreateCommunity'
        });
    $urlRouterProvider.otherwise('/greetings');
}]).
config(function($logProvider){
	$logProvider.debugEnabled(true);
}).
run(['$rootScope', function($rootScope) {


}]);
