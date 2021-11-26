import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IChvTeam } from '../chv-team.model';
import { ChvTeamService } from '../service/chv-team.service';

@Component({
  templateUrl: './chv-team-delete-dialog.component.html',
})
export class ChvTeamDeleteDialogComponent {
  chvTeam?: IChvTeam;

  constructor(protected chvTeamService: ChvTeamService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.chvTeamService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
