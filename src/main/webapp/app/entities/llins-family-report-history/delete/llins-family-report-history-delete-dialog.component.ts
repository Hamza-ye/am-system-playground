import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILLINSFamilyReportHistory } from '../llins-family-report-history.model';
import { LLINSFamilyReportHistoryService } from '../service/llins-family-report-history.service';

@Component({
  templateUrl: './llins-family-report-history-delete-dialog.component.html',
})
export class LLINSFamilyReportHistoryDeleteDialogComponent {
  lLINSFamilyReportHistory?: ILLINSFamilyReportHistory;

  constructor(protected lLINSFamilyReportHistoryService: LLINSFamilyReportHistoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lLINSFamilyReportHistoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
