import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import WishListResolve from './route/wish-list-routing-resolve.service';

const wishListRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/wish-list.component').then(m => m.WishListComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/wish-list-detail.component').then(m => m.WishListDetailComponent),
    resolve: {
      wishList: WishListResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/wish-list-update.component').then(m => m.WishListUpdateComponent),
    resolve: {
      wishList: WishListResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/wish-list-update.component').then(m => m.WishListUpdateComponent),
    resolve: {
      wishList: WishListResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default wishListRoute;
