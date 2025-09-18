const { defineConfig } = require('cypress');

module.exports = defineConfig({
  e2e: {
    baseUrl: 'http://localhost:9000',
    supportFile: false,
    specPattern: 'src/test/javascript/e2e/**/*.cy.{js,ts}',
  },
});
