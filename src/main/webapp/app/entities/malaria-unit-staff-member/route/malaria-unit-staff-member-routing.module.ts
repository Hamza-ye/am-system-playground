import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MalariaUnitStaffMemberComponent } from '../list/malaria-unit-staff-member.component';
import { MalariaUnitStaffMemberDetailComponent } from '../detail/malaria-unit-staff-member-detail.component';
import { MalariaUnitStaffMemberUpdateComponent } from '../update/malaria-unit-staff-member-update.component';
import { MalariaUnitStaffMemberRoutingResolveService } from './malaria-unit-staff-member-routing-resolve.service';

const malariaUnitStaffMemberRoute: Routes = [
  {
    path: '',
    component: MalariaUnitStaffMemberComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MalariaUnitStaffMemberDetailComponent,
    resolve: {
      malariaUnitStaffMember: MalariaUnitStaffMemberRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MalariaUnitStaffMemberUpdateComponent,
    resolve: {
      malariaUnitStaffMember: MalariaUnitStaffMemberRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MalariaUnitStaffMemberUpdateComponent,
    resolve: {
      malariaUnitStaffMember: MalariaUnitStaffMemberRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(malariaUnitStaffMemberRoute)],
  exports: [RouterModule],
})
export class MalariaUnitStaffMemberRoutingModule {}
