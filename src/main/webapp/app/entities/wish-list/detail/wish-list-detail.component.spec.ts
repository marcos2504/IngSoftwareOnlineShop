import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { WishListDetailComponent } from './wish-list-detail.component';

describe('WishList Management Detail Component', () => {
  let comp: WishListDetailComponent;
  let fixture: ComponentFixture<WishListDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WishListDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./wish-list-detail.component').then(m => m.WishListDetailComponent),
              resolve: { wishList: () => of({ id: 18308 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(WishListDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WishListDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load wishList on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', WishListDetailComponent);

      // THEN
      expect(instance.wishList()).toEqual(expect.objectContaining({ id: 18308 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
