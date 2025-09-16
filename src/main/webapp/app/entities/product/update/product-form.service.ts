import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProduct, NewProduct } from '../product.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProduct for edit and NewProductFormGroupInput for create.
 */
type ProductFormGroupInput = IProduct | PartialWithRequiredKeyOf<NewProduct>;

type ProductFormDefaults = Pick<NewProduct, 'id' | 'categories' | 'wishLists'>;

type ProductFormGroupContent = {
  id: FormControl<IProduct['id'] | NewProduct['id']>;
  title: FormControl<IProduct['title']>;
  keywords: FormControl<IProduct['keywords']>;
  description: FormControl<IProduct['description']>;
  rating: FormControl<IProduct['rating']>;
  price: FormControl<IProduct['price']>;
  image: FormControl<IProduct['image']>;
  imageContentType: FormControl<IProduct['imageContentType']>;
  dateAdded: FormControl<IProduct['dateAdded']>;
  dateModified: FormControl<IProduct['dateModified']>;
  categories: FormControl<IProduct['categories']>;
  wishLists: FormControl<IProduct['wishLists']>;
};

export type ProductFormGroup = FormGroup<ProductFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductFormService {
  createProductFormGroup(product: ProductFormGroupInput = { id: null }): ProductFormGroup {
    const productRawValue = {
      ...this.getFormDefaults(),
      ...product,
    };
    return new FormGroup<ProductFormGroupContent>({
      id: new FormControl(
        { value: productRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(productRawValue.title, {
        validators: [Validators.required],
      }),
      keywords: new FormControl(productRawValue.keywords),
      description: new FormControl(productRawValue.description),
      rating: new FormControl(productRawValue.rating),
      price: new FormControl(productRawValue.price, {
        validators: [Validators.required],
      }),
      image: new FormControl(productRawValue.image),
      imageContentType: new FormControl(productRawValue.imageContentType),
      dateAdded: new FormControl(productRawValue.dateAdded),
      dateModified: new FormControl(productRawValue.dateModified),
      categories: new FormControl(productRawValue.categories ?? []),
      wishLists: new FormControl(productRawValue.wishLists ?? []),
    });
  }

  getProduct(form: ProductFormGroup): IProduct | NewProduct {
    return form.getRawValue() as IProduct | NewProduct;
  }

  resetForm(form: ProductFormGroup, product: ProductFormGroupInput): void {
    const productRawValue = { ...this.getFormDefaults(), ...product };
    form.reset(
      {
        ...productRawValue,
        id: { value: productRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProductFormDefaults {
    return {
      id: null,
      categories: [],
      wishLists: [],
    };
  }
}
