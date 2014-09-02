'use strict';

/* Controllers */

angular.module('LunchyApp.controllers', []).
controller('LunchyControllerMain', ['$scope', 'LoginDao', 'UserDao', 'UpdatesDao', '$location', 'Authetication', '$modal',
        function($scope, LoginDao, UserDao, UpdatesDao, $location, Authetication, $modal) {

            $scope.Authetication = Authetication;

            $scope.logout = function() {
                LoginDao.logout();
                $scope.Authetication.loggedIn = false;
                $location.path("/");
            };

            $scope.login = function() {
                $scope.Authetication.showLogin();
            };

            $scope.getClass = function(path) {
                if ($location.path().substr(0, path.length) == path) {
                    return "active"
                } else {
                    return ""
                }
            };

}]).
controller('LunchyControllerUpdates', ['$scope', 'UpdatesDao', function($scope, UpdatesDao) {

        $scope.windowWidth = $window.innerWidth;

        UpdatesDao.query(function(updatesResponse) {
            $scope.latestUpdates = updatesResponse.latestUpdates;
            $scope.latestPictures = updatesResponse.latestPictures;
        });

}]).
controller('LunchyControllerLogin', ['$scope', '$modalInstance', 'LoginDao', '$timeout', 'Authetication', 'StorageService', function ($scope, $modalInstance, LoginDao, $timeout, Authetication, StorageService) {

        $scope.initShowMode = 0;
        $scope.alerts = [];
        $scope.$modalInstance = $modalInstance;
        $scope.login = {};

        $scope.passwordForgotten = function() {
            $scope.alerts = [];
            $scope.initShowMode = 1;
        }

        $scope.register = function() {
            $scope.alerts = [];
            $scope.initShowMode = 2;
        }

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        }

        $scope.closeAlert = function(index) {
            $scope.alerts.splice(index, 1);
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
	}
	
}]).
controller('LunchyControllerLoginPassReset', ['$scope', 'UserDao', function ($scope, UserDao) {
	
	$scope.data = {};

	$scope.submitPassReset = function() {
		UserDao.sendPasswordLink({id:$scope.passReset.email});
        $scope.$modalInstance.dismiss('cancel');
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
controller('LunchyControllerAdd', ['$scope', '$location', 'LocationsDao', 'OfficesDao', 'Authetication', 'TagService', function ($scope, $location, LocationsDao, OfficesDao, Authetication, TagService) {
	
	$scope.data = {};
	$scope.alerts = [];
	
	$scope.allTags = [];
	TagService.get().then(function(data) {
		$scope.allTags = data;
	});
	
	OfficesDao.query(function(offices) {
		$scope.offices = offices;
		$scope.data.fkOffice = Authetication.fkBaseOffice;
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
controller('LunchyControllerView', ['$scope', '$stateParams', 'LocationsDao', 'ReviewDao', 'Authetication', '$timeout', 'PicturesDao', 'OfficesDao', 'TagService',
        function ($scope, $stateParams, LocationsDao, ReviewDao, Authetication, $timeout, PicturesDao, OfficesDao, TagService) {


            // permissions
            $scope.allowedToEditPermission = false;

            // status of buttons / areas
            $scope.showButtonsMode= false;
            $scope.showTabMode = true;
            $scope.editLocationMode = false;
            $scope.modifyReviewMode = false;
            $scope.addPictureMode = false;
            $scope.reviewButton = "Add Review";

            // check user permission/hasReviews
            function getlocationStatusForCurrentUser() {
                $scope.showButtonsMode= true;
                LocationsDao.locationStatusForCurrentUser({"id": $stateParams.locationId }, function (result) {
                    $scope.allowedToEditPermission = result.allowedToEdit;
                    if(result.hasReview) {
                        $scope.usersReview = result.fkReview;
                        $scope.reviewButton = "Edit Review";
                    }
                });
            }

            // load location base-data
            LocationsDao.get({ "id": $stateParams.locationId }, function(loadLocationResponse) {
                $scope.data = loadLocationResponse;
                $scope.initialGeoMovedManually = loadLocationResponse.geoMovedManually;
            } );
            // load location reviews
            LocationsDao.queryReviews({"id": $stateParams.locationId }, function (reviews) {
                $scope.reviews = reviews;
                if(reviews.length>0 && !$scope.tabs.active[2]) {
                    $scope.tabs.active = [false, true, false, false];
                }
            });
            // available tags in auto-completion
            $scope.allTags = [];
            // load all tags
            TagService.get().then(function(data) {
                $scope.allTags = data;
            });

            // all alerts currently shown
            $scope.alerts = [];
            // data of review in creation
            $scope.newReview = {
                    fkLocation:$stateParams.locationId,
                    comment:'',
                    favoriteMeal:'',
                    rating:1,
                    ratingExplained:'none'
            };
            // reference to review from current-user
            $scope.usersReview = null;

            // generic scope holder
            $scope.childScopeHolder = {};


            Authetication.checkLoggedIn().then(function(data) {
                if(data.loggedIn){
                    getlocationStatusForCurrentUser();
                };
            });
            $scope.$on('userLoggedIn', function(event) {
                getlocationStatusForCurrentUser();
            });

            // tabs state variable
            $scope.tabs = {};
            $scope.tabs.active = [true, false, false, false];
            $scope.tabs.disabled = [false, false, false, false];

            // load all pictures
            LocationsDao.queryPictures({"id": $stateParams.locationId }, function (pictures) {
                $scope.childScopeHolder.pictures = pictures;
                if(pictures.length > 0) {
                    $scope.tabs.active = [false, false, true, false];
                }
            });

            // load all offices (for this community)
            OfficesDao.query(function(offices) {
                $scope.offices = offices;
            });

            // create map data
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
                }
                angular.forEach($scope.offices, function(off) {
                    if(off.id == $scope.data.fkOffice){
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
                    }
                })
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

            $scope.editLocationStart = function() {
                $scope.showTabMode = false;
                $scope.editLocationMode = true;
                $scope.showButtonsMode= false;
            };

            $scope.modifyReviewStart = function() {
                angular.forEach($scope.reviews, function(rev, idx) {
                    if(rev.id == $scope.usersReview) {
                        $scope.newReview = angular.copy(rev);
                    }
                });
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
            $scope.showView = function() {
                $scope.showTabMode = true;
                $scope.editLocationMode = false;
                $scope.modifyReviewMode = false;
                $scope.addPictureMode = false;
                $scope.showButtonsMode= true;
                $scope.tabs.disabled = [false, false, false, false];
            };

            $scope.editLocationSave = function() {
                LocationsDao.save($scope.data, function(result) {
                    $scope.tabs.active = [true, false, false, false];
                    $scope.showView();
                }, function(result) {
                    $scope.alerts.push({type:'danger', msg: 'Error while saving location: ' + result.statusText});
                });
            };

            $scope.modifyReviewSave = function() {
                var newReview = new ReviewDao($scope.newReview);
                newReview.$save(function(result) {
                    if($scope.usersReview==null) {
                        $scope.reviewButton = "Edit Review";
                    } else {
                        $scope.reviews = _.filter($scope.reviews, function(review) { return review.id !== result.id; });
                    }
                    $scope.data.turnAroundTime = result.locationTurnAroundTime;
                    $scope.reviews.splice(0, 0, result);
                    $scope.usersReview = result.id;
                    $scope.tabs.active = [false, true, false, false];
                    $scope.showView();
                }, function(result) {
                    if(result.status==409) {
                        $scope.alerts.push({type:'danger', msg: 'Location was already reviewed by this user! Refresh this page!'});
                    } else {
                        $scope.alerts.push({type:'danger', msg: 'Error while saving review: ' + result.statusText});
                    }
                });
            }
            $scope.addPictureSave = function() {
                if($scope.childScopeHolder.$flow.files.length>0) {
                    var f1 = $scope.childScopeHolder.$flow.files[0];
                    if(!f1.isUploading() && f1.size < 1024*1024*15) {
                        var newPic = new PicturesDao({fkLocation: $stateParams.locationId, caption: $scope.childScopeHolder.picCaption, uniqueId: f1.uniqueIdentifier, originalFilename: f1.name});
                        newPic.$save(function(pic) {
                            LocationsDao.queryPictures({"id": $stateParams.locationId }, function (pictures) {
                                $scope.childScopeHolder.pictures = pictures;
                            });
                        });
                        $scope.childScopeHolder.$flow.cancel();
                        $scope.childScopeHolder.picCaption = "";
                        $scope.showView();
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
	
	$scope.officeMarker = [];
	OfficesDao.query(function(offices) {
		$scope.offices = offices;
		angular.forEach(offices, function(off) {
			if(off.id == $scope.selectedOffice){
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
			}
		})
	});
	
	$scope.map = {
		loaded:false
	};
	
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
	}, function() {
        initTable([]);
    });

	$scope.$on('userLoggedIn', function(event) {
		UserDao.query(function (data) {
			dataHolder = data;
			$scope.tableParams.reload();
		});
	});
}]);
