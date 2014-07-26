// defines the app
var lunchyApp = angular.module('LunchyApp', ['ngResource', 'ui.validate']);

lunchyApp.config(function($logProvider){
	$logProvider.debugEnabled(true);
});

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
          var fieldName = attr.blacklist;
          
          function checkAgainstServer(value) {
        	  if(typeof(value)!='undefined' && value != ""){
	        	  IUser.lookup({email:value}, function(result) {
	        		  console.log(result.success);
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

//add a controller
lunchyApp.controller('LunchyControllerMain', ['$scope', 'ILogin', 'IUser', 'IUpdates', LunchyControllerMain ]);
lunchyApp.controller('LunchyControllerLogin', ['$scope', 'ILogin', 'IUser', LunchyControllerLogin ]);
lunchyApp.controller('LunchyControllerRegister', ['$scope', 'ILogin', 'IUser', LunchyControllerRegister ]);

function LunchyControllerMain($scope, ILogin, IUser, IUpdates) {
	
	$scope.userLoggedIn = false;
	$scope.showRegisterFrame = false;
	$scope.latestUpdates = IUpdates.query();
	
	console.log($scope.latestUpdates);
	
	$scope.logout = function() {
		ILogin.logout();	
		$scope.userLoggedIn = false;
	};
	
	$scope.showRegister = function() {
		$scope.showRegisterFrame = true;
	}
	
	ILogin.check(function(data) {
		$scope.userLoggedIn = data.success;
	});
};

function LunchyControllerLogin($scope, ILogin, IUser) {
	
	$scope.submitLogin = function() {
		ILogin.login({email:$scope.email, password:$scope.password}, function(data) {
			if(data.success) {
				$scope.$parent.userLoggedIn = true;
			} else {
				alert(data.errorMsg);
			}
		});		
	}
	
};

function LunchyControllerRegister($scope, ILogin, IUser) {

	function reset() {
		$scope.password = "";
		$scope.confirm_password = "";
		$scope.nickname = "";
		$scope.email = "";
	}
	
	$scope.cancelRegister = function() {
		reset();
		$scope.$parent.showRegisterFrame = false;
	}
	
	$scope.submitRegister = function() {
		IUser.create({email:$scope.email},{password:$scope.password, displayname:$scope.nickname}, function(data) {
			if(data.success) {
				$scope.$parent.userLoggedIn = true;
				reset();
				$scope.$parent.showRegisterFrame = false;
			} else {
				alert(data.errorMsg);
			}
		});		
	}
	
};