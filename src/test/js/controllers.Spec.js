describe('LunchyControllers', function() {
	var scope, $location, Authetication;

	// see: https://github.com/pivotal/jasmine/wiki/Matchers
	// https://github.com/pivotal/jasmine/wiki/Spies
	
	beforeEach(module('LunchyApp'));
	
	beforeEach(function() {
		Authetication = {
				checkLoggedIn: function() {
				},
				checkIsAdmin: function() {
				},
				loggedIn: false,
				login: function() {
				},
				showLogin: function() {
				},
				logout: function() {
				}
		};
	    module('LunchyApp', function($provide) {
	      $provide.value('Authetication', Authetication);
	    });
	});
	
	describe('LunchyControllerMenu', function() {
	
		beforeEach(inject(function($controller, $rootScope, _$location_) {
			scope = $rootScope.$new();
			scope.authetication = Authetication;
			$location = _$location_;
	
			$controller('LunchyControllerMenu', {
				$scope : scope,
				$location : $location
			});
	
			scope.$digest();
		}));
	
		it('getClass(1)', function() {
			$location.path("/list/1");
			var pathActive = scope.getClass("/list");
			expect(pathActive).toEqual("active");
		});
		it('getClass(2)', function() {
			$location.path("/list");
			var pathActive = scope.getClass("/list");
			expect(pathActive).toEqual("active");
		});
		it('getClass(3)', function() {
			$location.path("/list/1");
			var pathActive = scope.getClass("/finder");
			expect(pathActive).toEqual("");
		});
		it('getClass(4)', function() {
			$location.path("/list");
			var pathActive = scope.getClass("/finder");
			expect(pathActive).toEqual("");
		});
		it('login', function() {
			spyOn(Authetication, 'showLogin');
			scope.login();
			expect(Authetication.showLogin).toHaveBeenCalled();
		});
		it('logout', function() {
			spyOn(Authetication, 'logout');
			scope.logout();
			expect(Authetication.logout).toHaveBeenCalled();
			expect($location.path()).toEqual("/");
		});
		
	});
	
});
