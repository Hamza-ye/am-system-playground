import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMalariaUnitStaffMember } from '../malaria-unit-staff-member.model';

@Component({
  selector: 'app-malaria-unit-staff-member-detail',
  templateUrl: './malaria-unit-staff-member-detail.component.html',
})
export class MalariaUnitStaffMemberDetailComponent implements OnInit {
  malariaUnitStaffMember: IMalariaUnitStaffMember | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ malariaUnitStaffMember }) => {
      this.malariaUnitStaffMember = malariaUnitStaffMember;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
