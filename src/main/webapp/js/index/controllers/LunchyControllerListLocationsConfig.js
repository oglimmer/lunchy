'use strict';

/* Controllers */

angular.module('LunchyApp.controllers').
controller('LunchyControllerListLocationsConfig', [ '$scope', 'UserDao', '$uibModalInstance', 'showColumnSettingsConfig', 'Authetication', '$cookies',
                                              function($scope, UserDao, $uibModalInstance, showColumnSettingsConfig, Authetication, $cookies) {

	
	// maps index no to strings (the DB saves only the index)
	var itemNames = ["R (Reviewed by myself)", "Official name", $scope.companyName+" name", "Turn around time", "Number of Reviews", "Avg Rating", "Last Rating", "Last Update", "Tags"];
	function buildListViewColPrioUI() {        
		// translates the array of itemNo into array of itemNames
		angular.forEach(showColumnSettingsConfig, function(value) {
			this.push(itemNames[value]);
		}, $scope.listViewColPrioItems);			
	}
	function buildListViewColPrioDB() {
        // translates the array of itemNames into string with itemNo
		var listViewColPrioString = "";
		angular.forEach($scope.listViewColPrioItems, function(listViewColPrioItem) {
			angular.forEach(itemNames, function(itemName, index) {
				if(itemName == listViewColPrioItem) {
					if(listViewColPrioString != "") {
						listViewColPrioString += ",";
					}
					listViewColPrioString += index;
				}
			});
		});
        if(listViewColPrioString=="0,1,8,3,5,2,4,6,7") {
        	// save the default prio into the DB as empty
            return "";
        } else {
        	return listViewColPrioString;
        }
	}

	$scope.listViewColPrioItems = [];
	buildListViewColPrioUI();
	
	$scope.saveConfig = function() {
		var listViewColPrioString = buildListViewColPrioDB();
		Authetication.checkLoggedIn().then(function(data) {
	        if(data.loggedIn){
	        	UserDao.saveListViewColConfig({listViewColPrio: listViewColPrioString});
	        }
		}, function() {
			$cookies.put("listViewColPrio", listViewColPrioString)
		});
		$uibModalInstance.close(listViewColPrioString);
	};
	
	$scope.cancel = function() {
		$uibModalInstance.dismiss('cancel');
    };

	$scope.dragControlListeners = {	    
	};

}]);