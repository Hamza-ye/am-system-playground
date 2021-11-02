import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICHV } from '../chv.model';
import { CHVService } from '../service/chv.service';

@Component({
  templateUrl: './chv-delete-dialog.component.html',
})
export class CHVDeleteDialogComponent {
  cHV?: ICHV;

  constructor(protected cHVService: CHVService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cHVService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
