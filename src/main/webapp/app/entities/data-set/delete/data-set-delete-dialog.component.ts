import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDataSet } from '../data-set.model';
import { DataSetService } from '../service/data-set.service';

@Component({
  templateUrl: './data-set-delete-dialog.component.html',
})
export class DataSetDeleteDialogComponent {
  dataSet?: IDataSet;

  constructor(protected dataSetService: DataSetService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dataSetService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
