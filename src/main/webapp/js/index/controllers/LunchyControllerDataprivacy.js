'use strict';

/* Controllers */

angular.module('LunchyApp.controllers').
controller('LunchyControllerDataprivacy', ['$scope', 'Authetication', function($scope, Authetication) {

  $scope.authetication = Authetication;
   
}]);