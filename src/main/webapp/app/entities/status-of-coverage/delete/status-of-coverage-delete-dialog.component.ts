import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IStatusOfCoverage } from '../status-of-coverage.model';
import { StatusOfCoverageService } from '../service/status-of-coverage.service';

@Component({
  templateUrl: './status-of-coverage-delete-dialog.component.html',
})
export class StatusOfCoverageDeleteDialogComponent {
  statusOfCoverage?: IStatusOfCoverage;

  constructor(protected statusOfCoverageService: StatusOfCoverageService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.statusOfCoverageService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
