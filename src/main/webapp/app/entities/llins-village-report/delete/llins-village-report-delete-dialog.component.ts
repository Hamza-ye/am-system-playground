import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILlinsVillageReport } from '../llins-village-report.model';
import { LlinsVillageReportService } from '../service/llins-village-report.service';

@Component({
  templateUrl: './llins-village-report-delete-dialog.component.html',
})
export class LlinsVillageReportDeleteDialogComponent {
  llinsVillageReport?: ILlinsVillageReport;

  constructor(protected llinsVillageReportService: LlinsVillageReportService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.llinsVillageReportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
