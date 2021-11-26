import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILlinsFamilyReport } from '../llins-family-report.model';
import { LlinsFamilyReportService } from '../service/llins-family-report.service';

@Component({
  templateUrl: './llins-family-report-delete-dialog.component.html',
})
export class LlinsFamilyReportDeleteDialogComponent {
  llinsFamilyReport?: ILlinsFamilyReport;

  constructor(protected llinsFamilyReportService: LlinsFamilyReportService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.llinsFamilyReportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
