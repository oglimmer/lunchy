'use strict';

/* Controllers */

angular.module('LunchyApp.controllers').
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
				title: "Office " + off.name,
				icon:'images/office.png'
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
        var topArea = angular.element($window).width()<768 ? 110 : 110;
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
			    	title: loc.officialName+" ("+loc.numberOfReviews+"/"+loc.avgRating+"/"+(loc.reviewed?"X":"-")+")",
			    	labelClass:'marker-labels',
	                labelAnchor:'30 0',
	                labelContent:loc.officialName	                
			    }
			});
		});
	});

}]);