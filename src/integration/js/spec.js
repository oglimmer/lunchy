describe('Lunchy', function() {

	it('contains spec with an expectation', function() {
		expect(true).toBe(true);
	});

	it('should have a title', function() {
				
		browser.get('http://localhost:8080/#/updates');

		var login = element(by.linkText('Login/Register'));
		login.click();

		var email = element(by.model('login.email'))
		email.sendKeys('test@test.de');
		var pass = element(by.model('login.password'))
		pass.sendKeys('the_pass');

		element(by.buttonText('Log me in')).click();

		element(by.linkText('Log out')).click();

	});
});
