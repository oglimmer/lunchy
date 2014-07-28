'use strict';

/* Controllers */

angular.module('LunchyApp.controllers', []).
controller('LunchyControllerMain', ['$scope', 'ILogin', 'IUser', 'IUpdates', '$modal', '$location', function($scope, ILogin, IUser, IUpdates, $modal, $location) {
	
	$scope.userLoggedIn = false;
	$scope.showRegisterFrame = false;
	$scope.latestUpdates = IUpdates.query();
		
	$scope.logout = function() {
		ILogin.logout();	
		$scope.userLoggedIn = false;
	};
	
	$scope.showRegister = function() {
		var modalInstance = $modal.open({
		  templateUrl: 'partials/register.html',
		  controller: 'LunchyControllerRegister'		  
		});
		modalInstance.result.then(function (result) {
			if(result) {
				$scope.userLoggedIn = true;
			}
		}, function () {
			console.log('Modal dismissed at: ' + new Date());
		});
	}
	
	$scope.getClass = function(path) {
	    if ($location.path().substr(0, path.length) == path) {
	      return "active"
	    } else {
	      return ""
	    }
	}
	
	ILogin.check(function(data) {
		$scope.userLoggedIn = data.success;
	});
}]).
controller('LunchyControllerLogin', ['$scope', 'ILogin', '$timeout', function ($scope, ILogin, $timeout) {
	
	$scope.submitLogin = function() {				
		ILogin.login({email:$scope.email, password:$scope.password}, function(data) {
			if(data.success) {
				$scope.$parent.userLoggedIn = true;
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
	
}]);

