'use strict';

/* Directives */

angular.module('LunchyApp.directives', []).
directive('unique', ['UserDao', function (UserDao){ 
   return {
      require: 'ngModel',
      link: function(scope, elem, attr, ngModel) {
          var fieldName = attr.unique;
          if(fieldName!="email") {
        	  console.log("unique needs to be email"); return;
          }
          
          function checkAgainstServer(value) {
        	  if(typeof(value)!='undefined' && value != ""){
	        	  UserDao.lookup({email:value}, function(result) {
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
}]).
directive('focus', function($timeout) {
	return {
		scope : {
			trigger : '@focus'
		},
		link : function(scope, element) {
			scope.$watch('trigger', function(value) {
				if (value === "true") {
					$timeout(function() {
						element[0].focus();
					});
				}
			});
		}
	};
}); 