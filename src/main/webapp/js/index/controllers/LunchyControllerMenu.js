'use strict';

/* Controllers */

angular.module('LunchyApp.controllers').
controller('LunchyControllerMenu', ['$scope', '$location', '$state', function($scope, $location, $state) {
	
	$scope.login = function() {
		$scope.navCollapsed=!$scope.navCollapsed;
		$scope.authetication.showLogin();
	};
	
	$scope.logout = function() {		
		$scope.navCollapsed=!$scope.navCollapsed;
		$scope.authetication.logout();
		$location.path("/");
	};

	$scope.getClass = function(path) {
        if ($location.path().substr(0, path.length) === path) {
            return "active";
        } else {
            return "";
        }
    };
    
    $scope.redirectTo = function(path, param1) {
    	$scope.navCollapsed=!$scope.navCollapsed;
    	var param = {};
    	if(path=="pictures"){
    		param = {startPos: 1};
    	}
    	$state.go(path, param);
    }

}]);