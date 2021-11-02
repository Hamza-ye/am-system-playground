import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FamilyHeadComponent } from '../list/family-head.component';
import { FamilyHeadDetailComponent } from '../detail/family-head-detail.component';
import { FamilyHeadUpdateComponent } from '../update/family-head-update.component';
import { FamilyHeadRoutingResolveService } from './family-head-routing-resolve.service';

const familyHeadRoute: Routes = [
  {
    path: '',
    component: FamilyHeadComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FamilyHeadDetailComponent,
    resolve: {
      familyHead: FamilyHeadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FamilyHeadUpdateComponent,
    resolve: {
      familyHead: FamilyHeadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FamilyHeadUpdateComponent,
    resolve: {
      familyHead: FamilyHeadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(familyHeadRoute)],
  exports: [RouterModule],
})
export class FamilyHeadRoutingModule {}
