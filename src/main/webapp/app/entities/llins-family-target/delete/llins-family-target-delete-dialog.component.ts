import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILlinsFamilyTarget } from '../llins-family-target.model';
import { LlinsFamilyTargetService } from '../service/llins-family-target.service';

@Component({
  templateUrl: './llins-family-target-delete-dialog.component.html',
})
export class LlinsFamilyTargetDeleteDialogComponent {
  llinsFamilyTarget?: ILlinsFamilyTarget;

  constructor(protected llinsFamilyTargetService: LlinsFamilyTargetService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.llinsFamilyTargetService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
