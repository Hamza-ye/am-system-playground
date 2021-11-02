import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICHVMalariaCaseReport } from '../chv-malaria-case-report.model';
import { CHVMalariaCaseReportService } from '../service/chv-malaria-case-report.service';

@Component({
  templateUrl: './chv-malaria-case-report-delete-dialog.component.html',
})
export class CHVMalariaCaseReportDeleteDialogComponent {
  cHVMalariaCaseReport?: ICHVMalariaCaseReport;

  constructor(protected cHVMalariaCaseReportService: CHVMalariaCaseReportService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cHVMalariaCaseReportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
