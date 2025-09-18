describe('Login via API', () => {
  it('should login as admin and fetch account info', () => {
    // Paso 1: pedir el token
    cy.request('POST', 'http://localhost:8080/api/authenticate', {
      username: 'admin',
      password: 'admin',
      rememberMe: true,
    }).then(response => {
      expect(response.status).to.eq(200);
      const token = response.body.id_token;

      // Paso 2: usar el token para pedir info de la cuenta
      cy.request({
        method: 'GET',
        url: 'http://localhost:8080/api/account',
        headers: {
          Authorization: `Bearer ${token}`, // 👈 header correcto
        },
      }).then(accountResponse => {
        expect(accountResponse.status).to.eq(200);
        expect(accountResponse.body.login).to.eq('admin'); // comprobamos que sea el admin
      });
    });
  });
});
