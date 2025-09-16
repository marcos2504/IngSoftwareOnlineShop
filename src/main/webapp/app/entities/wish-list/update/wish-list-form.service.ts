import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IWishList, NewWishList } from '../wish-list.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWishList for edit and NewWishListFormGroupInput for create.
 */
type WishListFormGroupInput = IWishList | PartialWithRequiredKeyOf<NewWishList>;

type WishListFormDefaults = Pick<NewWishList, 'id' | 'restricted' | 'products'>;

type WishListFormGroupContent = {
  id: FormControl<IWishList['id'] | NewWishList['id']>;
  title: FormControl<IWishList['title']>;
  restricted: FormControl<IWishList['restricted']>;
  products: FormControl<IWishList['products']>;
  customer: FormControl<IWishList['customer']>;
};

export type WishListFormGroup = FormGroup<WishListFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WishListFormService {
  createWishListFormGroup(wishList: WishListFormGroupInput = { id: null }): WishListFormGroup {
    const wishListRawValue = {
      ...this.getFormDefaults(),
      ...wishList,
    };
    return new FormGroup<WishListFormGroupContent>({
      id: new FormControl(
        { value: wishListRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(wishListRawValue.title, {
        validators: [Validators.required],
      }),
      restricted: new FormControl(wishListRawValue.restricted),
      products: new FormControl(wishListRawValue.products ?? []),
      customer: new FormControl(wishListRawValue.customer),
    });
  }

  getWishList(form: WishListFormGroup): IWishList | NewWishList {
    return form.getRawValue() as IWishList | NewWishList;
  }

  resetForm(form: WishListFormGroup, wishList: WishListFormGroupInput): void {
    const wishListRawValue = { ...this.getFormDefaults(), ...wishList };
    form.reset(
      {
        ...wishListRawValue,
        id: { value: wishListRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): WishListFormDefaults {
    return {
      id: null,
      restricted: false,
      products: [],
    };
  }
}
