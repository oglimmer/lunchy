describe('Lunchy', function() {
	
	var userEmail = 'test@test.de' + Math.random();
	
	it('Create user', function() {
		
		browser.get('http://localhost:8080/#/updates');
		
		var login = element(by.linkText('Login/Register'));
		login.click();

		var register = element(by.buttonText('Register account'));
		register.click();

		var email = element(by.model('newUser.email'))
		email.sendKeys(userEmail);
		var email = element(by.model('newUser.displayname'))
		email.sendKeys('Test-User');
		var pass = element(by.model('newUser.password'))
		pass.sendKeys('fooba');
		var pass = element(by.model('newUser.confirm_password'))
		pass.sendKeys('fooba');
		element(by.cssContainingText('option', 'Office No.1')).click();
		
		element(by.buttonText('Register')).click();
		
		element(by.linkText('Log out')).click();
		
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
		
		login();
		
		element(by.linkText('Add')).click();		
		
		element(by.model('data.officialName')).sendKeys("First Test Location")
		element(by.model('data.address')).sendKeys("Alexanderplatz 1")
		element(by.model('data.city')).sendKeys("Berlin")
				
		element(by.buttonText('Save')).click();		
	});
});
