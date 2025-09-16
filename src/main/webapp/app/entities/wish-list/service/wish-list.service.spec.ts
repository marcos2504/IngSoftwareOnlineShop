import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IWishList } from '../wish-list.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../wish-list.test-samples';

import { WishListService } from './wish-list.service';

const requireRestSample: IWishList = {
  ...sampleWithRequiredData,
};

describe('WishList Service', () => {
  let service: WishListService;
  let httpMock: HttpTestingController;
  let expectedResult: IWishList | IWishList[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(WishListService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a WishList', () => {
      const wishList = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(wishList).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WishList', () => {
      const wishList = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(wishList).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WishList', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WishList', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a WishList', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addWishListToCollectionIfMissing', () => {
      it('should add a WishList to an empty array', () => {
        const wishList: IWishList = sampleWithRequiredData;
        expectedResult = service.addWishListToCollectionIfMissing([], wishList);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(wishList);
      });

      it('should not add a WishList to an array that contains it', () => {
        const wishList: IWishList = sampleWithRequiredData;
        const wishListCollection: IWishList[] = [
          {
            ...wishList,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addWishListToCollectionIfMissing(wishListCollection, wishList);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WishList to an array that doesn't contain it", () => {
        const wishList: IWishList = sampleWithRequiredData;
        const wishListCollection: IWishList[] = [sampleWithPartialData];
        expectedResult = service.addWishListToCollectionIfMissing(wishListCollection, wishList);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(wishList);
      });

      it('should add only unique WishList to an array', () => {
        const wishListArray: IWishList[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const wishListCollection: IWishList[] = [sampleWithRequiredData];
        expectedResult = service.addWishListToCollectionIfMissing(wishListCollection, ...wishListArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const wishList: IWishList = sampleWithRequiredData;
        const wishList2: IWishList = sampleWithPartialData;
        expectedResult = service.addWishListToCollectionIfMissing([], wishList, wishList2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(wishList);
        expect(expectedResult).toContain(wishList2);
      });

      it('should accept null and undefined values', () => {
        const wishList: IWishList = sampleWithRequiredData;
        expectedResult = service.addWishListToCollectionIfMissing([], null, wishList, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(wishList);
      });

      it('should return initial array if no WishList is added', () => {
        const wishListCollection: IWishList[] = [sampleWithRequiredData];
        expectedResult = service.addWishListToCollectionIfMissing(wishListCollection, undefined, null);
        expect(expectedResult).toEqual(wishListCollection);
      });
    });

    describe('compareWishList', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareWishList(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 18308 };
        const entity2 = null;

        const compareResult1 = service.compareWishList(entity1, entity2);
        const compareResult2 = service.compareWishList(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 18308 };
        const entity2 = { id: 15584 };

        const compareResult1 = service.compareWishList(entity1, entity2);
        const compareResult2 = service.compareWishList(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 18308 };
        const entity2 = { id: 18308 };

        const compareResult1 = service.compareWishList(entity1, entity2);
        const compareResult2 = service.compareWishList(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
