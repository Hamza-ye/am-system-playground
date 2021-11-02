import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMalariaUnitStaffMember } from '../malaria-unit-staff-member.model';
import { MalariaUnitStaffMemberService } from '../service/malaria-unit-staff-member.service';
import { MalariaUnitStaffMemberDeleteDialogComponent } from '../delete/malaria-unit-staff-member-delete-dialog.component';

@Component({
  selector: 'app-malaria-unit-staff-member',
  templateUrl: './malaria-unit-staff-member.component.html',
})
export class MalariaUnitStaffMemberComponent implements OnInit {
  malariaUnitStaffMembers?: IMalariaUnitStaffMember[];
  isLoading = false;

  constructor(protected malariaUnitStaffMemberService: MalariaUnitStaffMemberService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.malariaUnitStaffMemberService.query().subscribe(
      (res: HttpResponse<IMalariaUnitStaffMember[]>) => {
        this.isLoading = false;
        this.malariaUnitStaffMembers = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IMalariaUnitStaffMember): number {
    return item.id!;
  }

  delete(malariaUnitStaffMember: IMalariaUnitStaffMember): void {
    const modalRef = this.modalService.open(MalariaUnitStaffMemberDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.malariaUnitStaffMember = malariaUnitStaffMember;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
