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
	        	  UserDao.lookup({id:value}, function(result) {	        		  
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
	            	console.log("up:"+name);
	            }
	            element.html(name);
	        });
	        
	        // install a watch on the scope variable referenced by a dynamic var
	        for(var i = 0 ; i < 2 ; i++) {
	            if(names[i].indexOf("$") === 0) {
	            	var scopeVarName = names[i].substr(1);	            	
	            	scope.$watch(scopeVarName, function(val) {
	            		console.log("down:"+val);
	            		element.html(val);
	            	});
	            }
	        }	        
		}
    };
}).
directive('tagInput', function() {
    return {
        restrict: 'E',
        scope: {
            inputTags: '=taglist',
            autocomplete: '=autocomplete'
        },
        link: function($scope, element, attrs) {
            $scope.defaultWidth = 200;
            $scope.tagText = '';
            $scope.placeholder = attrs.placeholder;
            if ($scope.autocomplete) {
                $scope.autocompleteFocus = function(event, ui) {
                    $(element).find('input').val(ui.item.value);
                    return false;
                };
                $scope.autocompleteSelect = function(event, ui) {
                    $scope.$apply("tagText='" + ui.item.value + "'");
                    $scope.$apply('addTag()');
                    return false;
                };                
                $(element).find('input').autocomplete({
                    minLength: 0,
                    source: function(request, response) {
                        var item;
                        return response((function() {
                            var _i, _len, _ref, _results;
                            _ref = $scope.autocomplete;
                            _results = [];
                            for (_i = 0, _len = _ref.length; _i < _len; _i++) {
                                item = _ref[_i];
                                if (item.toLowerCase().indexOf(request.term.toLowerCase()) !== -1) {
                                    _results.push(item);
                                }
                            }
                            return _results;
                        })());
                    },
                    focus: (function(_this) {
                        return function(event, ui) {
                            return $scope.autocompleteFocus(event, ui);
                        };
                    })(this),
                    select: (function(_this) {
                        return function(event, ui) {
                            return $scope.autocompleteSelect(event, ui);
                        };
                    })(this)
                });
            }
            $scope.tagArray = function() {
                if ($scope.inputTags === undefined) {
                    return [];
                }
                return $scope.inputTags.split(',').filter(function(tag) {
                    return tag !== "";
                });
            };
            $scope.addTag = function() {
                var tagArray;
                if ($scope.tagText.length === 0) {
                    return;
                }
                tagArray = $scope.tagArray();
                tagArray.push($scope.tagText);
                $scope.inputTags = tagArray.join(',');
                return $scope.tagText = "";
            };
            $scope.deleteTag = function(key) {
                var tagArray;
                tagArray = $scope.tagArray();
                if (tagArray.length > 0 && $scope.tagText.length === 0 && key === undefined) {
                    tagArray.pop();
                } else {
                    if (key !== undefined) {
                        tagArray.splice(key, 1);
                    }
                }
                return $scope.inputTags = tagArray.join(',');
            };
            $scope.$watch('tagText', function(newVal, oldVal) {
                var tempEl;
                if (!(newVal === oldVal && newVal === undefined)) {
                    tempEl = $("<span>" + newVal + "</span>").appendTo("body");
                    $scope.inputWidth = tempEl.width() + 5;
                    if ($scope.inputWidth < $scope.defaultWidth) {
                        $scope.inputWidth = $scope.defaultWidth;
                    }
                    return tempEl.remove();
                }
            });
            element.bind("keydown", function(e) {
                var key;
                key = e.which;
                if (key === 9 || key === 13) {
                    e.preventDefault();
                }
                if (key === 8) {
                    return $scope.$apply('deleteTag()');
                }
            });
            return element.bind("keyup", function(e) {
                var key;
                key = e.which;
                if (key === 9 || key === 13 || key === 188) {
                    e.preventDefault();
                    return $scope.$apply('addTag()');
                }
            });
        },
        template: "<div class='tag-input-ctn'><div class='input-tag' data-ng-repeat=\"tag in tagArray()\">{{tag}}<div class='delete-tag' data-ng-click='deleteTag($index)'>&times;</div></div><input type='text' data-ng-style='{width: inputWidth}' data-ng-model='tagText' placeholder='{{placeholder}}'/></div>"
    };
});
