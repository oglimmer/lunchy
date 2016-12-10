exports.config = {
  seleniumAddress: 'http://localhost:4444/wd/hub',
  framework: "jasmine2",
  specs: ['spec.js'],
  baseUrl: 'http://localhost:8080/',
  // Chrome is not allowed to create a SUID sandbox when running inside Docker  
  capabilities: {
    'browserName': 'chrome',
    'chromeOptions': {
      'args': ['no-sandbox']
    }
  }
};