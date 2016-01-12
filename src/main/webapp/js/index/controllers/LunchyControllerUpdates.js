'use strict';

/* Controllers */

angular.module('LunchyApp.controllers').
controller('LunchyControllerUpdates', ['$scope', 'UpdatesDao', '$window', function($scope, UpdatesDao, $window) {

	$scope.windowWidth = $window.innerWidth;
	
	UpdatesDao.query(function(updatesResponse) {
	    $scope.latestUpdates = updatesResponse.latestUpdates;
	    $scope.latestPictures = updatesResponse.latestPictures;
	});

}]);