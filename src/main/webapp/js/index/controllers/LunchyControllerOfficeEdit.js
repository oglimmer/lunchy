'use strict';

/* Controllers */

angular.module('LunchyApp.controllers').
controller('LunchyControllerOfficeEdit', ['$scope', 'OfficesDao', '$stateParams', '$location', 'AlertPaneService', function($scope, OfficesDao, $stateParams, $location, AlertPaneService) {
	AlertPaneService.add($scope);

    $scope.data = {};

    if($stateParams.officeId!="") {
        OfficesDao.get({id: $stateParams.officeId}, function (officeResponse) {
            $scope.data = officeResponse;
        });
    }

    $scope.cancel = function() {
        $location.path("office");
    };

    $scope.saveOffice = function() {
        OfficesDao.save($scope.data, function(successResult) {
            $location.path("office");
        }, function(failResult) {
            $scope.alerts.push({type:'danger', msg: 'Error while saving user: ' + failResult.statusText});
        });
    };

    $scope.deleteOffice = function() {
        OfficesDao.delete({id:$scope.data.id}, function(successResult) {
            $location.path("office");
        }, function(failResult) {
            $scope.alerts.push({type:'danger', msg: 'This office has already locations or users set it as their base office. It cannot be deleted anymore.'});
        });
    };

}]);