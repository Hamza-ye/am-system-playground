import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrganisationUnitLevel } from '../organisation-unit-level.model';
import { OrganisationUnitLevelService } from '../service/organisation-unit-level.service';

@Component({
  templateUrl: './organisation-unit-level-delete-dialog.component.html',
})
export class OrganisationUnitLevelDeleteDialogComponent {
  organisationUnitLevel?: IOrganisationUnitLevel;

  constructor(protected organisationUnitLevelService: OrganisationUnitLevelService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.organisationUnitLevelService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
