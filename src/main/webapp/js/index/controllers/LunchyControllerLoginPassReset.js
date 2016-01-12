'use strict';

/* Controllers */

angular.module('LunchyApp.controllers').
controller('LunchyControllerLoginPassReset', ['$scope', 'UserDao', function ($scope, UserDao) {
	
	$scope.data = {};

	$scope.submitPassReset = function() {
		UserDao.sendPasswordLink({id:$scope.passReset.email});
        $scope.$uibModalInstance.dismiss('cancel');
	};
	
}]);