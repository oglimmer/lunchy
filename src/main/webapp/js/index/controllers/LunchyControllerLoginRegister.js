'use strict';

/* Controllers */

angular.module('LunchyApp.controllers').
controller('LunchyControllerLoginRegister', ['$scope', 'UserDao', 'OfficesDao', function ($scope, UserDao, OfficesDao) {
	
	$scope.newUser = {};

	OfficesDao.query(function(offices) {
		$scope.offices = offices;
	});

	$scope.submitRegister = function() {	
		var newUser = new UserDao($scope.newUser);
		newUser.$save(function(result) {
			if(result.success) {
                $scope.$uibModalInstance.close(result);
			} else {
				$scope.alerts.push({type:'danger',msg:result.errorMsg});
			}
		});		
	};
	
}]);