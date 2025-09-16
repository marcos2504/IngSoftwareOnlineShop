import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { IWishList } from '../wish-list.model';
import { WishListService } from '../service/wish-list.service';
import { WishListFormService } from './wish-list-form.service';

import { WishListUpdateComponent } from './wish-list-update.component';

describe('WishList Management Update Component', () => {
  let comp: WishListUpdateComponent;
  let fixture: ComponentFixture<WishListUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let wishListFormService: WishListFormService;
  let wishListService: WishListService;
  let productService: ProductService;
  let customerService: CustomerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [WishListUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(WishListUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WishListUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    wishListFormService = TestBed.inject(WishListFormService);
    wishListService = TestBed.inject(WishListService);
    productService = TestBed.inject(ProductService);
    customerService = TestBed.inject(CustomerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Product query and add missing value', () => {
      const wishList: IWishList = { id: 15584 };
      const products: IProduct[] = [{ id: 21536 }];
      wishList.products = products;

      const productCollection: IProduct[] = [{ id: 21536 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [...products];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ wishList });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining),
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('should call Customer query and add missing value', () => {
      const wishList: IWishList = { id: 15584 };
      const customer: ICustomer = { id: 26915 };
      wishList.customer = customer;

      const customerCollection: ICustomer[] = [{ id: 26915 }];
      jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
      const additionalCustomers = [customer];
      const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
      jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ wishList });
      comp.ngOnInit();

      expect(customerService.query).toHaveBeenCalled();
      expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(
        customerCollection,
        ...additionalCustomers.map(expect.objectContaining),
      );
      expect(comp.customersSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const wishList: IWishList = { id: 15584 };
      const products: IProduct = { id: 21536 };
      wishList.products = [products];
      const customer: ICustomer = { id: 26915 };
      wishList.customer = customer;

      activatedRoute.data = of({ wishList });
      comp.ngOnInit();

      expect(comp.productsSharedCollection).toContainEqual(products);
      expect(comp.customersSharedCollection).toContainEqual(customer);
      expect(comp.wishList).toEqual(wishList);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWishList>>();
      const wishList = { id: 18308 };
      jest.spyOn(wishListFormService, 'getWishList').mockReturnValue(wishList);
      jest.spyOn(wishListService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wishList });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: wishList }));
      saveSubject.complete();

      // THEN
      expect(wishListFormService.getWishList).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(wishListService.update).toHaveBeenCalledWith(expect.objectContaining(wishList));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWishList>>();
      const wishList = { id: 18308 };
      jest.spyOn(wishListFormService, 'getWishList').mockReturnValue({ id: null });
      jest.spyOn(wishListService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wishList: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: wishList }));
      saveSubject.complete();

      // THEN
      expect(wishListFormService.getWishList).toHaveBeenCalled();
      expect(wishListService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWishList>>();
      const wishList = { id: 18308 };
      jest.spyOn(wishListService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wishList });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(wishListService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProduct', () => {
      it('should forward to productService', () => {
        const entity = { id: 21536 };
        const entity2 = { id: 11926 };
        jest.spyOn(productService, 'compareProduct');
        comp.compareProduct(entity, entity2);
        expect(productService.compareProduct).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCustomer', () => {
      it('should forward to customerService', () => {
        const entity = { id: 26915 };
        const entity2 = { id: 21032 };
        jest.spyOn(customerService, 'compareCustomer');
        comp.compareCustomer(entity, entity2);
        expect(customerService.compareCustomer).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
