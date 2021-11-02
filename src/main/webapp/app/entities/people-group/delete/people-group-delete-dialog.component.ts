import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPeopleGroup } from '../people-group.model';
import { PeopleGroupService } from '../service/people-group.service';

@Component({
  templateUrl: './people-group-delete-dialog.component.html',
})
export class PeopleGroupDeleteDialogComponent {
  peopleGroup?: IPeopleGroup;

  constructor(protected peopleGroupService: PeopleGroupService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.peopleGroupService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
