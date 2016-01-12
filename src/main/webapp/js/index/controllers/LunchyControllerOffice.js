'use strict';

/* Controllers */

angular.module('LunchyApp.controllers').
controller('LunchyControllerOffice', ['$scope', 'ngTableParams', '$filter', 'OfficesDao', '$location', function($scope, ngTableParams, $filter, OfficesDao, $location) {

    var dataHolder = null;

    $scope.rowclick = function(item) {
        $location.path("office-edit/"+item.id);
    };
    
    $scope.newOffice = function() {
        $location.path("office-edit/");
    };

    function initTable(data) {
        dataHolder = data;
        $scope.tableParams = new ngTableParams({
            page: 1,
            count: 10,
            sorting: {
                name: 'asc'
            }
        }, {
            total: dataHolder.length,
            getData: function($defer, params) {
                var filterData = $filter('filter')(dataHolder, params.filter());
                var orderedData = $filter('orderBy')(filterData, params.orderBy());
                var pagedData = orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count());
                params.total(filterData.length);
                return $defer.resolve(pagedData);
            }
        });
    }

    OfficesDao.query(function (data) {
        initTable(data);
    });
}]);