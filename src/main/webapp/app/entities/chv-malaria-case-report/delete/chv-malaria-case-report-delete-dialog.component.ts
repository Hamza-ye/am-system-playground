import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IChvMalariaCaseReport } from '../chv-malaria-case-report.model';
import { ChvMalariaCaseReportService } from '../service/chv-malaria-case-report.service';

@Component({
  templateUrl: './chv-malaria-case-report-delete-dialog.component.html',
})
export class ChvMalariaCaseReportDeleteDialogComponent {
  chvMalariaCaseReport?: IChvMalariaCaseReport;

  constructor(protected chvMalariaCaseReportService: ChvMalariaCaseReportService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.chvMalariaCaseReportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
