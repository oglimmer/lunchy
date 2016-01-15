'use strict';

/* Controllers */

angular.module('LunchyApp.controllers').
controller('LunchyControllerUpdates', ['$scope', 'UpdatesDao', '$window', 'UsageDao', function($scope, UpdatesDao, $window, UsageDao) {

	$scope.windowWidth = $window.innerWidth;
	
	UpdatesDao.query(function(updatesResponse) {
	    $scope.latestUpdates = updatesResponse.latestUpdates;
	    $scope.latestPictures = updatesResponse.latestPictures;
	});
	
	UsageDao.register({action: 'screenWidth', context: $scope.windowWidth});

}]);