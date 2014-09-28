'use strict';

/* Filters */

angular.module('LunchyApp.filters', []).
filter('partition', function() {
	var cache = {};
	var filter = function(arr, size) {
		if (!arr) {
			return;
		}
		var newArr = [];
		for (var i = 0; i < arr.length; i += size) {
			newArr.push(arr.slice(i, i + size));
		}
		var arrString = JSON.stringify(arr);
		var fromCache = cache[arrString + size];
		if (JSON.stringify(fromCache) === JSON.stringify(newArr)) {
			return fromCache;
		}
		cache[arrString + size] = newArr;
		return newArr;
	};
	return filter;
}).
filter('bycomma', function () {
    return function(text) {
    	if(typeof(text)!=='undefined') {
    		if(text===null||text.trim()==='') {
    			return [];
    		} else {    		
    			return text.split(/,/g);
    		}
    	}
    }
}).
filter('resolveNameById', function () {
    return function(id, objArray) {
    	var resolvedName = id;
    	angular.forEach(objArray, function(obj) {
    		if(obj.id == id) {
    			resolvedName = obj.name;
    		}
    	})
    	return resolvedName;
    }
}).
filter('numberToNA', function () {
	return function(number) {
		if(typeof(number)==='undefined' || number == Number.POSITIVE_INFINITY || number == Number.NEGATIVE_INFINITY || number == null || number == "0") {
			return "n/a";
		}
		return number;
	}
});
