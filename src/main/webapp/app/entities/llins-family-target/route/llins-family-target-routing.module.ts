import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LlinsFamilyTargetComponent } from '../list/llins-family-target.component';
import { LlinsFamilyTargetDetailComponent } from '../detail/llins-family-target-detail.component';
import { LlinsFamilyTargetUpdateComponent } from '../update/llins-family-target-update.component';
import { LlinsFamilyTargetRoutingResolveService } from './llins-family-target-routing-resolve.service';

const llinsFamilyTargetRoute: Routes = [
  {
    path: '',
    component: LlinsFamilyTargetComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LlinsFamilyTargetDetailComponent,
    resolve: {
      llinsFamilyTarget: LlinsFamilyTargetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LlinsFamilyTargetUpdateComponent,
    resolve: {
      llinsFamilyTarget: LlinsFamilyTargetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LlinsFamilyTargetUpdateComponent,
    resolve: {
      llinsFamilyTarget: LlinsFamilyTargetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(llinsFamilyTargetRoute)],
  exports: [RouterModule],
})
export class LlinsFamilyTargetRoutingModule {}
