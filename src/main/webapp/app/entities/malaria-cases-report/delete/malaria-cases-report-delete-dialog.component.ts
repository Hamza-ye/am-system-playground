import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMalariaCasesReport } from '../malaria-cases-report.model';
import { MalariaCasesReportService } from '../service/malaria-cases-report.service';

@Component({
  templateUrl: './malaria-cases-report-delete-dialog.component.html',
})
export class MalariaCasesReportDeleteDialogComponent {
  malariaCasesReport?: IMalariaCasesReport;

  constructor(protected malariaCasesReportService: MalariaCasesReportService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.malariaCasesReportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
