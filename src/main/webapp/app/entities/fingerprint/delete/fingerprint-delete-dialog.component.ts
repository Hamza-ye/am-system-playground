import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFingerprint } from '../fingerprint.model';
import { FingerprintService } from '../service/fingerprint.service';

@Component({
  templateUrl: './fingerprint-delete-dialog.component.html',
})
export class FingerprintDeleteDialogComponent {
  fingerprint?: IFingerprint;

  constructor(protected fingerprintService: FingerprintService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fingerprintService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
