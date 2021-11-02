import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICHVMalariaReportVersion1 } from '../chv-malaria-report-version-1.model';
import { CHVMalariaReportVersion1Service } from '../service/chv-malaria-report-version-1.service';

@Component({
  templateUrl: './chv-malaria-report-version-1-delete-dialog.component.html',
})
export class CHVMalariaReportVersion1DeleteDialogComponent {
  cHVMalariaReportVersion1?: ICHVMalariaReportVersion1;

  constructor(protected cHVMalariaReportVersion1Service: CHVMalariaReportVersion1Service, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cHVMalariaReportVersion1Service.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
