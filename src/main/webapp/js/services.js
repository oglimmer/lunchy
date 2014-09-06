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
factory('Authetication', ['$modal', '$q', 'LoginDao', '$rootScope', 'StorageService', 'OfficesDao', 'CommunityService', '$cookies',
    function($modal, $q, LoginDao, $rootScope, StorageService, OfficesDao, CommunityService, $cookies) {

        function getLongTimeToken() {
            var longTimeToken = StorageService.get('longTimeToken');
            if (typeof(longTimeToken) === 'undefined' || longTimeToken == null) {
                longTimeToken = $cookies.lunchylogintoken;
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

            checkLoggedIn: function() {
                var thiz = this;
                if(thiz.loggedIn) {
                    return $q.when(thiz);
                } else {
                    this._loadDefaultOffice();
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
                var modalInstance = $modal.open({
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
}]);
