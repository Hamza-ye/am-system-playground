import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrganisationUnit } from '../organisation-unit.model';
import { OrganisationUnitService } from '../service/organisation-unit.service';

@Component({
  templateUrl: './organisation-unit-delete-dialog.component.html',
})
export class OrganisationUnitDeleteDialogComponent {
  organisationUnit?: IOrganisationUnit;

  constructor(protected organisationUnitService: OrganisationUnitService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.organisationUnitService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
