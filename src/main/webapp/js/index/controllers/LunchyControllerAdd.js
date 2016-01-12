'use strict';

/* Controllers */

angular.module('LunchyApp.controllers').
controller('LunchyControllerAdd', ['$scope', '$location', 'LocationsDao', 'OfficesDao', 'Authetication', 'TagService', 'AlertPaneService', function ($scope, $location, LocationsDao, OfficesDao, Authetication, TagService, AlertPaneService) {
	AlertPaneService.add($scope);

	function loadTags() {
		TagService.get({selectedOffice:$scope.data.selectedOffice.id}).then(function(data) {
			$scope.allTags = data;
		});
	}
	
	$scope.data = {};
	$scope.data.selectedOffice = null;	
	$scope.allTags = [];
	
	OfficesDao.query(function(offices) {
		$scope.offices = offices;
		$scope.data.selectedOffice = _.find($scope.offices, function(office) { return office.id == Authetication.fkBaseOffice; });
		$scope.selectedOfficeChanged();
	});
	
	$scope.submitAdd = function() {				
		var newLoc = new LocationsDao($scope.data);
		newLoc.$save(function(result) {
			$location.path("/view/"+result.id);
		}, function(result) {
			$scope.alerts.push({type:'danger', msg: 'Error while saving: ' + result.statusText});
		});			
	};
	
	$scope.selectedOfficeChanged = function() {
		$scope.data.fkOffice = $scope.data.selectedOffice.id;
		loadTags();
	}
	
}]);