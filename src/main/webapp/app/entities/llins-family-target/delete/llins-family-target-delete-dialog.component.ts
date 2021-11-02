import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILLINSFamilyTarget } from '../llins-family-target.model';
import { LLINSFamilyTargetService } from '../service/llins-family-target.service';

@Component({
  templateUrl: './llins-family-target-delete-dialog.component.html',
})
export class LLINSFamilyTargetDeleteDialogComponent {
  lLINSFamilyTarget?: ILLINSFamilyTarget;

  constructor(protected lLINSFamilyTargetService: LLINSFamilyTargetService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lLINSFamilyTargetService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
