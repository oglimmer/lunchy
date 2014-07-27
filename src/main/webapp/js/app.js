// defines the app
var lunchyApp = angular.module('LunchyApp', ['ngResource', 'ngRoute', 'ui.validate', 'ui.bootstrap']);

lunchyApp.config(function($logProvider){
	$logProvider.debugEnabled(true);
});

lunchyApp.config(['$tooltipProvider', function($tooltipProvider){
    $tooltipProvider.setTriggers({
        'mouseenter': 'mouseleave',
        'click': 'click',
        'focus': 'blur',
        'never': 'mouseleave',
        'show': 'hide'
    });
}]);

lunchyApp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/updates', {
		templateUrl : 'partials/updates.html',
		controller : 'LunchyControllerMain'
	}).when('/add', {
		templateUrl : 'partials/add-location.html',
		controller : 'LunchyControllerMain'
	}).otherwise({
		redirectTo : '/updates'
	});
} ]);

lunchyApp.factory('ILogin', ['$resource', function($resource) {
	return $resource('/lunchy/rest/login', null, {
		'login': {
			method: 'POST'
		},
		'logout': {
			method: 'DELETE'
		},
		'check': {
			method: 'GET'
		}
	});
}]);
lunchyApp.factory('IUser', ['$resource', function($resource) {
	return $resource('/lunchy/rest/users/:email', null, {
		'create': {
			method: 'POST'
		},
		'lookup': {
			method: 'GET'
		}
	});
}]);
lunchyApp.factory('IUpdates', ['$resource', function($resource) {
	return $resource('/lunchy/rest/updates');
}]);

lunchyApp.directive('unique', ['IUser', function (IUser){ 
   return {
      require: 'ngModel',
      link: function(scope, elem, attr, ngModel) {
          var fieldName = attr.unique;
          if(fieldName!="email") {
        	  console.log("unique needs to be email"); return;
          }
          
          function checkAgainstServer(value) {
        	  if(typeof(value)!='undefined' && value != ""){
	        	  IUser.lookup({email:value}, function(result) {
	        		  ngModel.$setValidity('unique', !result.success);
	        	  });
        	  }
        	  return true;
          }

          //For DOM -> model validation
          ngModel.$parsers.unshift(function(value) {
             var valid = checkAgainstServer(value);
             ngModel.$setValidity('unique', valid);
             return valid ? value : undefined;
          });

          //For model -> DOM validation
          ngModel.$formatters.unshift(function(value) {
             ngModel.$setValidity('unique', checkAgainstServer(value));
             return value;
          });
      }
   };
}]);


// Common directive for Focus
lunchyApp.directive('focus', function($timeout) {
	return {
		scope : {
			trigger : '@focus'
		},
		link : function(scope, element) {
			scope.$watch('trigger', function(value) {
				if (value === "true") {
					$timeout(function() {
						element[0].focus();
					});
				}
			});
		}
	};
}); 

lunchyApp.filter('partition', function() {
  var cache = {};
  var filter = function(arr, size) {
    if (!arr) { return; }
    var newArr = [];
    for (var i=0; i<arr.length; i+=size) {
      newArr.push(arr.slice(i, i+size));
    }
    var arrString = JSON.stringify(arr);
    var fromCache = cache[arrString+size];
    if (JSON.stringify(fromCache) === JSON.stringify(newArr)) {
      return fromCache;
    }
    cache[arrString+size] = newArr;
    return newArr;
  };
  return filter;
});

// add a controller
lunchyApp.controller('LunchyControllerMain', ['$scope', 'ILogin', 'IUser', 'IUpdates', '$modal', '$location', LunchyControllerMain ]);
lunchyApp.controller('LunchyControllerLogin', ['$scope', 'ILogin', 'IUser', '$timeout', LunchyControllerLogin ]);
lunchyApp.controller('LunchyControllerRegister', ['$scope', '$modalInstance', 'ILogin', 'IUser', LunchyControllerRegister ]);

function LunchyControllerMain($scope, ILogin, IUser, IUpdates, $modal, $location) {
	
	$scope.userLoggedIn = false;
	$scope.showRegisterFrame = false;
	$scope.latestUpdates = IUpdates.query();
		
	$scope.logout = function() {
		ILogin.logout();	
		$scope.userLoggedIn = false;
	};
	
	$scope.showRegister = function() {
		var modalInstance = $modal.open({
		  templateUrl: 'partials/register.html',
		  controller: LunchyControllerRegister		  
		});
		modalInstance.result.then(function (result) {
			if(result) {
				$scope.userLoggedIn = true;
			}
		}, function () {
			console.log('Modal dismissed at: ' + new Date());
		});
	}
	
	$scope.getClass = function(path) {
	    if ($location.path().substr(0, path.length) == path) {
	      return "active"
	    } else {
	      return ""
	    }
	}
	
	ILogin.check(function(data) {
		$scope.userLoggedIn = data.success;
	});
};

function LunchyControllerLogin($scope, ILogin, IUser, $timeout) {
	
	$scope.submitLogin = function() {				
		ILogin.login({email:$scope.email, password:$scope.password}, function(data) {
			if(data.success) {
				$scope.$parent.userLoggedIn = true;
			} else {
				$scope.errorMsg = data.errorMsg;
				$timeout(function() {$('#LoginError').trigger('show');}, 1);
				$timeout(function() {$('#LoginError').trigger('hide');}, 3000);				
			}
		});		
	}
	
};


function LunchyControllerRegister($scope, $modalInstance, ILogin, IUser) {
	
	$scope.newUser = {};
	$scope.alerts = [];
	
	$scope.cancelRegister = function() {
		$modalInstance.dismiss('cancel');
	}
	
	$scope.closeAlert = function(index) {
		$scope.alerts.splice(index, 1);
	};
	
	$scope.submitRegister = function() {
		console.log($scope.newUser);
		IUser.create({email:$scope.newUser.email},{password:$scope.newUser.password, displayname:$scope.newUser.nickname}, function(data) {
			if(data.success) {
				$modalInstance.close(true);				
			} else {
				$scope.alerts.push({type:'danger',msg:data.errorMsg});
			}
		});		
	}
	
};