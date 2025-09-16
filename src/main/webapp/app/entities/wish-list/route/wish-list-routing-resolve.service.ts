import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWishList } from '../wish-list.model';
import { WishListService } from '../service/wish-list.service';

const wishListResolve = (route: ActivatedRouteSnapshot): Observable<null | IWishList> => {
  const id = route.params.id;
  if (id) {
    return inject(WishListService)
      .find(id)
      .pipe(
        mergeMap((wishList: HttpResponse<IWishList>) => {
          if (wishList.body) {
            return of(wishList.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default wishListResolve;
