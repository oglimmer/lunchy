describe('LunchyFilters', function() {
	
	// see: https://github.com/pivotal/jasmine/wiki/Matchers

	beforeEach(module('LunchyApp'));

	describe('LunchyFilters', function() {

		it('seperates a string by comma', inject(function($filter) {
			expect($filter('bycomma')).toBeDefined();
			expect($filter('bycomma')()).not.toBeDefined(); 
			expect($filter('bycomma')("")).toEqual([]);
			expect($filter('bycomma')(null)).toEqual([]);
			expect($filter('bycomma')("foo")).toEqual(['foo']);
			expect($filter('bycomma')("foo,bar")).toEqual(['foo', 'bar']);
			expect($filter('bycomma')("foo,bar,1337")).toEqual(['foo', 'bar', '1337']);
		}));

		it('splits array into row/column chunks-1', inject(function($filter) {
			expect($filter('partition')).toBeDefined();
			expect($filter('partition')(null, 2)).not.toBeDefined();
			expect($filter('partition')([], 1)).toEqual([]); 
			expect($filter('partition')(['a'], 1)).toEqual([['a']]); 
			expect($filter('partition')(['a','b'], 1)).toEqual([['a'],['b']]); 
			expect($filter('partition')(['a','b','c'], 1)).toEqual([['a'],['b'],['c']]); 
			expect($filter('partition')(['a','b','c','d'], 1)).toEqual([['a'],['b'],['c'],['d']]); 
			expect($filter('partition')(['a','b','c','d','e'], 1)).toEqual([['a'],['b'],['c'],['d'],['e']]); 
		}));
		it('splits array into row/column chunks-2', inject(function($filter) {
			expect($filter('partition')).toBeDefined();			
			expect($filter('partition')([], 2)).toEqual([]); 
			expect($filter('partition')(['a'], 2)).toEqual([['a']]); 
			expect($filter('partition')(['a','b'], 2)).toEqual([['a','b']]); 
			expect($filter('partition')(['a','b','c'], 2)).toEqual([['a','b'], ['c']]); 
			expect($filter('partition')(['a','b','c', 'd'], 2)).toEqual([['a','b'], ['c', 'd']]); 
			expect($filter('partition')(['a','b','c', 'd', 'e'], 2)).toEqual([['a','b'], ['c', 'd'], ['e']]); 
		}));
		it('splits array into row/column chunks-3', inject(function($filter) {
			expect($filter('partition')).toBeDefined();
			expect($filter('partition')([], 3)).toEqual([]); 
			expect($filter('partition')(['a'], 3)).toEqual([['a']]); 
			expect($filter('partition')(['a','b'], 3)).toEqual([['a','b']]); 
			expect($filter('partition')(['a','b','c'], 3)).toEqual([['a','b','c']]); 
			expect($filter('partition')(['a','b','c', 'd'], 3)).toEqual([['a','b','c'], ['d']]); 
			expect($filter('partition')(['a','b','c', 'd', 'e'], 3)).toEqual([['a','b','c'], ['d', 'e']]); 
		}));

		it('resolves an id against an array of objects', inject(function($filter) {
			expect($filter('resolveNameById')).toBeDefined();
			expect($filter('resolveNameById')(3, [{id:1,name:'foo'},{id:2,name:'bar'}])).toEqual(3); 
			expect($filter('resolveNameById')(1, [{id:1,name:'foo'},{id:2,name:'bar'}])).toEqual('foo'); 			
		}));
		
		it('display n/a for invalid numbers and 0', inject(function($filter) {
			expect($filter('numberToNA')).toBeDefined();
			expect($filter('numberToNA')()).toEqual('n/a'); 
			expect($filter('numberToNA')(null)).toEqual('n/a'); 
			expect($filter('numberToNA')(1/0)).toEqual('n/a'); 
			expect($filter('numberToNA')(0)).toEqual('n/a'); 
			expect($filter('numberToNA')(1)).toEqual(1); 
			expect($filter('numberToNA')(-1)).toEqual(-1); 
		}));
		
	});

});
