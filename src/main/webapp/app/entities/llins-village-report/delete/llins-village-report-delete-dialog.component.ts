import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILLINSVillageReport } from '../llins-village-report.model';
import { LLINSVillageReportService } from '../service/llins-village-report.service';

@Component({
  templateUrl: './llins-village-report-delete-dialog.component.html',
})
export class LLINSVillageReportDeleteDialogComponent {
  lLINSVillageReport?: ILLINSVillageReport;

  constructor(protected lLINSVillageReportService: LLINSVillageReportService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lLINSVillageReportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
