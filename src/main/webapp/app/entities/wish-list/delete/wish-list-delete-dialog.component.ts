import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IWishList } from '../wish-list.model';
import { WishListService } from '../service/wish-list.service';

@Component({
  templateUrl: './wish-list-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class WishListDeleteDialogComponent {
  wishList?: IWishList;

  protected wishListService = inject(WishListService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.wishListService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
