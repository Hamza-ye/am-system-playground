import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICasesReportClass } from '../cases-report-class.model';
import { CasesReportClassService } from '../service/cases-report-class.service';

@Component({
  templateUrl: './cases-report-class-delete-dialog.component.html',
})
export class CasesReportClassDeleteDialogComponent {
  casesReportClass?: ICasesReportClass;

  constructor(protected casesReportClassService: CasesReportClassService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.casesReportClassService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
