'use strict';

/* Directives */

angular.module('LunchyApp.directives', []).
directive('unique', ['UserDao','CommunityDao', function (UserDao, CommunityDao){
   return {
      require: 'ngModel',
      link: function(scope, elem, attr, ngModel) {
          var fieldName = attr.unique;
          if(fieldName!="email" && fieldName!="domain") {
        	  console.log("unique needs to be email/domain"); return;
          }
          
          function checkAgainstServer(value) {
        	  if(typeof(value)!='undefined' && value != ""){
                  var dao;
                  switch (fieldName) {
                      case "email":
                          dao = UserDao;
                          break;
                      case "domain":
                          dao = CommunityDao;
                          break;
                  }
                  dao.lookup({id:value}, function(result) {
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
}).
directive('lyDisabled', function() {
	return {
		link: function(scope, element, attrs) {				
	        scope.$watch(attrs.lyDisabled, function(val) {
	            if (val) {
	                element.attr('disabled', 'disabled');
	                element.removeAttr('placeholder');
	            } else {
	                element.removeAttr('disabled');
	                if(typeof(element.attr('ly-placeholder'))!=='undefined') {
	                	element.attr('placeholder', element.attr('ly-placeholder'));
	                }
	            }
	        });
		}
    };
}).
/*
 * Static:
 * Sets the name of the button according to the value of this attribute. Where the attribute needs to have 2 values separated by ;
 * Example: lyDynamicname="Edit;Add" => If the value of the button is 0 the button has the label "Edit". If value is 1 the button is labeled "Add".
 * 
 * Dynamic:
 * Sets the name of the button according to a scope variable where the name of the variable is defined as a value of this attribute. Where the attribute needs to have 2 values separated by ;
 * Example: lyDynamicname="$buttonLbl;Add" => If the value of the button is 0 the button has the label $scope.buttonLbl . If value is 1 the button is labeled "Add".
 * 
 */
directive('lyDynamicname', function() {
	return {
		require: 'ngModel',
		link: function(scope, element, attrs, ngModel) {
			var names = attrs.lyDynamicname.split(";");

			// install a watch on the scope variable referenced by the model
	        scope.$watch(attrs.ngModel, function(val) {
	        	var name;
	            if (val) {
	            	name = names[1];
	            } else {
	            	name = names[0];
	            }
	            if(name.indexOf("$") === 0) {
	            	name = scope[name.substr(1)];
	            	//console.log("up:"+name);
	            }
	            element.html(name);
	        });
	        
	        // install a watch on the scope variable referenced by a dynamic var
	        for(var i = 0 ; i < 2 ; i++) {
	            if(names[i].indexOf("$") === 0) {
	            	var scopeVarName = names[i].substr(1);	            	
	            	scope.$watch(scopeVarName, function(val) {
	            		//console.log("down:"+val);
	            		element.html(val);
	            	});
	            }
	        }	        
		}
    };
}).
directive('lyEnter', function() {
    return {
        link: function (scope, element, attrs) {
            element.bind("keydown keypress", function (event) {
                if (event.which === 13) {
                    scope.$apply(function () {
                        scope.$eval(attrs.lyEnter, {'event': event});
                    });
                    event.preventDefault();
                }
            });
        }
    };
});
