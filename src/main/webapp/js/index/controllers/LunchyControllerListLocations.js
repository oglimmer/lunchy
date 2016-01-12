'use strict';

/* Controllers */

angular.module('LunchyApp.controllers').
controller('LunchyControllerListLocations', [ '$scope', '$location', 'LocationsDao', '$filter', 'NgTableParams', 'ListConfig', 'Comparator', 'OfficesDao', 'Authetication', 'UserDao', '$uibModal', '$cookies',
                                                function($scope, $location, LocationsDao, $filter, NgTableParams, ListConfig, Comparator, OfficesDao, Authetication, UserDao, $uibModal, $cookies) {
	
	
	// -- local functions
	
	function reloadTableData() {
		var currentOfficeId = $scope.selectedOffice != null ? $scope.selectedOffice.id : Authetication.fkBaseOffice;
		OfficesDao.locations({id: currentOfficeId}, function (data) {
			dataHolder = data;
			$scope.tableParams.reload();
		});
	}
	
	function setColVisibility() {
        var minColWidth = [46,83,83,83,83,83,77,77,100];
		var screenWidth=$(window).width();
        var usedWidth = 0;
		angular.forEach($scope.showColumnSettingsConfig, function(val) {
            usedWidth += minColWidth[val];
            $scope.showColumnSettings[val] = usedWidth < screenWidth;            
        });
	}
	
	function init(listViewColPrioString) {
		if(_.isUndefined(listViewColPrioString) || listViewColPrioString == "") {
            $scope.showColumnSettingsConfig = [0,1,8,3,5,2,4,6,7]; // default prios
        } else {
            $scope.showColumnSettingsConfig = listViewColPrioString.split(",");            
        }		
        setColVisibility();
	}

	// -- scope & local attributes	

	$scope.selectedOffice = null;
	var dataHolder = [];	
	var initPage = ListConfig.page;
	setColVisibility();
    $scope.showColumnSettings = [false,false,false,false,false,false,false,false,false];

	// -- initial queries
	
	OfficesDao.query(function(offices) {
		$scope.offices = offices;
		
		if($scope.selectedOffice==null) {
			$scope.selectedOffice = _.find($scope.offices, function(office) { return office.id == Authetication.fkBaseOffice; });
		}	
	});

	Authetication.checkLoggedIn().then(function(data) {
        if(data.loggedIn){
        	UserDao.current(function(loadData) {    
            	init(loadData.listViewColPrio);
            });
        } else {
        	init($cookies.get("listViewColPrio"));
        }
    }, function() {
    	init($cookies.get("listViewColPrio"));
    });


	// -- scope methods
    
    $scope.openConfig = function() {
    	var modalInstance = $uibModal.open({
            templateUrl: 'partials/list-locations-config.html',
            controller: 'LunchyControllerListLocationsConfig',
            resolve: {
            	showColumnSettingsConfig: function() {
            		return $scope.showColumnSettingsConfig;
            	}
            }
        });
        modalInstance.result.then(function (result) {            
            init(result);
        }, function () {
            //console.log('Modal dismissed at: ' + new Date());
        });
	}
	
	$scope.rowclick = function(item) {
		$location.path('/view/'+item.id);
	};	
	
	$scope.tableParams = new NgTableParams(ListConfig, {
        total: dataHolder.length,
        getData: function($defer, params) {
        	
        	// HACK: seems like the first parameter of NgTableParams doesn't respect page 
        	if(initPage!=null) {
        		if(initPage!=params.page()){
        			console.log("assert failed."+initPage+"!="+params.page());
        		}
        		params.page(initPage);
        		initPage=null;
        	}
        	
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
		
	$scope.$on('userLoggedIn', function(event) {
		reloadTableData();
	});
	
	$scope.$watch('selectedOffice', function() {
        $scope.tableParams.page(1);
		reloadTableData();
	});
		
	$(window).on("resize.doResize", function (){
        $scope.$apply(function(){
        	setColVisibility();
        });
    });

    $scope.$on("$destroy",function (){
         $(window).off("resize.doResize");
    });
	
}]);