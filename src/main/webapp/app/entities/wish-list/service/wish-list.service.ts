import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWishList, NewWishList } from '../wish-list.model';

export type PartialUpdateWishList = Partial<IWishList> & Pick<IWishList, 'id'>;

export type EntityResponseType = HttpResponse<IWishList>;
export type EntityArrayResponseType = HttpResponse<IWishList[]>;

@Injectable({ providedIn: 'root' })
export class WishListService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/wish-lists');

  create(wishList: NewWishList): Observable<EntityResponseType> {
    return this.http.post<IWishList>(this.resourceUrl, wishList, { observe: 'response' });
  }

  update(wishList: IWishList): Observable<EntityResponseType> {
    return this.http.put<IWishList>(`${this.resourceUrl}/${this.getWishListIdentifier(wishList)}`, wishList, { observe: 'response' });
  }

  partialUpdate(wishList: PartialUpdateWishList): Observable<EntityResponseType> {
    return this.http.patch<IWishList>(`${this.resourceUrl}/${this.getWishListIdentifier(wishList)}`, wishList, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWishList>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWishList[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getWishListIdentifier(wishList: Pick<IWishList, 'id'>): number {
    return wishList.id;
  }

  compareWishList(o1: Pick<IWishList, 'id'> | null, o2: Pick<IWishList, 'id'> | null): boolean {
    return o1 && o2 ? this.getWishListIdentifier(o1) === this.getWishListIdentifier(o2) : o1 === o2;
  }

  addWishListToCollectionIfMissing<Type extends Pick<IWishList, 'id'>>(
    wishListCollection: Type[],
    ...wishListsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const wishLists: Type[] = wishListsToCheck.filter(isPresent);
    if (wishLists.length > 0) {
      const wishListCollectionIdentifiers = wishListCollection.map(wishListItem => this.getWishListIdentifier(wishListItem));
      const wishListsToAdd = wishLists.filter(wishListItem => {
        const wishListIdentifier = this.getWishListIdentifier(wishListItem);
        if (wishListCollectionIdentifiers.includes(wishListIdentifier)) {
          return false;
        }
        wishListCollectionIdentifiers.push(wishListIdentifier);
        return true;
      });
      return [...wishListsToAdd, ...wishListCollection];
    }
    return wishListCollection;
  }
}
