import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILLINSVillageReportHistory } from '../llins-village-report-history.model';
import { LLINSVillageReportHistoryService } from '../service/llins-village-report-history.service';

@Component({
  templateUrl: './llins-village-report-history-delete-dialog.component.html',
})
export class LLINSVillageReportHistoryDeleteDialogComponent {
  lLINSVillageReportHistory?: ILLINSVillageReportHistory;

  constructor(protected lLINSVillageReportHistoryService: LLINSVillageReportHistoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lLINSVillageReportHistoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
