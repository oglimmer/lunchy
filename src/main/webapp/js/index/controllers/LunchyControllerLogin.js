'use strict';

/* Controllers */

angular.module('LunchyApp.controllers').
controller('LunchyControllerLogin', ['$scope', '$uibModalInstance', 'LoginDao', '$timeout', 'Authetication', 'StorageService', 'AlertPaneService', 
                                     function ($scope, $uibModalInstance, LoginDao, $timeout, Authetication, StorageService, AlertPaneService) {
	AlertPaneService.add($scope);
	
    $scope.initShowMode = 0;
    $scope.$uibModalInstance = $uibModalInstance;
    $scope.login = {};

    $scope.passwordForgotten = function() {
        $scope.alerts = [];
        $scope.initShowMode = 1;
    };

    $scope.register = function() {
        $scope.alerts = [];
        $scope.initShowMode = 2;
    };

    $scope.cancel = function() {
    	$uibModalInstance.dismiss('cancel');
    };

    $scope.submitLogin = function() {
        $scope.alerts = [];
        LoginDao.login({email: $scope.login.email, password: $scope.login.password, keepLoggedIn: $scope.login.keepMeLoggedIn}, function(data) {
            if(data.success) {
                Authetication.logInUser(data);
                $scope.password = "";
                if(data.longTimeToken != null && data.longTimeToken != "") {
                    StorageService.save('longTimeToken', data.longTimeToken);
                }
                $uibModalInstance.close('ok');
            } else {
                $scope.alerts.push({type:'danger',msg:data.errorMsg});
            }
        });
    };

    // Firefox password manager sets user/pass and angularJS is not informed.
    $scope.init = function() {
        $timeout(function() {
            $('input[ng-model]').trigger('input');
        }, 100);
    };
	
}]);