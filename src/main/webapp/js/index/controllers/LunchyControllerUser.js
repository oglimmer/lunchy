'use strict';

/* Controllers */

angular.module('LunchyApp.controllers').
controller('LunchyControllerUser', ['$scope', 'UserDao', 'ngTableParams', '$filter', function($scope, UserDao, ngTableParams, $filter) {

    var dataHolder = null;
    $scope.lineEdit = {};
    $scope.activeRow = null;

    function deactivateEdit() {
        if($scope.activeRow != null) {
            $scope.lineEdit[$scope.activeRow] = false;
            $scope.activeRow = null;
        }
    }

    $scope.rowclick = function(item) {
        deactivateEdit();
        $scope.activeRow = item.id;
        $scope.lineEdit[item.id] = true;
    };

    $scope.changePermission = function(item, newPerm) {
        item.permissions = newPerm;
        UserDao.savePermission({id:item.id}, {permissions:item.permissions});
        deactivateEdit();
    };

    function initTable(data) {
        dataHolder = data;
        $scope.tableParams = new ngTableParams({
            page: 1,
            count: 10,
            sorting: {
                email: 'asc'
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

    UserDao.query(function (data) {
        initTable(data);
    });

}]);