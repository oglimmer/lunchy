'use strict';

/* Controllers */

angular.module('LunchyApp.controllers').
controller('LunchyControllerViewTab', ['$scope', function ($scope) {
	
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
                	$scope.childScopeHolder.marker.newPosition = marker.getPosition();
                    $scope.$apply(function() {
                        if($scope.allowedToEditPermission) {
                        	$scope.childScopeHolder.marker.pinMoved = true;
                        }
                    })
                }
            },
            markerOptions: {
                title: $scope.data.officialName,
                draggable:true,
                labelClass:'marker-labels',
                labelAnchor:'30 0',
                labelContent: $scope.data.officialName
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
                title: "Office " + officeObj.name,
                icon:'images/office.png'
            },
            id: 'officeMarker'
        }];
	}
	
	// create map data
    $scope.mapSelected = function() {
        $scope.map = createMap();
        $scope.childScopeHolder.marker = createMarker();        
        $scope.officeMarker = createOffice();
        $scope.childScopeHolder.mapTabShown = true;
    };	
    
    $scope.mapDeselected = function() {
    	$scope.childScopeHolder.mapTabShown = false;
    };
	
}]);