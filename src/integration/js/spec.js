var Chance = require("./chance.js");

describe('Lunchy', function() {
	
	var chance = new Chance();
	
	var userEmail = chance.email();
	var displayName = chance.name();
	
	it('Create user', function() {
		
		browser.get('http://localhost:8080/#/updates');
		
		var login = element(by.linkText('Login/Register'));
		login.click();

		var register = element(by.buttonText('Register account'));
		register.click();

		var email = element(by.model('newUser.email'))
		email.sendKeys(userEmail);
		var email = element(by.model('newUser.displayname'))
		email.sendKeys(displayName);
		var pass = element(by.model('newUser.password'))
		pass.sendKeys('fooba');
		var pass = element(by.model('newUser.confirm_password'))
		pass.sendKeys('fooba');
		element(by.cssContainingText('option', 'Office No.1')).click();
		
		element(by.buttonText('Register')).click();
		
		element(by.linkText('Log out')).click();
		
		browser.get('http://localhost:8080/#/updates');
		
		element.all(by.repeater('update in rows')).filter(function(elem, index) {
			return elem.getText().then(function(text) {
				return text === displayName+' joined';
			});
		}).then(function(filteredList) {
			expect(filteredList.length).toBe(1);	
		});
	});
	
	it('Check new user', function() {
		
		
	});	
	
	function login() {
		browser.get('http://localhost:8080/#/updates');

		var login = element(by.linkText('Login/Register'));
		login.click();

		var email = element(by.model('login.email'))
		email.sendKeys(userEmail);
		var pass = element(by.model('login.password'))
		pass.sendKeys('fooba');

		element(by.buttonText('Log me in')).click();
		protractor.getInstance().sleep(250);
	}
	
	it('Login / Logout', function() {
				
		login();
				
		element(by.linkText('Log out')).click();

	});
	
	it('Create location', function() {
		
		var locName = chance.word({syllables: 5});
		
		login();
		
		element(by.linkText('Add')).click();		
		
		element(by.model('data.officialName')).sendKeys(locName)
		element(by.model('data.address')).sendKeys("Alexanderplatz 1")
		element(by.model('data.city')).sendKeys("Berlin")
				
		element(by.buttonText('Save')).click();
		
		browser.get('http://localhost:8080/#/updates');
		
		element.all(by.repeater('update in rows').column('update.text')).filter(function(elem){
			return elem.getText().then(function(text) {
				return text === 'New location '+locName+' in Berlin';
			});
		}).then(function(filteredList) {
			expect(filteredList.length).toBe(1);	
		});
	});
});
