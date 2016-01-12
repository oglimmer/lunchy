'use strict';

/* Controllers */

angular.module('LunchyApp.controllers').
controller('LunchyControllerMain', ['$scope', 'Authetication', function($scope, Authetication) {

	$scope.authetication = Authetication;
   
}]);