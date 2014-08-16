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
controller('LunchyControllerLogin', ['$scope', 'LoginDao', '$timeout', 'Authetication', 'StorageService', function ($scope, LoginDao, $timeout, Authetication, StorageService) {
	
	$scope.submitLogin = function() {
		LoginDao.login({email:$scope.email, password:$scope.password}, function(data) {
			if(data.success) {
				$scope.Authetication.logInUser(data);
				$scope.password = "";
				if($scope.email.indexOf('#')===0) {
					StorageService.save('longTimeToken', data.errorMsg);
				}
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
controller('LunchyControllerRegister', ['$scope', '$modalInstance', 'UserDao', 'OfficesDao', function ($scope, $modalInstance, UserDao, OfficesDao) {
	
	$scope.newUser = {};
	$scope.alerts = [];

	OfficesDao.query(function(offices) {
		$scope.offices = offices;
	});

	$scope.cancelRegister = function() {
		$modalInstance.dismiss('cancel');
	}
	
	$scope.closeAlert = function(index) {
		$scope.alerts.splice(index, 1);
	};
	
	$scope.submitRegister = function() {	
		var newUser = new UserDao($scope.newUser);
		newUser.$save(function(result) {
			if(result.success) {
				$modalInstance.close(result);				
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
controller('LunchyControllerAdd', ['$scope', '$location', 'LocationsDao', 'OfficesDao', 'Authetication', function ($scope, $location, LocationsDao, OfficesDao, Authetication) {
	
	$scope.data = {};
	$scope.alerts = [];
	
	$scope.allTags = ["Mex", "Italian", "Burger", "Döner", "Thai", "Chinese", "Indian", "Sandwich", "Wurst", "Fish"];
	
	OfficesDao.query(function(offices) {
		$scope.offices = offices;
		$scope.data.fkoffice = Authetication.fkBaseOffice;
	});
	
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
controller('LunchyControllerView', ['$scope', '$stateParams', 'LocationsDao', 'ReviewDao', 'Authetication', '$timeout', 'PicturesDao', 'OfficesDao',
                                    function ($scope, $stateParams, LocationsDao, ReviewDao, Authetication, $timeout, PicturesDao, OfficesDao) {
	
	$scope.allowedToEdit = false;
	function getlocationStatusForCurrentUser() {
		LocationsDao.locationStatusForCurrentUser({"id": $stateParams.locationId }, function (result) {
			$scope.allowedToEdit = result.allowedToEdit;
			if(result.hasReview) {
				$scope.usersReview = result.fkReview;
				$scope.reviewButton = "Edit Review";
			}
		});
	}
	
	$scope.data = LocationsDao.get({ "id": $stateParams.locationId } );	
	LocationsDao.queryReviews({"id": $stateParams.locationId }, function (reviews) {
		$scope.reviews = reviews;
	});
	$scope.allTags = ["Mex", "Italian", "Burger", "Döner", "Thai", "Chinese", "Indian", "Sandwich", "Wurst", "Fish"];	
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
	$scope.flowHolder = {};
	Authetication.checkLoggedIn().then(function(data) {
		if(data.loggedIn){
			getlocationStatusForCurrentUser();	 
		};		
	});
	$scope.$on('userLoggedIn', function(event) {
		getlocationStatusForCurrentUser();
	});
	$scope.tabs = {};
	$scope.tabs.pictureTabActive = false;
	$scope.picHolder = {};
	LocationsDao.queryPictures({"id": $stateParams.locationId }, function (pictures) {
		$scope.picHolder.pictures = pictures;
		if(pictures.length > 0) {
			$scope.tabs.pictureTabActive = true;
		}
	});
	
	OfficesDao.query(function(offices) {
		$scope.offices = offices;		
	});
	
	$scope.mapSelected = function() {
		$scope.map = {
				center : {
					latitude : $scope.data.geoLat, 
					longitude : $scope.data.geoLng
				},
				zoom : 13
		};
		$scope.marker = {
				id:$scope.data.id,
				coords: {
					latitude: $scope.data.geoLat, 
					longitude: $scope.data.geoLng
				},
				markerOptions: {
					title: $scope.data.officialname
				}
		}
		$scope.mapTabShown = true;
	};
	
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
		$scope.addPictureButton = 0;
		if($scope.flowHolder.$flow) {
			$scope.flowHolder.$flow.cancel();
		}
	};
	
	$scope.saveEdit = function() {		
		if($scope.editableButton == 0) {
			if($scope.editLocation.$invalid) {
				$scope.alerts.push({type:'danger', msg: 'Missing fields'});
				$timeout(function() {
					$scope.editableButton = 0;
				});
			} else {
				LocationsDao.save($scope.data, function(result) {
					// nothing to do
				}, function(result) {
					$scope.alerts.push({type:'danger', msg: 'Error while saving location: ' + result.statusText});
				});
			}
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
					$scope.reviews = _.filter($scope.reviews, function(review) { return review.id !== result.id; });
				}		
				$scope.reviews.splice(0, 0, result);
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
	$scope.addPicture = function() {
		if($scope.addPictureButton==1) {
			if($scope.flowHolder.$flow.files.length>0) {
				var f1 = $scope.flowHolder.$flow.files[0];
				if(f1.isUploading() || f1.size > 1024*1024*15) {
					$scope.addPictureButton=0;					
				} else {
				
					var newPic = new PicturesDao({fklocation: $stateParams.locationId, caption: $scope.flowHolder.picCaption, uniqueId: f1.uniqueIdentifier, originalFilename: f1.name});
					newPic.$save(function(pic) {
						LocationsDao.queryPictures({"id": $stateParams.locationId }, function (pictures) {
							$scope.picHolder.pictures = pictures;
						});
					});
					$scope.flowHolder.$flow.cancel();
					$scope.flowHolder.picCaption = "";
					$scope.tabs.pictureTabActive = true;
				}				
			} else {
				$scope.addPictureButton=0;
			}
		}
	}
	
}]).
controller('LunchyControllerBrowseLocations', [ '$scope', '$stateParams', '$location', '$window', 'LocationsDao', 'OfficesDao', 'Authetication',
                                                function($scope, $stateParams, $location, $window, LocationsDao, OfficesDao, Authetication) {

	if(typeof($stateParams.officeId)==='undefined') {
		$location.path('/browse/'+Authetication.fkBaseOffice);
		return;
	}
	$scope.selectedOffice = $stateParams.officeId;
	
	OfficesDao.query(function(offices) {
		$scope.offices = offices;	
	});
	
	$scope.map = {
		loaded:false
	};
	Authetication.checkLoggedIn().then(function(data) {		
		OfficesDao.get({id:$scope.selectedOffice}).$promise.then(function(office) {			
			$scope.map = {
				center : {
					latitude : office.geoLat,
					longitude : office.geoLng
				},
				zoom : office.zoomfactor,
				events: {
					tilesloaded: function (map) {
		                $scope.$apply(function () {
		                    google.maps.event.trigger(map, 'resize');
		                });
		            }
				}
			};			
			$scope.map.loaded = true;
		});
	});
	
	$scope.$watch("selectedOffice", function() {
		$location.path('/browse/'+$scope.selectedOffice);
	});
		
	$scope.mapCreated = function() {
		$("#browseMap .angular-google-map-container").height(angular.element($window).height()-75);
	}
	
	OfficesDao.locations({id: $scope.selectedOffice}, function (locations) {
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
			        },
			        markerOptions: {
				    	title: loc.officialname+" ("+loc.numberOfReviews+"/"+loc.avgRating+"/"+(loc.reviewed?"X":"-")+")"
				    }
				});
			}			
		});
	});

}]).
controller('LunchyControllerListLocations', [ '$scope', '$location', 'LocationsDao', '$filter', 'ngTableParams', 'ListConfig', 'Comparator', 'OfficesDao', 'Authetication',
                                                function($scope, $location, LocationsDao, $filter, ngTableParams, ListConfig, Comparator, OfficesDao, Authetication) {
	
	$scope.rowclick = function(item) {
		$location.path('/view/'+item.id);
	};
	
	OfficesDao.query(function(offices) {
		$scope.offices = offices;
	});
	
	$scope.selectedOffice = Authetication.fkBaseOffice;
	var dataHolder = [];
	
	$scope.tableParams = new ngTableParams(ListConfig, {
        total: dataHolder.length,
        getData: function($defer, params) {
        	ListConfig.copyParams(params);	        	
        	
  	        var filterParams = angular.copy(params.filter());
  	        
  	        function flagIntegerSearch(attr, flagChar) {
  	        	if(typeof(filterParams[attr]) !== 'undefined') {
	  	        	if(filterParams[attr] != "") {
	  	        		filterParams[attr] = flagChar+filterParams[attr];
	  	        	} else {
	  	        		delete filterParams[attr];
	  	        	}
	  	        }
  	        }
  	        
  	        var flags = {
  	        	turnaroundtime:'@',
  	        	numberOfReviews:'#',
  	        	avgRating:'#',
  	        }
  	        
  	        for(var key in filterParams) {
  	        	flagIntegerSearch(key, typeof(flags[key])!=='undefined'?flags[key]:'');
  	        }	  	       
  	        
  	        var filterData = $filter('filter')(dataHolder, filterParams, Comparator);
  	        
            var orderedData = $filter('orderBy')(filterData, params.orderBy());

            var pagedData = orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count());

            params.total(filterData.length);
            return $defer.resolve(pagedData);	        	
        }
	});
	
	function reloadTableData() {
		OfficesDao.locations({id: $scope.selectedOffice}, function (data) {
			dataHolder = data;
			$scope.tableParams.reload();
		});
	}
	
	$scope.$on('userLoggedIn', function(event) {
		reloadTableData();
	});
	
	$scope.$watch('selectedOffice', function() {
		reloadTableData();
	});
	
}]).
controller('LunchyControllerSettings', [ '$scope', 'UserDao', 'OfficesDao', 'Authetication', function($scope, UserDao, OfficesDao, Authetication) {
	$scope.data = UserDao.current();
	$scope.alerts = [];
	
	OfficesDao.query(function(offices) {
		$scope.offices = offices;
	});	
	
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
				Authetication.fkBaseOffice = result.fkOffice; 
			}
		}, function(result) {
			$scope.alerts.push({type:'danger', msg: 'Error while saving user: ' + result.statusText});
		});		
	}
}]).
controller('LunchyControllerUser', ['$scope', 'UserDao', 'ngTableParams', '$filter', function($scope, UserDao, ngTableParams, $filter) {
	
	var dataHolder = null;
	$scope.lineEdit = {};
	$scope.activeRow = null;
	
	function deactivateEdit() {
		if($scope.activeRow != null) {
			$scope.lineEdit[$scope.activeRow] = false;
			$scope.activeRow = null;
		}		
	}
	
	$scope.rowclick = function(item) {
		deactivateEdit();
		$scope.activeRow = item.id;
		$scope.lineEdit[item.id] = true;
	}
	
	$scope.changePermission = function(item, newPerm) {
		item.permissions = newPerm;
		UserDao.savePermission({id:item.id}, {permissions:item.permissions});
		deactivateEdit();			
	}
	
	UserDao.query(function (data) {
		dataHolder = data;
		$scope.tableParams = new ngTableParams({
			page: 1,
	        count: 10,
	        sorting: {
	        	email: 'asc'
	        }
		}, {
	        total: dataHolder.length,
	        getData: function($defer, params) {
	        		  	        
	  	        var filterData = $filter('filter')(dataHolder, params.filter());
	  	        
	            var orderedData = $filter('orderBy')(filterData, params.orderBy());

	            var pagedData = orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count());

	            params.total(filterData.length);
	            return $defer.resolve(pagedData);	        	
	        }
	    });
	});
	$scope.$on('userLoggedIn', function(event) {
		UserDao.query(function (data) {
			dataHolder = data;
			$scope.tableParams.reload();
		});
	});
}]);
