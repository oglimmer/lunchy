'use strict';

/* Controllers */

angular.module('LunchyApp.controllers').
controller('LunchyControllerFinder', ['$scope', 'TagService', 'UserDao', 'FinderDao', '$timeout', 'Authetication', 'OfficesDao', 'FinderSearchParameter', function($scope, TagService, UserDao, FinderDao, $timeout, Authetication, OfficesDao, FinderSearchParameter) {
	
	// -- local functions
	
	function loadTags() {
		TagService.get({selectedOffice:$scope.data.selectedOffice.id}).then(function(data) {
			$scope.data.inclTags = data.join();
			$scope.data.exclTags = "";
		});
	}
	
	function addTags(listString, element) {
		if(element==""){
			return listString;
		}
		if(listString.length>0){
			listString+=',';
		}
		listString+=element;
		return listString;
	}
	
	function getRestParam() {
		var restParameters = angular.copy($scope.data);
		restParameters.selectedOffice = restParameters.selectedOffice.id; 
		return restParameters;
	}

	// -- scope & local attributes

	$scope.data = FinderSearchParameter;
	$scope.allPartner = [];

	// -- initial queries
	
	OfficesDao.query(function(offices) {
		$scope.offices = offices;
		
		if($scope.data.selectedOffice==null) {
			$scope.data.selectedOffice = _.find($scope.offices, function(office) { return office.id == Authetication.fkBaseOffice; });
			loadTags();
		}				
	});
	
	UserDao.query(function(data) {		
		$scope.allPartner = _.map(data, function(userObj) { return userObj.displayname.replace(/'/g,'´');; });
	});

	// -- scope methods
	
	$scope.search = function() {		
		FinderDao.query(getRestParam(), function(result) {
			$scope.resultData = result;
		});
	};

	$scope.searchRandom = function() {
		FinderDao.queryRandom(getRestParam(), function(result) {
			$scope.resultData = result;
		});
	};

	$scope.removeAll = function() {
		$scope.data.exclTags = addTags($scope.data.exclTags, $scope.data.inclTags);
		$scope.data.inclTags = "";
	};

	$scope.addAll = function() {
		$scope.data.inclTags = addTags($scope.data.inclTags, $scope.data.exclTags);
		$scope.data.exclTags = "";
	};
	
	$scope.selectedOfficeChanged = function() {
		loadTags();
	}
	
	$scope.deleteInclTag = function(key) {
		$scope.data.exclTags = addTags($scope.data.exclTags, key);
	}
	
	$scope.deleteExclTag = function(key) {
		$scope.data.inclTags = addTags($scope.data.inclTags, key);
	}
	
}]);
