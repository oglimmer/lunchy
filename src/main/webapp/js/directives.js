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
    /*
 * Focus supports two kinds of attributes.
 * A) true - as soon as the element which uses focus becomes active it gets the focus
 * B) variable,value - the variable must be on the scope and will be watched, as soon as it gets the value the element gets the focus
 */
directive('focus', function($timeout) {
    function reeval(val){
        if(/^(true|false|null|undefined|NaN)$/i.test(val)) return eval(val);
        if(parseFloat(val)+''== val) return parseFloat(val);
        return val;
    }
	return {
		link : function(scope, element, attrs) {
            var config = attrs.focus.split(",");
            var triggerValue = true;
            if(config.length>1) {
                triggerValue = config[1];
            }
			scope.$watch(config[0], function(value) {
				if (value == reeval(triggerValue) ) {
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
}).
directive('passwordStrength', ['PasswordStrengthService', function(PasswordStrengthService) {
    return {
      restrict: 'E',
      require: 'ngModel',
      scope: {
    	  password: '=ngModel'
      },
      link: function(scope, element, attrs, ngModel) {    	      	 
    	  var allowNull = false;
    	  if(attrs.allowNull) {
    		  allowNull = true;
    	  }
    	  scope.pss = PasswordStrengthService.create(allowNull); 
    	  scope.$watch("password", function(val) {
    		  var validity = scope.pss.changed(val, allowNull);    		  
    		  ngModel.$setValidity('passwordStrength', validity);
    	  });
      },
      template: '<span ng-class="pss.passStrengthClass">{{ pss.passStrength }}</span>'
    };
}]).
directive('pictureEdit', [ function() {
	return {
		restrict: 'E',
		require: 'ngModel',
		scope: {
			model: '=ngModel',
			show: '=ngShow'
		},
		link: function(scope, element, attrs, ngModel) {
			$(element).find('input').bind("blur", function (event) {
				scope.$apply(function () {
					scope.show = false;
				});
			});
			element.bind("keydown keypress", function (event) {
                if (event.which === 13) {
                    scope.$apply(function () {
                    	scope.show = false;
                    });
                    event.preventDefault();
                }
            });
		},
		template: '<input type="text" ng-show="show" class="form-control" ng-model="model" focus="show,true" style="position:relative;top:-90px;" />'
	};
}]);
