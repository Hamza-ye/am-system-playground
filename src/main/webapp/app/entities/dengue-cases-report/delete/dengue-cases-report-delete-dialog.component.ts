import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDengueCasesReport } from '../dengue-cases-report.model';
import { DengueCasesReportService } from '../service/dengue-cases-report.service';

@Component({
  templateUrl: './dengue-cases-report-delete-dialog.component.html',
})
export class DengueCasesReportDeleteDialogComponent {
  dengueCasesReport?: IDengueCasesReport;

  constructor(protected dengueCasesReportService: DengueCasesReportService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dengueCasesReportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
