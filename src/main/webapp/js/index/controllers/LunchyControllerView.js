'use strict';

/* Controllers */

angular.module('LunchyApp.controllers').
controller('LunchyControllerView', ['$scope', '$stateParams', 'LocationsDao', 'ReviewDao', 'Authetication', '$timeout', 'PicturesDao', 'OfficesDao', 'TagService', '$location', 'AlertPaneService',
    function ($scope, $stateParams, LocationsDao, ReviewDao, Authetication, $timeout, PicturesDao, OfficesDao, TagService, $location, AlertPaneService) {
	AlertPaneService.add($scope);

	/* ### DATA ### */
    // generic scope holder
    $scope.childScopeHolder = {};
	
    // permissions
    $scope.allowedToEditPermission = false;    
    $scope.allowedChangeCaption = {};

    // status of buttons / areas
    $scope.showButtonsMode= false;
    $scope.showTabMode = true;
    $scope.editLocationMode = false;
    $scope.modifyReviewMode = false;
    $scope.addPictureMode = false;
    $scope.childScopeHolder.reviewButton = "Add Review";
    
    // vote for current picture
    $scope.childScopeHolder.currentPicVoted = false;

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
            $scope.allowedToDeletePermission = result.allowedToDelete;
            if(result.hasReview) {
                $scope.childScopeHolder.usersReview = result.fkReview;
                $scope.childScopeHolder.reviewButton = "Edit Review";
            }
            $scope.picVotes = result.pictureVotes;
            $scope.allowedChangeCaption = result.allowedChangeCaption;
            onSlideChanged();
        });
    }                 

    /* ### SCOPE BUTTON METHODS ### */
    
    $scope.picVoteClicked = function() {
    	var newStateForPicVoted = $scope.childScopeHolder.currentPicVoted;
        var activePicture = _.find($scope.childScopeHolder.pictures, function(pic) { return pic.active; });
        if(_.isUndefined(activePicture)){
            return;
        }
        if(newStateForPicVoted) {
        	$scope.picVotes = _.union($scope.picVotes, [activePicture.id]);
        	PicturesDao.vote({id: activePicture.id}, {direction: 'up'});
        	activePicture.upVotes++;
        } else {
        	$scope.picVotes = _.without($scope.picVotes, activePicture.id);
        	PicturesDao.vote({id: activePicture.id}, {direction: 'down'});
        	activePicture.upVotes--;
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
    	$scope.childScopeHolder.mapTabShown = false;
        $scope.tabs.disabled = [true, true, false, true];
        $scope.addPictureMode = true;
        $scope.showButtonsMode= false;
        $scope.tabs.active = [false, false, true, false];
    };

    $scope.saveMovedPin = function() {
        LocationsDao.updatePosition({ "id": $stateParams.locationId }, {
            lat: $scope.childScopeHolder.marker.newPosition.lat(),
            lng: $scope.childScopeHolder.marker.newPosition.lng()
        });
        $scope.childScopeHolder.marker.pinMoved = false;
        $scope.initialGeoMovedManually = $scope.data.geoMovedManually = true;
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
            
            // edit picture caption end
            $scope.$watch("childScopeHolder.pictures["+idx+"].caption", function(newVal, oldVal) {
            	if(newVal!=oldVal){
	        		var activePicture = $scope.childScopeHolder.pictures[idx];
	        		PicturesDao.save(activePicture);
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

}]);