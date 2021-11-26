import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILlinsFamilyReportHistory } from '../llins-family-report-history.model';
import { LlinsFamilyReportHistoryService } from '../service/llins-family-report-history.service';

@Component({
  templateUrl: './llins-family-report-history-delete-dialog.component.html',
})
export class LlinsFamilyReportHistoryDeleteDialogComponent {
  llinsFamilyReportHistory?: ILlinsFamilyReportHistory;

  constructor(protected llinsFamilyReportHistoryService: LlinsFamilyReportHistoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.llinsFamilyReportHistoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
