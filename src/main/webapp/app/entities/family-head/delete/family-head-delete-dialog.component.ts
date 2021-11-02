import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFamilyHead } from '../family-head.model';
import { FamilyHeadService } from '../service/family-head.service';

@Component({
  templateUrl: './family-head-delete-dialog.component.html',
})
export class FamilyHeadDeleteDialogComponent {
  familyHead?: IFamilyHead;

  constructor(protected familyHeadService: FamilyHeadService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.familyHeadService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
