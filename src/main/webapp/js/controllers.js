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
	
	$scope.alerts = [];
	
	$scope.closeAlert = function(index) {
		$scope.alerts.splice(index, 1);
	};
	
	$scope.submitAdd = function() {		
		var newLoc = new LocationsDao({officialname:$scope.officialname, streetname:$scope.streetname, address:$scope.address, city:$scope.city, zip:$scope.zip, comment:$scope.comment, turnaroundtime:$scope.turnaroundtime});
		newLoc.$save(function(result) {
			$location.path("/view/"+result.id);
		}, function(result) {
			$scope.alerts.push({type:'danger', msg: 'Error while saving: ' + result.statusText});
		});			
	}
	
}]).
controller('LunchyControllerView', ['$scope', '$stateParams', 'LocationsDao', function ($scope, $stateParams, LocationsDao) {
	
	$scope.data = LocationsDao.get({ "id": $stateParams.locationId } );
	$scope.editableButton = 1;
	$scope.alerts = [];
	
	$scope.closeAlert = function(index) {
		$scope.alerts.splice(index, 1);
	};
	
	$scope.cancelEdit = function() {
		$scope.data = LocationsDao.get({ "id": $stateParams.locationId } );
		$scope.editableButton = 1;
	};
	
	$scope.saveEdit = function() {		
		if($scope.editableButton == 0) {
			LocationsDao.save($scope.data, function(result) {
				// nothing to do
			}, function(result) {
				$scope.alerts.push({type:'danger', msg: 'Error while saving: ' + result.statusText});
			});		
		}
	};
	
}]).
controller('LunchyControllerBrowseLocations', [ '$scope', '$stateParams', '$location', '$window', 'LocationsDao', 
                                                function($scope, $stateParams, $location, $window, LocationsDao) {

	$scope.map = {
		center : {
			latitude : 50.032687,
			longitude : 8.705638
		},
		zoom : 13,
		events: {
			tilesloaded: function (map) {
                $scope.$apply(function () {
                    google.maps.event.trigger(map, 'resize');
                });
            }
		}
	};
	
	$scope.$on('$viewContentLoaded', function () {
		$("#browseMap .angular-google-map-container").height(angular.element($window).height()-75);		
	});
	
	LocationsDao.query(function (locations) {
		$scope.markers = [];
		angular.forEach(locations, function(loc, idx) {			
			if(loc.geoLat != null && loc.geoLng != null) {			
				$scope.markers.push({
				    id:loc.id,
				    coords: {
				        latitude: loc.geoLat,
				        longitude: loc.geoLng
				    },
				    title: loc.officialname,
				    events: {
				    	click: function (marker, eventName, args) {
			                console.log('marker clicked:'+loc.officialname+"/"+loc.id);
			                $scope.$apply(function() {
			                	$location.path("/view/"+loc.id);
			                });
			            }			            
			        }
				});
			}			
		});
	});

}]);
