'use strict';

/* Controllers */

angular.module('LunchyApp.controllers').
controller('LunchyControllerViewEditLocation', ['$scope', 'LocationsDao', 'TagService', '$location',
                                                function ($scope, LocationsDao, TagService, $location) {

	function loadTags() {		
		TagService.get({selectedOffice:$scope.data.selectedOffice.id}).then(function(data) {
			$scope.allTags = data;
		});
	}
	
	$scope.allTags = [];
		
    $scope.editLocationSave = function() {
        LocationsDao.save($scope.data, function(result) {
            $scope.tabs.active = [true, false, false, false];
            $scope.showView();
        }, function(result) {
            $scope.alerts.push({type:'danger', msg: 'Error while saving location: ' + result.statusText});
        });
    };
    
    $scope.editLocationDelete = function() {
    	LocationsDao.delete({id:$scope.data.id}, function(result) {
    		$location.path('/');
        }, function(result) {
            $scope.alerts.push({type:'danger', msg: 'Error while deleting location: ' + result.statusText});
        });
    }
	
	$scope.selectedOfficeChanged = function() {
		$scope.data.fkOffice = $scope.data.selectedOffice.id;
		loadTags();
	}
	
	$scope.$watch('editLocationMode', function(newValue, oldValue) {
		if(newValue) {	    	
	    	$scope.data.selectedOffice = _.find($scope.offices, function(office) { return office.id == $scope.data.fkOffice; });
	    	$scope.selectedOfficeChanged();
		}
    });
	
}]);