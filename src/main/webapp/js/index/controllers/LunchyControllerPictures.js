'use strict';

/* Controllers */

angular.module('LunchyApp.controllers').
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

}]);