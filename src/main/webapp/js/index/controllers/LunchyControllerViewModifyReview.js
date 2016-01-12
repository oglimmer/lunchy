'use strict';

/* Controllers */

angular.module('LunchyApp.controllers').
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
	
}]);