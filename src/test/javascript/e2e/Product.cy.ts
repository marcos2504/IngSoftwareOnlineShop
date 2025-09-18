/// <reference types="cypress" />

describe('Login via API and create Product (API only)', () => {
  let token: string;

  before(() => {
    // Login API
    cy.request('POST', 'http://localhost:8080/api/authenticate', {
      username: 'admin',
      password: 'admin',
      rememberMe: true,
    }).then(res => {
      expect(res.status).to.eq(200);
      token = res.body.id_token;
    });
  });

  it('should create a product and verify it via API', () => {
    const newProduct = {
      title: 'Cypress API Product ' + Date.now(),
      price: 49.99,
      description: 'Product created via Cypress API',
      rating: 4,
      keywords: 'cypress,automation',
      categories: [{ id: 1 }], // ⚠️ usar IDs válidos en tu base
      wishLists: [],
    };

    // Crear producto por API
    cy.request({
      method: 'POST',
      url: 'http://localhost:8080/api/products',
      headers: { Authorization: `Bearer ${token}` },
      body: newProduct,
    }).then(res => {
      expect(res.status).to.eq(201);
      expect(res.body.title).to.eq(newProduct.title);

      // Verificar producto en el listado por API
      cy.request({
        method: 'GET',
        url: 'http://localhost:8080/api/products',
        headers: { Authorization: `Bearer ${token}` },
      }).then(getRes => {
        expect(getRes.status).to.eq(200);
        const titles = getRes.body.map((p: any) => p.title);
        expect(titles).to.include(newProduct.title);
      });
    });
  });
});
