'use strict';

/* Controllers */

angular.module('LunchyApp.controllers', []).
controller('LunchyControllerMain', [
	'$scope', 'ILogin', 'IUser', 'IUpdates', '$location', 'Authetication', 
	function($scope, ILogin, IUser, IUpdates, $location, Authetication) {
	
	$scope.Authetication = Authetication;
	$scope.latestUpdates = IUpdates.query();
		
	$scope.logout = function() {
		ILogin.logout();	
		$scope.Authetication.loggedIn = false;
		$location.path("/");
	};
	
	$scope.showRegister = function() {
		$scope.Authetication.showRegister();
	}
	
	$scope.getClass = function(path) {
	    if ($location.path().substr(0, path.length) == path) {
	      return "active"
	    } else {
	      return ""
	    }
	}
	
	ILogin.check(function(data) {
		$scope.Authetication.loggedIn = data.success;
	});
}]).
controller('LunchyControllerLogin', ['$scope', 'ILogin', '$timeout', 'Authetication', function ($scope, ILogin, $timeout, Authetication) {
	
	$scope.submitLogin = function() {				
		ILogin.login({email:$scope.email, password:$scope.password}, function(data) {
			if(data.success) {
				$scope.Authetication.loggedIn = true;
				$scope.password = "";
			} else {
				$scope.errorMsg = data.errorMsg;
				$timeout(function() {$('#LoginError').trigger('show');}, 1);
				$timeout(function() {$('#LoginError').trigger('hide');}, 3000);				
			}
		});		
	}
	
}]).
controller('LunchyControllerRegister', ['$scope', '$modalInstance', 'IUser', function ($scope, $modalInstance, IUser) {
	
	$scope.newUser = {};
	$scope.alerts = [];
	
	$scope.cancelRegister = function() {
		$modalInstance.dismiss('cancel');
	}
	
	$scope.closeAlert = function(index) {
		$scope.alerts.splice(index, 1);
	};
	
	$scope.submitRegister = function() {
		console.log($scope.newUser);
		IUser.create({email:$scope.newUser.email},{password:$scope.newUser.password, displayname:$scope.newUser.nickname}, function(data) {
			if(data.success) {
				$modalInstance.close(true);				
			} else {
				$scope.alerts.push({type:'danger',msg:data.errorMsg});
			}
		});		
	}
	
}]).
controller('LunchyControllerAdd', ['$scope', '$location', 'ILocations', function ($scope, $location, ILocations) {
	
	$scope.submitAdd = function() {
		
		$location.path("/view/1");
	}
	
}]).
controller('LunchyControllerView', ['$scope', '$stateParams', 'ILocations', function ($scope, $stateParams, ILocations) {
	
	console.log($stateParams.locationId);
	
	$scope.data = ILocations.get({ "id": $stateParams.locationId } );
	
}]);

