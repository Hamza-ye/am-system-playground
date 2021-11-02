import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILLINSFamilyReport } from '../llins-family-report.model';
import { LLINSFamilyReportService } from '../service/llins-family-report.service';

@Component({
  templateUrl: './llins-family-report-delete-dialog.component.html',
})
export class LLINSFamilyReportDeleteDialogComponent {
  lLINSFamilyReport?: ILLINSFamilyReport;

  constructor(protected lLINSFamilyReportService: LLINSFamilyReportService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lLINSFamilyReportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
