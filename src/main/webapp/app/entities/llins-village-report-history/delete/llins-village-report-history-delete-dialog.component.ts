import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILlinsVillageReportHistory } from '../llins-village-report-history.model';
import { LlinsVillageReportHistoryService } from '../service/llins-village-report-history.service';

@Component({
  templateUrl: './llins-village-report-history-delete-dialog.component.html',
})
export class LlinsVillageReportHistoryDeleteDialogComponent {
  llinsVillageReportHistory?: ILlinsVillageReportHistory;

  constructor(protected llinsVillageReportHistoryService: LlinsVillageReportHistoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.llinsVillageReportHistoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
