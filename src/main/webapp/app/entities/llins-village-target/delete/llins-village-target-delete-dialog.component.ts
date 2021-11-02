import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILLINSVillageTarget } from '../llins-village-target.model';
import { LLINSVillageTargetService } from '../service/llins-village-target.service';

@Component({
  templateUrl: './llins-village-target-delete-dialog.component.html',
})
export class LLINSVillageTargetDeleteDialogComponent {
  lLINSVillageTarget?: ILLINSVillageTarget;

  constructor(protected lLINSVillageTargetService: LLINSVillageTargetService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lLINSVillageTargetService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
