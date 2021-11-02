import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LLINSVillageTargetComponent } from '../list/llins-village-target.component';
import { LLINSVillageTargetDetailComponent } from '../detail/llins-village-target-detail.component';
import { LLINSVillageTargetUpdateComponent } from '../update/llins-village-target-update.component';
import { LLINSVillageTargetRoutingResolveService } from './llins-village-target-routing-resolve.service';

const lLINSVillageTargetRoute: Routes = [
  {
    path: '',
    component: LLINSVillageTargetComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LLINSVillageTargetDetailComponent,
    resolve: {
      lLINSVillageTarget: LLINSVillageTargetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LLINSVillageTargetUpdateComponent,
    resolve: {
      lLINSVillageTarget: LLINSVillageTargetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LLINSVillageTargetUpdateComponent,
    resolve: {
      lLINSVillageTarget: LLINSVillageTargetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(lLINSVillageTargetRoute)],
  exports: [RouterModule],
})
export class LLINSVillageTargetRoutingModule {}
