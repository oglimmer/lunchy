'use strict';

/* Controllers */

angular.module('LunchyApp.controllers').
controller('LunchyControllerSettings', [ '$scope', 'UserDao', 'OfficesDao', 'Authetication', 'AlertPaneService', function($scope, UserDao, OfficesDao, Authetication, AlertPaneService) {
	AlertPaneService.add($scope);
	
	$scope.data = {};
	$scope.data.selectedOffice = null;
	
	UserDao.current(function(loadData) {
		$scope.data = loadData;
	});
	
	OfficesDao.query(function(offices) {
		$scope.offices = offices;
		$scope.data.selectedOffice = _.find($scope.offices, function(office) { return office.id == $scope.data.fkBaseOffice; });
	});	
	
	$scope.saveEdit = function() {
		$scope.alerts = [];
		$scope.data.fkBaseOffice = $scope.data.selectedOffice.id;
		UserDao.save($scope.data, function(result) {
			if(!result.success) {
				$scope.alerts.push({type:'danger', msg: 'Error while saving user: ' + result.errorMsg});
			} else {
				$scope.alerts = [ {type:'success', msg: 'Settings saved.'} ];
				$scope.data.currentpassword = '';
				$scope.data.password = '';
				$scope.data.newpasswordverification = '';
				Authetication.fkBaseOffice = result.fkOffice; 
			}
		}, function(result) {
			$scope.alerts.push({type:'danger', msg: 'Error while saving user: ' + result.statusText});
		});		
	};
	
}]);