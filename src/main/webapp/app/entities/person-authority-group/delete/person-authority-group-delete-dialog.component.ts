import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPersonAuthorityGroup } from '../person-authority-group.model';
import { PersonAuthorityGroupService } from '../service/person-authority-group.service';

@Component({
  templateUrl: './person-authority-group-delete-dialog.component.html',
})
export class PersonAuthorityGroupDeleteDialogComponent {
  personAuthorityGroup?: IPersonAuthorityGroup;

  constructor(protected personAuthorityGroupService: PersonAuthorityGroupService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.personAuthorityGroupService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
