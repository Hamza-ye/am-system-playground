import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDataProvider } from '../data-provider.model';
import { DataProviderService } from '../service/data-provider.service';

@Component({
  templateUrl: './data-provider-delete-dialog.component.html',
})
export class DataProviderDeleteDialogComponent {
  dataProvider?: IDataProvider;

  constructor(protected dataProviderService: DataProviderService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dataProviderService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
