import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrganisationUnitGroup } from '../organisation-unit-group.model';
import { OrganisationUnitGroupService } from '../service/organisation-unit-group.service';

@Component({
  templateUrl: './organisation-unit-group-delete-dialog.component.html',
})
export class OrganisationUnitGroupDeleteDialogComponent {
  organisationUnitGroup?: IOrganisationUnitGroup;

  constructor(protected organisationUnitGroupService: OrganisationUnitGroupService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.organisationUnitGroupService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
