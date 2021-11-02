import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWorkingDay } from '../working-day.model';
import { WorkingDayService } from '../service/working-day.service';

@Component({
  templateUrl: './working-day-delete-dialog.component.html',
})
export class WorkingDayDeleteDialogComponent {
  workingDay?: IWorkingDay;

  constructor(protected workingDayService: WorkingDayService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.workingDayService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
