'use strict';

/* Controllers */

angular.module('LunchyApp.controllers').
controller('LunchyControllerViewAddPicture', ['$scope', 'LocationsDao', 'PicturesDao', '$stateParams',
	function ($scope, LocationsDao, PicturesDao, $stateParams) {
	
	$scope.addPictureSave = function() {
        if($scope.childScopeHolder.$flow.files.length>0) {
            var f1 = $scope.childScopeHolder.$flow.files[0];
            if(!f1.isUploading() && f1.size < 1024*1024*15) {
                var newPic = new PicturesDao({fkLocation: $stateParams.locationId, caption: $scope.childScopeHolder.picCaption, uniqueId: f1.uniqueIdentifier, originalFilename: f1.name});
                newPic.$save(function(pic) {
                	$scope.allowedChangeCaption["pic"+pic.id] = true;
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
	
	
}]);