import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICHVTeam } from '../chv-team.model';
import { CHVTeamService } from '../service/chv-team.service';

@Component({
  templateUrl: './chv-team-delete-dialog.component.html',
})
export class CHVTeamDeleteDialogComponent {
  cHVTeam?: ICHVTeam;

  constructor(protected cHVTeamService: CHVTeamService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cHVTeamService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
