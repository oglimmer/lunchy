'use strict';

/* Controllers */

angular.module('LunchyPortalApp.controllers', []).
controller('LunchyControllerMain', ['$scope',
        function($scope) {

}]).
controller('LunchyControllerPortalGreetings', ['$scope', function($scope) {

}]).
controller('LunchyControllerPortalCreateCommunity', ['$scope', '$location', 'CommunityDao', '$window', function($scope, $location, CommunityDao, $window) {

        $scope.data = {};
        $scope.alerts = [];

        $scope.closeAlert = function(index) {
            $scope.alerts.splice(index, 1);
        };

        function getContextPath() {
            var url = $location.absUrl();
            var host = $location.host();
            if(url.indexOf(host+":"+$location.port())!==-1) {
                host = host+":"+$location.port();
            }
            // => e.g. http://lunchylunch.com:8080/lunchy/portal.jsp#/create-community
            var contextPath = url.substr(url.indexOf(host)+host.length+1);
            // => e.g. lunchy/portal.jsp#/create-community
            if(contextPath.indexOf("#") > contextPath.indexOf("/")) {
                // => lunchy/portal.jsp#/create-community
                contextPath = contextPath.substr(0, contextPath.indexOf("/"));
            } else {
                // => portal.jsp#/create-community
                contextPath = "";
            }
            return contextPath;
        }

        function getDomain() {
            var hostname;
            if($location.host().match(/\./g).length >= 2){
                hostname = $location.host().match(/\w*$|\w*\.\w*$/i)[0];
            } else {
                hostname = $location.host();
            }
            hostname += $location.port() != 80 ? ":"+$location.port():"";
            return hostname;
        }

        $scope.saveCommunity = function() {
            CommunityDao.save($scope.data, function() {
                var targetUrl = "http://"+$scope.data.domain+"."+getDomain()+"/"+getContextPath();
                $window.location.href= targetUrl;
            }, function(error) {
                $scope.alerts= [{type:'danger', msg: 'Error while saving user: ' + error.statusText}];
            });
        }

        $scope.cancel = function() {
            $location.path("/");
        }

}]);
