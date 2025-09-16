import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../wish-list.test-samples';

import { WishListFormService } from './wish-list-form.service';

describe('WishList Form Service', () => {
  let service: WishListFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WishListFormService);
  });

  describe('Service methods', () => {
    describe('createWishListFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWishListFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            restricted: expect.any(Object),
            products: expect.any(Object),
            customer: expect.any(Object),
          }),
        );
      });

      it('passing IWishList should create a new form with FormGroup', () => {
        const formGroup = service.createWishListFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            restricted: expect.any(Object),
            products: expect.any(Object),
            customer: expect.any(Object),
          }),
        );
      });
    });

    describe('getWishList', () => {
      it('should return NewWishList for default WishList initial value', () => {
        const formGroup = service.createWishListFormGroup(sampleWithNewData);

        const wishList = service.getWishList(formGroup) as any;

        expect(wishList).toMatchObject(sampleWithNewData);
      });

      it('should return NewWishList for empty WishList initial value', () => {
        const formGroup = service.createWishListFormGroup();

        const wishList = service.getWishList(formGroup) as any;

        expect(wishList).toMatchObject({});
      });

      it('should return IWishList', () => {
        const formGroup = service.createWishListFormGroup(sampleWithRequiredData);

        const wishList = service.getWishList(formGroup) as any;

        expect(wishList).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWishList should not enable id FormControl', () => {
        const formGroup = service.createWishListFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWishList should disable id FormControl', () => {
        const formGroup = service.createWishListFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
