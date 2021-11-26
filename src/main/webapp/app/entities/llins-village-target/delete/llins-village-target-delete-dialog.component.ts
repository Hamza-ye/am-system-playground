import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILlinsVillageTarget } from '../llins-village-target.model';
import { LlinsVillageTargetService } from '../service/llins-village-target.service';

@Component({
  templateUrl: './llins-village-target-delete-dialog.component.html',
})
export class LlinsVillageTargetDeleteDialogComponent {
  llinsVillageTarget?: ILlinsVillageTarget;

  constructor(protected llinsVillageTargetService: LlinsVillageTargetService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.llinsVillageTargetService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
