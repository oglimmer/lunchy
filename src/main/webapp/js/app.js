'use strict';


// Declare app level module which depends on filters, and services
angular.module('LunchyApp', [
  'ngResource', 'ui.router', 'ui.validate', 'ui.bootstrap', 'google-maps', 'ngTable', 'flow', 'ngTouch',
  'LunchyApp.filters',
  'LunchyApp.services',
  'LunchyApp.directives',
  'LunchyApp.controllers'
]).
config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
	$stateProvider.
	state('updates', {
	      url: '/updates',
	      templateUrl: 'partials/updates.html',
	      controller : 'LunchyControllerMain'
	    }).
		state('view', {
	      url: '/view/:locationId',
	      templateUrl: 'partials/view-location.html',
	      controller : 'LunchyControllerView'
		}).
		state('browse', {
	      url: '/browse',
	      templateUrl: 'partials/browse-locations.html',
	      controller : 'LunchyControllerBrowseLocations'
		}).
		state('browse-by-office', {
			url: '/browse/:officeId',
			templateUrl: 'partials/browse-locations.html',
			controller : 'LunchyControllerBrowseLocations'
		}).
		state('list', {
	      url: '/list',
	      templateUrl: 'partials/list-locations.html',
	      controller : 'LunchyControllerListLocations'
		}).
		state('passwordReset', {
	      url: '/passwordReset',
	      templateUrl: 'partials/passwordReset.html',
	      controller : 'LunchyControllerPasswordReset'
		}).
		state('user', {
	      url: '/user',
	      templateUrl: 'partials/users.html',
	      controller : 'LunchyControllerUser'
		}).
	    state('add', {
	      url: '/add',
	      templateUrl: 'partials/add-location.html',
	      controller : 'LunchyControllerAdd',
	      resolve: {
	    	  auth: ["Authetication", function(Authetication) {			    	
			      return Authetication.checkLoggedIn();
			  }]
	      }
	    }).
	    state('settings', {
	      url: '/settings',
	      templateUrl: 'partials/settings.html',
	      controller : 'LunchyControllerSettings',
	      resolve: {
	    	  auth: ["Authetication", function(Authetication) {			    	
			      return Authetication.checkLoggedIn();
			  }]
	      }
	    });
    $urlRouterProvider.otherwise('/updates');
    
}]).
config(function($logProvider){
	$logProvider.debugEnabled(true);
}).
config(['$tooltipProvider', function($tooltipProvider){
    $tooltipProvider.setTriggers({
        'mouseenter': 'mouseleave',
        'click': 'click',
        'focus': 'blur',
        'never': 'mouseleave',
        'show': 'hide'
    });
}]).
config(['flowFactoryProvider', function (flowFactoryProvider) {
  flowFactoryProvider.defaults = {
    target: './upload',
    method:'octet',
    singleFile:true    
  };
  flowFactoryProvider.on('catchAll', function (event) {
    console.log('catchAll', arguments);
  });
  // Can be used with different implementations of Flow.js
  // flowFactoryProvider.factory = fustyFlowFactory;
}]).
factory('LoginDao', ['$resource', '$http', function($resource, $http) {	
	return $resource('rest/login', null, {
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
}]).
factory('UserDao', ['$resource', function($resource) {
	return $resource('rest/users/:id', {id: '@id'}, {
		'lookup': {
			method: 'OPTIONS'
		},
		'current': {
			method: 'GET',
			url: 'rest/users/current'
		},
		'sendPasswordLink': {
			method: 'POST',
			url: 'rest/users/:id/sendPasswordLink'			
		},
		'resetPassword': {
			method: 'POST',
			url: 'rest/users/:id/resetPassword'			
		},
		'savePermission': {
			method: 'POST',
			url: 'rest/users/:id/savePermission'
		}
	});
}]).
factory('UpdatesDao', ['$resource', function($resource) {
	return $resource('rest/updates');
}]).
factory('PicturesDao', ['$resource', function($resource) {
	return $resource('rest/pictures/:id', {id: '@id'});
}]).
factory('OfficesDao', ['$resource', function($resource) {
	return $resource('rest/offices/:id', {id: '@id'}, {
		'locations': {
			method: 'GET',
			url: 'rest/offices/:id/locations',
			isArray: true
		},
		'defaultOffice': {
			method: 'GET',
			url: 'rest/offices/defaultOffice'
		}
	});
}]).
factory('LocationsDao', ['$resource', function($resource) {
	return $resource('rest/locations/:id', {id: '@id'}, {
		'queryReviews': {
			method: 'GET',
			url: 'rest/locations/:id/reviews',
			isArray: true
		},
		'queryPictures': {
			method: 'GET',
			url: 'rest/locations/:id/pictures',
			isArray: true
		},
		'locationStatusForCurrentUser': {
			method: 'GET',
			url: 'rest/locations/:id/locationStatusForCurrentUser'
		}
	});
}]).
factory('ReviewDao', ['$resource', function($resource) {
	return $resource('rest/reviews/:id', {id: '@id'});
}]).
factory('TagDao', ['$resource', function($resource) {
	return $resource('rest/tags');
}]).
factory('Authetication', ['$modal', '$q', 'LoginDao', '$rootScope', 'StorageService', 'OfficesDao', 'CommunityService',
					function($modal, $q, LoginDao, $rootScope, StorageService, OfficesDao, CommunityService) {
	
	return {
		loggedIn: false,
		fkBaseOffice: -1,
		
		logInUser: function(loginResponse) {
			if(loginResponse.success) {
				this.loggedIn = true;
				this.fkBaseOffice = loginResponse.fkOffice;
				$rootScope.$broadcast('userLoggedIn', []);
				console.log(this);
			} else {
				console.error("logInUser was called but response didnt succeed.");
			}
		},
		
		checkLoggedIn: function() {
			var thiz = this;
			if(thiz.loggedIn) {
				return $q.when(thiz);
			} else {				
				// user is not logged in, get the default office
				if(thiz.fkBaseOffice === -1) {					
					OfficesDao.defaultOffice(function(result) {					
						// only if still no valid base-office
						if(thiz.fkBaseOffice === -1) {
							thiz.fkBaseOffice = result.errorMsg;
							console.log("Set base office to "+thiz.fkBaseOffice);
						}
					});				
				}

				return LoginDao.check({longTimeToken:StorageService.get('longTimeToken')}).$promise
					.then(function(successResp) {						
						CommunityService.setCompanyName(successResp.companyName);
						if(successResp.success) {
							thiz.logInUser(successResp);							
							return $q.when(thiz);
						} else {
							return $q.reject({ authenticated: false });
						}					
					}, function(errorResp) {
						return $q.reject({ authenticated: false });
					});		
			}
		},
		showRegister: function() {
			var thiz = this;
			var modalInstance = $modal.open({
			  templateUrl: 'partials/register.html',
			  controller: 'LunchyControllerRegister'		  
			});
			modalInstance.result.then(function (result) {
				console.log(result);
				if(result.success) {
					thiz.logInUser(result);
				}
			}, function () {
				console.log('Modal dismissed at: ' + new Date());
			});
		}
	};
}]).
factory('ListConfig', function() {
	return {
        page: 1,
        count: 10,
        sorting: {
        	lastRating: 'desc'
        },
        filter: {        	
        },
        copyParams: function(params) {
        	this.page = params.page();
        	this.count = params.count();
        	this.filter = params.filter();
        	this.sorting = params.sorting();
        }
    };
	
}).
factory('Comparator', function() {
	function comparator(obj, text) {	        	    
	    if (obj && text && typeof obj === 'object' && typeof text === 'object') {
	        for (var objKey in obj) {
	            if (objKey.charAt(0) !== '$' && hasOwnProperty.call(obj, objKey) && comparator(obj[objKey], text[objKey])) {
	                return true;
	            }
	        }
	        return false;
	    }	        	    
	    if (text.charAt(0) === '@') {	        	        
	        return parseFloat(obj) <= parseFloat(text.substr(1));
	    }
	    if (text.charAt(0) === '#') {	        	        
	    	return parseFloat(obj) >= parseFloat(text.substr(1));
	    }
	    text = ('' + text).toLowerCase();
	    return ('' + obj).toLowerCase().indexOf(text) > -1;
	};
	return comparator;
}).
factory('StorageService', function ($window) {
	var localStorage = $window['localStorage'];
    return {
        
        get: function (key) {
        	try {
        		return JSON.parse(localStorage.getItem(key));
        	} catch(e) {
        		return null;
        	}
        },

        save: function (key, data) {
           localStorage.setItem(key, JSON.stringify(data));
        },

        remove: function (key) {
            localStorage.removeItem(key);
        },
        
        clearAll : function () {
            localStorage.clear();
        }
    };
}).
factory('TagService', ['TagDao', function (TagDao) {
    return {
        get: function() {
        	 return TagDao.query().$promise;
        }
    };
}]).
factory('CommunityService', ['$rootScope', function ($rootScope) {
	return {
		setCompanyName : function(val) {
			$rootScope.companyName = val;
		}
	};
}]).
run(['$rootScope', '$location', 'Authetication', 'LoginDao', '$timeout', function($rootScope, $location, Authetication, LoginDao, $timeout) {	
	$rootScope.$on('$stateChangeError', function(event, toState, toParams, fromState, fromParams, eventObj) {
		if (eventObj.authenticated === false) {
			$location.path("/");
			Authetication.showRegister();
		}
	});

	Authetication.checkLoggedIn();

	
	function keepAlive() {
		if(Authetication.loggedIn){
			LoginDao.check();
		}
		$timeout(keepAlive, 1000*60*5);
	}
	keepAlive();
	
}]);
