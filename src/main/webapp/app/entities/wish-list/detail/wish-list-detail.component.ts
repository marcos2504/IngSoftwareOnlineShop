import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IWishList } from '../wish-list.model';

@Component({
  selector: 'jhi-wish-list-detail',
  templateUrl: './wish-list-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class WishListDetailComponent {
  wishList = input<IWishList | null>(null);

  previousState(): void {
    window.history.back();
  }
}
