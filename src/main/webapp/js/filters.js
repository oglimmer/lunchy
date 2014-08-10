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
filter('newlines', function () {
    return function(text) {
    	if(typeof(text)!=='undefined') {
    		return text.split(/\n/g);
    	}
    }
}).
filter('bycomma', function () {
    return function(text) {
    	if(typeof(text)!=='undefined') {
    		return text.split(/,/g);
    	}
    }
});
