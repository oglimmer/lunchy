'use strict';

/* Controllers */

angular.module('LunchyApp.controllers', []).
controller('LunchyControllerMain', [
	'$scope', 'LoginDao', 'UserDao', 'UpdatesDao', '$location', 'Authetication', '$modal',
	function($scope, LoginDao, UserDao, UpdatesDao, $location, Authetication, $modal) {
	
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

	$scope.showHelp = function() {
		$modal.open({
			templateUrl : 'partials/help.html',
			controller : 'LunchyControllerHelp'
		});
	}

	$scope.getClass = function(path) {
	    if ($location.path().substr(0, path.length) == path) {
	      return "active"
	    } else {
	      return ""
	    }
	}
		
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
	};

	// Firefox password manager sets user/pass and angularJS is not informed.
	$scope.init = function() {
		$timeout(function() {
			$('input[ng-model]').trigger('input');
		}, 100);
	};
	
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
		var newUser = new UserDao({email:$scope.newUser.email, password:$scope.newUser.password, displayname:$scope.newUser.nickname});
		newUser.$save(function(result) {
			if(result.success) {
				$modalInstance.close(true);				
			} else {
				$scope.alerts.push({type:'danger',msg:result.errorMsg});
			}
		});		
	}
	
}]).
controller('LunchyControllerHelp', ['$scope', '$modalInstance', 'UserDao', function ($scope, $modalInstance, UserDao) {
	
	$scope.data = {};
	$scope.alerts = [];
	
	$scope.cancelHelp = function() {
		$modalInstance.dismiss('cancel');
	}
	
	$scope.closeAlert = function(index) {
		$scope.alerts.splice(index, 1);
	};
	
	$scope.submitHelp = function() {
		UserDao.sendPasswordLink({id:$scope.data.email});
		$modalInstance.dismiss('cancel');
	}
	
}]).
controller('LunchyControllerPasswordReset', ['$scope', 'UserDao', '$location', function ($scope, UserDao, $location) {
	$scope.data = {};
	$scope.alerts = [];
	
	$scope.closeAlert = function(index) {
		$scope.alerts.splice(index, 1);
	};
	
	$scope.savePassword = function() {
		UserDao.resetPassword({id:($location.search()).token}, {password:$scope.data.password}, function(result) {
			if(result.success) {
				$location.path("/");
			} else {
				$scope.alerts.push({type:'danger', msg: 'Error while setting password: ' + result.errorMsg});
			}
		});
	}
	
}]).
controller('LunchyControllerAdd', ['$scope', '$location', 'LocationsDao', function ($scope, $location, LocationsDao) {
	
	$scope.data = {};
	$scope.alerts = [];
	
	$scope.closeAlert = function(index) {
		$scope.alerts.splice(index, 1);
	};
	
	$scope.submitAdd = function() {				
		var newLoc = new LocationsDao($scope.data);
		newLoc.$save(function(result) {
			$location.path("/view/"+result.id);
		}, function(result) {
			$scope.alerts.push({type:'danger', msg: 'Error while saving: ' + result.statusText});
		});			
	}
	
}]).
controller('LunchyControllerView', ['$scope', '$stateParams', 'LocationsDao', 'ReviewDao', 'Authetication', function ($scope, $stateParams, LocationsDao, ReviewDao, Authetication) {
	
	$scope.data = LocationsDao.get({ "id": $stateParams.locationId } );	
	LocationsDao.queryReviews({"id": $stateParams.locationId }, function (reviews) {
		$scope.reviews = reviews;
	});
	$scope.editableButton = 1;
	$scope.addReviewButton = 0;
	$scope.alerts = [];
	$scope.newReview = {
			fklocation:$stateParams.locationId,
			comment:'',
			favoritemeal:'',
			rating:1,
			ratingExplained:'none'
	};
	$scope.usersReview = null;
	$scope.reviewButton = "Add Review";
	Authetication.checkLoggedIn().then(function(data) {
		if(data.loggedIn){
			LocationsDao.userHasReview({"id": $stateParams.locationId }, function (result) {
				if(result.success) {
					$scope.usersReview = result.errorMsg;
					$scope.reviewButton = "Edit Review";
				}
			});		 
		};		
	});		
	
	function setRatingExplained(val) {
		switch(val) {
		case 1:
			$scope.newReview.ratingExplained = "I will never go there again!";
			break;
		case 2:
			$scope.newReview.ratingExplained = "Not amongst my favorites, but I would eventually join.";
			break;
		case 3:
			$scope.newReview.ratingExplained = "Liked it - somehow. Go there again.";
			break;
		case 4:
			$scope.newReview.ratingExplained = "Nice place, happy to do it again";
			break;
		case 5:
			$scope.newReview.ratingExplained = "One of my favorites! Couldn't get enough!";
			break;
		}
	}
	
	$scope.$watch('newReview.rating', function() {
		setRatingExplained($scope.newReview.rating);
	});
	
	$scope.hoveringOver = function(val) {
		setRatingExplained(val);
	}
	
	$scope.hoveringOut = function() {
		setRatingExplained($scope.newReview.rating);
	}
	
	$scope.closeAlert = function(index) {
		$scope.alerts.splice(index, 1);
	};
	
	$scope.cancelEdit = function() {
		$scope.data = LocationsDao.get({ "id": $stateParams.locationId } );
		$scope.editableButton = 1;
		$scope.addReviewButton = 0;
	};
	
	$scope.saveEdit = function() {		
		if($scope.editableButton == 0) {
			LocationsDao.save($scope.data, function(result) {
				// nothing to do
			}, function(result) {
				$scope.alerts.push({type:'danger', msg: 'Error while saving location: ' + result.statusText});
			});		
		}
	};
	
	$scope.addReview = function() {
		if($scope.addReviewButton == 0) {
			angular.forEach($scope.reviews, function(rev, idx) {
				if(rev.id == $scope.usersReview) {
					$scope.newReview = angular.copy(rev);
				}
			});
		} else {
			var newReview = new ReviewDao($scope.newReview);
			newReview.$save(function(result) {
				if($scope.usersReview==null) {
					$scope.reviewButton = "Edit Review";
				} else {
					angular.forEach($scope.reviews, function(rev, idx) {
						if(rev.id == result.id) {
							$scope.reviews.splice(idx, 1);							
						}
					});
				}
				$scope.reviews.push(result);
				$scope.usersReview = result.id;
			}, function(result) {
				if(result.status==409) {
					$scope.alerts.push({type:'danger', msg: 'Location was already reviewed by this user! Refresh this page!'});
				} else {
					$scope.alerts.push({type:'danger', msg: 'Error while saving review: ' + result.statusText});
				}
			});			

		}
	}
	
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

}]).
controller('LunchyControllerSettings', [ '$scope', 'UserDao', function($scope, UserDao) {
	$scope.data = UserDao.current();
	$scope.alerts = [];
	
	$scope.closeAlert = function(index) {
		$scope.alerts.splice(index, 1);
	};
	
	$scope.saveEdit = function() {
		UserDao.save($scope.data, function(result) {
			if(!result.success) {
				$scope.alerts = [];
				$scope.alerts.push({type:'danger', msg: 'Error while saving user: ' + result.errorMsg});
			} else {
				$scope.alerts = [ {type:'success', msg: 'Settings saved.'} ];
				$scope.data.currentpassword = '';
				$scope.data.password = '';
				$scope.data.newpasswordverification = '';
			}
		}, function(result) {
			$scope.alerts.push({type:'danger', msg: 'Error while saving user: ' + result.statusText});
		});		
	}
}]);
