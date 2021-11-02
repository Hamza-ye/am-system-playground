import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWHMovement } from '../wh-movement.model';
import { WHMovementService } from '../service/wh-movement.service';

@Component({
  templateUrl: './wh-movement-delete-dialog.component.html',
})
export class WHMovementDeleteDialogComponent {
  wHMovement?: IWHMovement;

  constructor(protected wHMovementService: WHMovementService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.wHMovementService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
