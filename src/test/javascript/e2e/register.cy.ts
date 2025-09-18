/// <reference types="cypress" />

describe('Register UI', () => {
  it('should register a new user', () => {
    const username = 'testuser' + Date.now();

    cy.visit('http://localhost:9000/account/register');

    // Usamos IDs únicos en el formulario
    cy.get('input#login').type(username);
    cy.get('input#email').type(`${username}@example.com`);
    cy.get('input#password').type('Test1234!');
    cy.get('input#confirmPassword').type('Test1234!');

    // Enviar
    cy.get('button[type="submit"]').click();

    // Verificar mensaje de éxito
    cy.contains('Registration saved!').should('exist');
  });
});
