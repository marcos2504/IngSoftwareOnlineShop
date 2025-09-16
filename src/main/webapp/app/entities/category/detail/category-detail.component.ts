import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { ICategory } from '../category.model';

@Component({
  selector: 'jhi-category-detail',
  templateUrl: './category-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class CategoryDetailComponent {
  category = input<ICategory | null>(null);

  previousState(): void {
    window.history.back();
  }
}
