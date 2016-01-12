'use strict';

/* Controllers */

angular.module('LunchyApp.controllers').
controller('LunchyControllerPasswordReset', ['$scope', 'UserDao', '$location', 'AlertPaneService', function ($scope, UserDao, $location, AlertPaneService) {
	AlertPaneService.add($scope);

	$scope.data = {};
	
	$scope.savePassword = function() {
		UserDao.resetPassword({id:($location.search()).token}, {password:$scope.data.password}, function(result) {
			if(result.success) {
				$location.path("/");
			} else {
				$scope.alerts.push({type:'danger', msg: 'Error while setting password: ' + result.errorMsg});
			}
		});
	};
	
}]);