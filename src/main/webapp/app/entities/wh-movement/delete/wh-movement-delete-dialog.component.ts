import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWhMovement } from '../wh-movement.model';
import { WhMovementService } from '../service/wh-movement.service';

@Component({
  templateUrl: './wh-movement-delete-dialog.component.html',
})
export class WhMovementDeleteDialogComponent {
  whMovement?: IWhMovement;

  constructor(protected whMovementService: WhMovementService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.whMovementService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
