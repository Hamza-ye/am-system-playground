import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LLINSFamilyTargetComponent } from '../list/llins-family-target.component';
import { LLINSFamilyTargetDetailComponent } from '../detail/llins-family-target-detail.component';
import { LLINSFamilyTargetUpdateComponent } from '../update/llins-family-target-update.component';
import { LLINSFamilyTargetRoutingResolveService } from './llins-family-target-routing-resolve.service';

const lLINSFamilyTargetRoute: Routes = [
  {
    path: '',
    component: LLINSFamilyTargetComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LLINSFamilyTargetDetailComponent,
    resolve: {
      lLINSFamilyTarget: LLINSFamilyTargetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LLINSFamilyTargetUpdateComponent,
    resolve: {
      lLINSFamilyTarget: LLINSFamilyTargetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LLINSFamilyTargetUpdateComponent,
    resolve: {
      lLINSFamilyTarget: LLINSFamilyTargetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(lLINSFamilyTargetRoute)],
  exports: [RouterModule],
})
export class LLINSFamilyTargetRoutingModule {}
