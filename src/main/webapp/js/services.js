'use strict';

/* Services */

angular.module('LunchyApp.services', []).
factory('CommunityDao', ['$resource', function($resource) {
    return $resource('rest/communities/:id', {id: '@id'}, {
        'lookup': {
            method: 'OPTIONS'
        }
    });
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
        },
        'saveListViewColConfig': {
            method: 'POST',
            url: 'rest/users/saveListViewColConfig'
        }
    });
}]).
factory('UpdatesDao', ['$resource', function($resource) {
    return $resource('rest/updates', null, {
        'query': {
            isArray: false
        }
    });
}]).
factory('PicturesDao', ['$resource', function($resource) {
    return $resource('rest/pictures/:id', {id: '@id'}, {
        'vote': {
            method: 'POST',
            url: 'rest/pictures/:id/vote'
        }
    });
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
        },
        'updatePosition': {
            method: 'POST',
            url: 'rest/locations/:id/updatePosition'
        }
    });
}]).
factory('ReviewDao', ['$resource', function($resource) {
    return $resource('rest/reviews/:id', {id: '@id'});
}]).
factory('TagDao', ['$resource', function($resource) {
    return $resource('rest/tags');
}]).
factory('FinderDao', ['$resource', function($resource) {
	return $resource('rest/finder', {}, {
		'queryRandom': {
			method: 'GET',
            url: 'rest/finder/random',
            isArray: true
		}
	});
}]).
factory('Authetication', ['$uibModal', '$q', 'LoginDao', '$rootScope', 'StorageService', 'OfficesDao', 'CommunityService', '$cookies',
    function($uibModal, $q, LoginDao, $rootScope, StorageService, OfficesDao, CommunityService, $cookies) {

        function getLongTimeToken() {
            var longTimeToken = StorageService.get('longTimeToken');
            if (typeof(longTimeToken) === 'undefined' || longTimeToken == null) {
                longTimeToken = $cookies.get("lunchylogintoken");
            }
            return longTimeToken;
        }


        return {
            loggedIn: false,
            fkBaseOffice: -1,
            permissions:-1,

            logInUser: function(loginResponse) {
                if(loginResponse.success) {
                    this.loggedIn = true;
                    this.fkBaseOffice = loginResponse.fkOffice;
                    this.permissions = loginResponse.permissions;
                    $rootScope.$broadcast('userLoggedIn', []);
                } else {
                    console.error("logInUser was called but response didnt succeed.");
                }
            },

            _loadDefaultOffice: function () {
                var thiz = this;
                // user is not logged in, get the default office
                if (thiz.fkBaseOffice === -1) {
                    OfficesDao.defaultOffice(function (result) {
                        // only if still no valid base-office
                        if (thiz.fkBaseOffice === -1) {
                            thiz.fkBaseOffice = result.defaultOffice;
                        }
                    });
                }
            },

            login: function() {
                var thiz = this;
                var longTimeToken = getLongTimeToken();
                return LoginDao.check({longTimeToken:longTimeToken}).$promise
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
            },
            
            logout: function() {
            	this.loggedIn = false;
            	this.permissions = 0;
            	LoginDao.logout();
            },

            checkLoggedIn: function() {
                if(this.loggedIn) {
                    return $q.when(this);
                } else {
                    this._loadDefaultOffice();
                    return this.login();
                }
            },

            checkIsAdmin: function() {
                var promise = this.checkLoggedIn();
                promise = promise.then(function(authetication) {
                    if(authetication.permissions==2) {
                        return $q.when(authetication);
                    } else {
                        return $q.reject({ authenticated: false });
                    }
                });
                return promise;
            },

            showLogin: function() {
                var thiz = this;
                var modalInstance = $uibModal.open({
                    templateUrl: 'partials/login.html',
                    controller: 'LunchyControllerLogin'
                });
                modalInstance.result.then(function (result) {
                    if(result.success) {
                        thiz.logInUser(result);
                    }
                }, function () {
                    //console.log('Modal dismissed at: ' + new Date());
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
        get: function(selectedOffice) {
            return TagDao.query(selectedOffice).$promise;
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
factory('PasswordStrengthService', [function () {

	function createStrengthText(number) {
		switch(number) {
		case -1:
			return "n/a";
		case 0:
			return "Too simple";
		case 1:
			return "Simple";
		case 2:
			return "Good";
		case 3:
			return "Strong";
		case 4:
			return "Ultra strong";
		}
	}
	function createStrengthButtonClass(number) {
		switch(number) {
		case 0:
			return { 'label':true, 'label-danger': true};
		case 1:
		case 2:
		case 3:
		case 4:
			return { 'label':true, 'label-success': true};
		}		
	}
	
	var PasswordStrengthService = function(emptyAllowed) {
		
		this.passStrength = null;
		this.passStrengthClass = null;
		this.emptyAllowed = emptyAllowed ? true : false;
		
		this.reset= function() {
			var str, strCla;
			if(this.emptyAllowed) {
				str = -1;
				strCla = {};
			} else {
				str = 0;
				strCla = {'label':true};				
			}
			this.passStrength = createStrengthText(str);;
			this.passStrengthClass = strCla;			
		};
		
		this.changed= function(val) {			
			if(typeof(val)==='undefined' || val == ""){
				if(this.emptyAllowed) {
					this.reset(this.emptyAllowed);
					return this.passStrength != 0;
				} else {
					val="";
				}
			}
			// zxcvbn is loaded async, therefore don't expect it here
			var result = {score:0};
			if(typeof(zxcvbn)!=='undefined') {
				result = zxcvbn(val);
			}
			this.passStrength = createStrengthText(result.score);
			this.passStrengthClass = createStrengthButtonClass(result.score);
			
			return result.score != 0;
		};
		
		this.reset(this.emptyAllowed);
	};
	
	return {
		create: function(emptyAllowed) {
			return new PasswordStrengthService(emptyAllowed);
		}		
	};
}]).
factory('AlertPaneService', ['$rootScope', function ($rootScope) {
	return {
		add: function($scope) {
			$scope.alerts = [];
			$scope.closeAlert = function(index) {
				$scope.alerts.splice(index, 1);
			};
		}
	};
}]).
factory('FinderSearchParameter', [ function () {
	return {
		inclTags: "",
		exclTags: "",
		partner: "",
		selectedOffice: null
	};
}]);
