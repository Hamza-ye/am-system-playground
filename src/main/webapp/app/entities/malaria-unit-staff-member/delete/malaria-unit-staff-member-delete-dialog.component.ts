import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMalariaUnitStaffMember } from '../malaria-unit-staff-member.model';
import { MalariaUnitStaffMemberService } from '../service/malaria-unit-staff-member.service';

@Component({
  templateUrl: './malaria-unit-staff-member-delete-dialog.component.html',
})
export class MalariaUnitStaffMemberDeleteDialogComponent {
  malariaUnitStaffMember?: IMalariaUnitStaffMember;

  constructor(protected malariaUnitStaffMemberService: MalariaUnitStaffMemberService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.malariaUnitStaffMemberService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
