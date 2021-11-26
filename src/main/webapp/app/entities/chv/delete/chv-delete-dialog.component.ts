import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IChv } from '../chv.model';
import { ChvService } from '../service/chv.service';

@Component({
  templateUrl: './chv-delete-dialog.component.html',
})
export class ChvDeleteDialogComponent {
  chv?: IChv;

  constructor(protected chvService: ChvService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.chvService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
