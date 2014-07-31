'use strict';

/* Controllers */

angular.module('LunchyApp.controllers', []).
controller('LunchyControllerMain', [
	'$scope', 'LoginDao', 'UserDao', 'UpdatesDao', '$location', 'Authetication', 
	function($scope, LoginDao, UserDao, UpdatesDao, $location, Authetication) {
	
	$scope.Authetication = Authetication;
	$scope.latestUpdates = UpdatesDao.query();
		
	$scope.logout = function() {
		LoginDao.logout();	
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
	
	LoginDao.check(function(data) {
		$scope.Authetication.loggedIn = data.success;
	});
}]).
controller('LunchyControllerLogin', ['$scope', 'LoginDao', '$timeout', 'Authetication', function ($scope, LoginDao, $timeout, Authetication) {
	
	$scope.submitLogin = function() {				
		LoginDao.login({email:$scope.email, password:$scope.password}, function(data) {
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
controller('LunchyControllerRegister', ['$scope', '$modalInstance', 'UserDao', function ($scope, $modalInstance, UserDao) {
	
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
		UserDao.create({email:$scope.newUser.email},{password:$scope.newUser.password, displayname:$scope.newUser.nickname}, function(data) {
			if(data.success) {
				$modalInstance.close(true);				
			} else {
				$scope.alerts.push({type:'danger',msg:data.errorMsg});
			}
		});		
	}
	
}]).
controller('LunchyControllerAdd', ['$scope', '$location', 'LocationsDao', function ($scope, $location, LocationsDao) {
	$scope.submitAdd = function() {		
		var newLoc = new LocationsDao({officialname:$scope.officialname, streetname:$scope.streetname, address:$scope.address, city:$scope.city, zip:$scope.zip, comment:$scope.comment, turnaroundtime:$scope.turnaroundtime});
		newLoc.$save(function(result) {
			$location.path("/view/"+result.id);
		});			
	}
	
}]).
controller('LunchyControllerView', ['$scope', '$stateParams', 'LocationsDao', function ($scope, $stateParams, LocationsDao) {
	
	console.log($stateParams.locationId);
	
	$scope.data = LocationsDao.get({ "id": $stateParams.locationId } );
	
}]);

