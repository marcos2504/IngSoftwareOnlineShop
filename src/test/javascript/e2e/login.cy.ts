describe('Login UI', () => {
  it('should login as admin user through the UI', () => {
    // Ir al frontend
    cy.visit('http://localhost:9000/login');

    // Completar formulario de login
    cy.get('input#username').type('admin');
    cy.get('input#password').type('admin');
    cy.get('button[type="submit"]').click();

    // Verificar que carga el home con los menús
    cy.url().should('include', '/'); // debería volver al home
    cy.contains('Welcome, Java Hipster!').should('exist'); // texto del home
    cy.contains('Account').should('exist'); // menú Account
    cy.contains('Administration').should('exist'); // menú Administration
  });
});
