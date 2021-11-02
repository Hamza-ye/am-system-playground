import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMalariaUnit } from '../malaria-unit.model';
import { MalariaUnitService } from '../service/malaria-unit.service';

@Component({
  templateUrl: './malaria-unit-delete-dialog.component.html',
})
export class MalariaUnitDeleteDialogComponent {
  malariaUnit?: IMalariaUnit;

  constructor(protected malariaUnitService: MalariaUnitService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.malariaUnitService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
