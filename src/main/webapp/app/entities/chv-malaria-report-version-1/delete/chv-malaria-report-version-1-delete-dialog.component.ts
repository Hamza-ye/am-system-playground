import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IChvMalariaReportVersion1 } from '../chv-malaria-report-version-1.model';
import { ChvMalariaReportVersion1Service } from '../service/chv-malaria-report-version-1.service';

@Component({
  templateUrl: './chv-malaria-report-version-1-delete-dialog.component.html',
})
export class ChvMalariaReportVersion1DeleteDialogComponent {
  chvMalariaReportVersion1?: IChvMalariaReportVersion1;

  constructor(protected chvMalariaReportVersion1Service: ChvMalariaReportVersion1Service, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.chvMalariaReportVersion1Service.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
