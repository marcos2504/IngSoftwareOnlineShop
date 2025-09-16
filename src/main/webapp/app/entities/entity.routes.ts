import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'onlineShopApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'category',
    data: { pageTitle: 'onlineShopApp.category.home.title' },
    loadChildren: () => import('./category/category.routes'),
  },
  {
    path: 'product',
    data: { pageTitle: 'onlineShopApp.product.home.title' },
    loadChildren: () => import('./product/product.routes'),
  },
  {
    path: 'customer',
    data: { pageTitle: 'onlineShopApp.customer.home.title' },
    loadChildren: () => import('./customer/customer.routes'),
  },
  {
    path: 'address',
    data: { pageTitle: 'onlineShopApp.address.home.title' },
    loadChildren: () => import('./address/address.routes'),
  },
  {
    path: 'wish-list',
    data: { pageTitle: 'onlineShopApp.wishList.home.title' },
    loadChildren: () => import('./wish-list/wish-list.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
