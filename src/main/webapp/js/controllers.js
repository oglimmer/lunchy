'use strict';

/* Controllers */

angular.module('LunchyApp.controllers', []).
controller('LunchyControllerMain', ['$scope', 'Authetication', function($scope, Authetication) {

	$scope.authetication = Authetication;
   
}]).
controller('LunchyControllerMenu', ['$scope', '$location', function($scope, $location) {
	
	$scope.login = function() {
		$scope.authetication.showLogin();
	};
	
	$scope.logout = function() {		
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

}]).
controller('LunchyControllerUpdates', ['$scope', 'UpdatesDao', '$window', function($scope, UpdatesDao, $window) {

	$scope.windowWidth = $window.innerWidth;
	
	UpdatesDao.query(function(updatesResponse) {
	    $scope.latestUpdates = updatesResponse.latestUpdates;
	    $scope.latestPictures = updatesResponse.latestPictures;
	});

}]).
controller('LunchyControllerLogin', ['$scope', '$modalInstance', 'LoginDao', '$timeout', 'Authetication', 'StorageService', 'AlertPaneService', 
                                     function ($scope, $modalInstance, LoginDao, $timeout, Authetication, StorageService, AlertPaneService) {
	AlertPaneService.add($scope);
	
    $scope.initShowMode = 0;
    $scope.$modalInstance = $modalInstance;
    $scope.login = {};

    $scope.passwordForgotten = function() {
        $scope.alerts = [];
        $scope.initShowMode = 1;
    };

    $scope.register = function() {
        $scope.alerts = [];
        $scope.initShowMode = 2;
    };

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };

    $scope.submitLogin = function() {
        $scope.alerts = [];
        LoginDao.login({email: $scope.login.email, password: $scope.login.password, keepLoggedIn: $scope.login.keepMeLoggedIn}, function(data) {
            if(data.success) {
                Authetication.logInUser(data);
                $scope.password = "";
                if(data.longTimeToken != null && data.longTimeToken != "") {
                    StorageService.save('longTimeToken', data.longTimeToken);
                }
                $modalInstance.close('ok');
            } else {
                $scope.alerts.push({type:'danger',msg:data.errorMsg});
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
controller('LunchyControllerLoginRegister', ['$scope', 'UserDao', 'OfficesDao', function ($scope, UserDao, OfficesDao) {
	
	$scope.newUser = {};

	OfficesDao.query(function(offices) {
		$scope.offices = offices;
	});

	$scope.submitRegister = function() {	
		var newUser = new UserDao($scope.newUser);
		newUser.$save(function(result) {
			if(result.success) {
                $scope.$modalInstance.close(result);
			} else {
				$scope.alerts.push({type:'danger',msg:result.errorMsg});
			}
		});		
	};
	
}]).
controller('LunchyControllerLoginPassReset', ['$scope', 'UserDao', function ($scope, UserDao) {
	
	$scope.data = {};

	$scope.submitPassReset = function() {
		UserDao.sendPasswordLink({id:$scope.passReset.email});
        $scope.$modalInstance.dismiss('cancel');
	};
	
}]).
controller('LunchyControllerPasswordReset', ['$scope', 'UserDao', '$location', 'AlertPaneService', function ($scope, UserDao, $location, AlertPaneService) {
	AlertPaneService.add($scope);

	$scope.data = {};
	
	$scope.savePassword = function() {
		UserDao.resetPassword({id:($location.search()).token}, {password:$scope.data.password}, function(result) {
			if(result.success) {
				$location.path("/");
			} else {
				$scope.alerts.push({type:'danger', msg: 'Error while setting password: ' + result.errorMsg});
			}
		});
	};
	
}]).
controller('LunchyControllerAdd', ['$scope', '$location', 'LocationsDao', 'OfficesDao', 'Authetication', 'TagService', 'AlertPaneService', function ($scope, $location, LocationsDao, OfficesDao, Authetication, TagService, AlertPaneService) {
	AlertPaneService.add($scope);
	
	$scope.data = {};
	
	$scope.allTags = [];
	TagService.get().then(function(data) {
		$scope.allTags = data;
	});
	
	OfficesDao.query(function(offices) {
		$scope.offices = offices;
		$scope.data.fkOffice = Authetication.fkBaseOffice;
	});
	
	$scope.submitAdd = function() {				
		var newLoc = new LocationsDao($scope.data);
		newLoc.$save(function(result) {
			$location.path("/view/"+result.id);
		}, function(result) {
			$scope.alerts.push({type:'danger', msg: 'Error while saving: ' + result.statusText});
		});			
	};
	
}]).
controller('LunchyControllerViewEditLocation', ['$scope', 'LocationsDao', 'TagService', 
	function ($scope, LocationsDao, TagService) {

    // available tags in auto-completion
    $scope.allTags = [];

    // load all tags
    TagService.get().then(function(data) {
        $scope.allTags = data;
    });

    $scope.editLocationSave = function() {
        LocationsDao.save($scope.data, function(result) {
            $scope.tabs.active = [true, false, false, false];
            $scope.showView();
        }, function(result) {
            $scope.alerts.push({type:'danger', msg: 'Error while saving location: ' + result.statusText});
        });
    };

	
}]).
controller('LunchyControllerViewAddPicture', ['$scope', 'LocationsDao', 'PicturesDao', '$stateParams',
	function ($scope, LocationsDao, PicturesDao, $stateParams) {
	
	$scope.addPictureSave = function() {
        if($scope.childScopeHolder.$flow.files.length>0) {
            var f1 = $scope.childScopeHolder.$flow.files[0];
            if(!f1.isUploading() && f1.size < 1024*1024*15) {
                var newPic = new PicturesDao({fkLocation: $stateParams.locationId, caption: $scope.childScopeHolder.picCaption, uniqueId: f1.uniqueIdentifier, originalFilename: f1.name});
                newPic.$save(function(pic) {
                    LocationsDao.queryPictures({"id": $stateParams.locationId }, function (pictures) {
                    	$scope.setPictures(pictures);
                    });
                });
                $scope.childScopeHolder.$flow.cancel();
                $scope.childScopeHolder.picCaption = "";
                $scope.showView();
            }
        }
    };
	
	
}]).
controller('LunchyControllerViewModifyReview', ['$scope', 'ReviewDao',
	function ($scope, ReviewDao) {
	
	$scope.modifyReviewSave = function() {		
		$scope.alerts = [];
        var newReview = new ReviewDao($scope.newReview);
        newReview.$save(function(result) {
            if($scope.childScopeHolder.usersReview===null) {
                $scope.childScopeHolder.reviewButton = "Edit Review";
            } else {            	
                $scope.childScopeHolder.reviews = _.filter($scope.childScopeHolder.reviews, function(review) { return review.id !== result.id; });                
            }
            $scope.data.turnAroundTime = result.locationTurnAroundTime;            
            $scope.childScopeHolder.reviews.splice(0, 0, result);            
            $scope.childScopeHolder.usersReview = result.id;
            $scope.tabs.active = [false, true, false, false];
            $scope.showView();
        }, function(result) {
            if(result.status===409) {
                $scope.alerts.push({type:'danger', msg: 'Location was already reviewed by this user! Refresh this page!'});
            } else {
                $scope.alerts.push({type:'danger', msg: 'Error while saving review: ' + result.statusText});
            }
        });
    };

    $scope.hoveringOver = function(val) {
        setRatingExplained(val);
    };

    $scope.hoveringOut = function() {
   		setRatingExplained($scope.newReview.rating);
    }
    
    $scope.$watch('newReview.rating', function() {
        setRatingExplained($scope.newReview.rating);
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
	
}]).
controller('LunchyControllerViewMap', ['$scope', function ($scope) {
	
	function createMap() {
		return {
            center : {
                latitude : $scope.data.geoLat,
                longitude : $scope.data.geoLng
            },
            zoom : 13
        };
	}
	
	function createMarker() {
		return {
            id:$scope.data.id,
            coords: {
                latitude: $scope.data.geoLat,
                longitude: $scope.data.geoLng
            },
            events: {
                dragend: function(marker, eventName, args) {
                    $scope.marker.newPosition = marker.getPosition();
                    $scope.$apply(function() {
                        if($scope.allowedToEditPermission) {
                            $scope.marker.pinMoved = true;
                        }
                    })
                }
            },
            markerOptions: {
                title: $scope.data.officialName,
                draggable:true
            },
            pinMoved : false
        };
	}
	
	function createOffice() {
		var officeObj = _.find($scope.offices, function(off) { return off.id == $scope.data.fkOffice; });
		return [{
            coords: {
                latitude: officeObj.geoLat,
                longitude: officeObj.geoLng
            },
            markerOptions: {
                title: "Office " + officeObj.name
            },
            id: 'officeMarker'
        }];
	}
	
	// create map data
    $scope.mapSelected = function() {
        $scope.map = createMap();
        $scope.marker = createMarker();        
        $scope.officeMarker = createOffice();
        $scope.mapTabShown = true;
    };	
	
}]).
controller('LunchyControllerView', ['$scope', '$stateParams', 'LocationsDao', 'ReviewDao', 'Authetication', '$timeout', 'PicturesDao', 'OfficesDao', 'TagService', '$location', 'AlertPaneService',
    function ($scope, $stateParams, LocationsDao, ReviewDao, Authetication, $timeout, PicturesDao, OfficesDao, TagService, $location, AlertPaneService) {
	AlertPaneService.add($scope);

	/* ### DATA ### */
    // generic scope holder
    $scope.childScopeHolder = {};
	
    // permissions
    $scope.allowedToEditPermission = false;

    // status of buttons / areas
    $scope.showButtonsMode= false;
    $scope.showTabMode = true;
    $scope.editLocationMode = false;
    $scope.modifyReviewMode = false;
    $scope.addPictureMode = false;
    $scope.childScopeHolder.reviewButton = "Add Review";
    
    // vote for current picture
    $scope.childScopeHolder.currentPicVoted = 0;

    // reference to review from current-user
    $scope.childScopeHolder.usersReview = null;

    // tabs state variable
    $scope.tabs = {};
    $scope.tabs.active = [true, false, false, false];
    $scope.tabs.disabled = [false, false, false, false];
    
    // we don't want to a transition when a direct pic should be shown
    $scope.carouselNoTransition = true;
    
    // data container for new/edit review
    $scope.newReview = {
        fkLocation:$stateParams.locationId,
        comment:'',
        favoriteMeal:'',
        rating:1,
        ratingExplained:'none'
    };

    /* ### PRIVATE METHODS ### */

    function onSlideChanged() {
        var activePicture = _.find($scope.childScopeHolder.pictures, function(pic) { return pic.active; });
        if(_.isUndefined(activePicture)){
            return;
        }
        var activePicIsVoted = _.find($scope.picVotes, function(vote) { return vote == activePicture.id });
        $scope.childScopeHolder.currentPicVoted = !_.isUndefined(activePicIsVoted);
    }

    // check user permission/hasReviews
    function getlocationStatusForCurrentUser() {
        $scope.showButtonsMode= true;
        LocationsDao.locationStatusForCurrentUser({"id": $stateParams.locationId }, function (result) {                	
            $scope.allowedToEditPermission = result.allowedToEdit;
            if(result.hasReview) {
                $scope.childScopeHolder.usersReview = result.fkReview;
                $scope.childScopeHolder.reviewButton = "Edit Review";
            }
            $scope.picVotes = result.pictureVotes;
            onSlideChanged();
        });
    }                 

    /* ### SCOPE BUTTON METHODS ### */
    
    $scope.picVoteClicked = function(newState) {
        var activePicture = _.find($scope.childScopeHolder.pictures, function(pic) { return pic.active; });
        if(_.isUndefined(activePicture)){
            return;
        }
        if(newState) {
            $scope.picVotes = _.without($scope.picVotes, activePicture.id);
            PicturesDao.vote({id: activePicture.id}, {direction: 'down'});
            activePicture.upVotes--;
        } else {
            $scope.picVotes = _.union($scope.picVotes, [activePicture.id]);
            PicturesDao.vote({id: activePicture.id}, {direction: 'up'});
            activePicture.upVotes++;
        }
    };         

    $scope.editLocationStart = function() {
        $scope.showTabMode = false;
        $scope.editLocationMode = true;
        $scope.showButtonsMode= false;
    };

    $scope.modifyReviewStart = function() {
    	if($scope.childScopeHolder.usersReview != null) {
        	$scope.newReview = angular.copy(_.find($scope.childScopeHolder.reviews, function(rev) { return rev.id == $scope.childScopeHolder.usersReview; }));                	
    	}
        $scope.showTabMode = false;
        $scope.modifyReviewMode = true;
        $scope.showButtonsMode= false;
    };

    $scope.addPictureStart = function() {
        $scope.tabs.disabled = [true, true, false, true];
        $scope.addPictureMode = true;
        $scope.showButtonsMode= false;
        $scope.tabs.active = [false, false, true, false];
    };

    $scope.saveMovedPin = function() {
        LocationsDao.updatePosition({ "id": $stateParams.locationId }, {
            lat: $scope.marker.newPosition.lat(),
            lng: $scope.marker.newPosition.lng()
        });
        $scope.marker.pinMoved = false;
    };

    $scope.cancelEdit = function() {
        $scope.data = LocationsDao.get({ "id": $stateParams.locationId } );
        $scope.showView();
        if($scope.childScopeHolder.$flow) {
            $scope.childScopeHolder.$flow.cancel();
        }
    };
    
    /* ### Scope helper methods ### */
    
    $scope.showView = function() {
        $scope.showTabMode = true;
        $scope.editLocationMode = false;
        $scope.modifyReviewMode = false;
        $scope.addPictureMode = false;
        $scope.showButtonsMode= true;
        $scope.tabs.disabled = [false, false, false, false];
    };                   

    $scope.setPictures = function(pictures) {
        $scope.childScopeHolder.pictures = pictures;
        angular.forEach(pictures, function(obj, idx) {
            $scope.$watch("childScopeHolder.pictures["+idx+"].active", function(val) {
                if(val) {
                    onSlideChanged();
                }
            });
        })
    };

    /* ### Scope watches ### */          
    
    $scope.$on('userLoggedIn', function(event) {
        getlocationStatusForCurrentUser();
    });
    
    /* ### RUN ### */

    // load location base-data
    LocationsDao.get({ "id": $stateParams.locationId }, function(loadLocationResponse) {
        $scope.data = loadLocationResponse;
        $scope.initialGeoMovedManually = loadLocationResponse.geoMovedManually;
    } );
    
    // load location reviews
    LocationsDao.queryReviews({"id": $stateParams.locationId }, function (reviews) {
        $scope.childScopeHolder.reviews = reviews;
        if(reviews.length>0 && !$scope.tabs.active[2]) {
            $scope.tabs.active = [false, true, false, false];
        }                
    });
    
    // load all pictures
    LocationsDao.queryPictures({"id": $stateParams.locationId }, function (pictures) {
    	$scope.setPictures(pictures);
        if(pictures.length > 0) {
            $scope.tabs.active = [false, false, true, false];
        }
        if($location.search().pic) {
            _.each(pictures, function(picture) {
                picture.active = (picture.id == $location.search().pic);
            });
        }
        $timeout(function() {
            // need to be async ($currentTransition get stuck on scope otherwise)
            $scope.carouselNoTransition = false;
        })
    });

    // load all offices (for this community)
    OfficesDao.query(function(offices) {
        $scope.offices = offices;
    });

    Authetication.checkLoggedIn().then(function(data) {
        if(data.loggedIn){
            getlocationStatusForCurrentUser();
        }
    });

}]).
controller('LunchyControllerBrowseLocations', [ '$scope', '$stateParams', '$location', '$window', 'OfficesDao', 'Authetication',
                                                function($scope, $stateParams, $location, $window, OfficesDao, Authetication) {

	if(typeof($stateParams.officeId)==='undefined') {
		$location.path('/browse/'+Authetication.fkBaseOffice);
		return;
	}
	$scope.selectedOffice = $stateParams.officeId;
	
	$scope.officeMarker = [];
	$scope.map = {
		loaded:false
	};	
	
	OfficesDao.query(function(offices) {
		$scope.offices = offices;
		
		var off = _.find(offices, function(off) { return off.id == $scope.selectedOffice; });
		$scope.officeMarker = [{
			coords: {
				latitude: off.geoLat,
				longitude: off.geoLng
			},
			markerOptions: {
				title: "Office " + off.name
			},
            id: 'officeMarker'
		}];			
	});
	
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
	
	$scope.$watch("selectedOffice", function() {
		$location.path('/browse/'+$scope.selectedOffice);
	});
		
	$scope.mapCreated = function() {
        var topArea = angular.element($window).width()<768 ? 75 : 110;
		$("#browseMap .angular-google-map-container").height(angular.element($window).height()-topArea);
	};
	
	OfficesDao.locations({id: $scope.selectedOffice}, function (locations) {
		$scope.markers = [];
		var validLocations = _.filter(locations, function(loc) { return loc.geoLat != null && loc.geoLng != null; });
		_.each(validLocations, function(loc) {
			$scope.markers.push({
			    id:loc.id,
			    coords: {
			        latitude: loc.geoLat,
			        longitude: loc.geoLng
			    },
			    title: loc.officialName,
			    events: {
			    	click: function (marker, eventName, args) {
		                //console.log('marker clicked:'+loc.officialName+"/"+loc.id);
		                $scope.$apply(function() {
		                	$location.path("/view/"+loc.id);
		                });
		            }			            
		        },
		        markerOptions: {
			    	title: loc.officialName+" ("+loc.numberOfReviews+"/"+loc.avgRating+"/"+(loc.reviewed?"X":"-")+")"
			    }
			});
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
  	        	turnAroundTime:'@',
  	        	numberOfReviews:'#',
  	        	avgRating:'#'
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
        $scope.tableParams.page(1);
		reloadTableData();
	});
	
}]).
controller('LunchyControllerSettings', [ '$scope', 'UserDao', 'OfficesDao', 'Authetication', 'AlertPaneService', function($scope, UserDao, OfficesDao, Authetication, AlertPaneService) {
	AlertPaneService.add($scope);

	$scope.data = UserDao.current();
	
	OfficesDao.query(function(offices) {
		$scope.offices = offices;
	});	
	
	$scope.saveEdit = function() {
		$scope.alerts = [];
		UserDao.save($scope.data, function(result) {
			if(!result.success) {
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
	};
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
    };

    $scope.changePermission = function(item, newPerm) {
        item.permissions = newPerm;
        UserDao.savePermission({id:item.id}, {permissions:item.permissions});
        deactivateEdit();
    };

    function initTable(data) {
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
    }

    UserDao.query(function (data) {
        initTable(data);
    });

}]).
controller('LunchyControllerOffice', ['$scope', 'ngTableParams', '$filter', 'OfficesDao', '$location', function($scope, ngTableParams, $filter, OfficesDao, $location) {

    var dataHolder = null;

    $scope.rowclick = function(item) {
        $location.path("office-edit/"+item.id);
    };
    
    $scope.newOffice = function() {
        $location.path("office-edit/");
    };

    function initTable(data) {
        dataHolder = data;
        $scope.tableParams = new ngTableParams({
            page: 1,
            count: 10,
            sorting: {
                name: 'asc'
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
    }

    OfficesDao.query(function (data) {
        initTable(data);
    });
}]).
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

}]).
controller('LunchyControllerPictures', ['$scope', 'PicturesDao', '$stateParams', '$window', '$location', function($scope, PicturesDao, $stateParams, $window, $location) {

    $scope.windowWidth = $window.innerWidth;

    $scope.numberOfRecords = 4;
    $scope.startPos = (parseInt($stateParams.startPos)-1)*$scope.numberOfRecords;

    $scope.data = [];

    $scope.back = function() {
        if(parseInt($stateParams.startPos) > 1) {
            $location.path("pictures/"+(parseInt($stateParams.startPos)-1));
        }
    };

    $scope.next = function() {
        if($scope.data.length>0) {
            $location.path("pictures/"+(parseInt($stateParams.startPos)+1));
        }
    };

    PicturesDao.query({startPos: $scope.startPos, numberOfRecords: $scope.numberOfRecords}, function (queryResult) {
        $scope.data = queryResult;
    });

}]).
controller('LunchyControllerFinder', ['$scope', 'TagService', function($scope, TagService) {
	
	$scope.data = {};
	
	$scope.allTags = [];
	TagService.get().then(function(data) {
		$scope.allTags = data;
		$scope.data.inclTags = data.join();
	});
	
}]);
