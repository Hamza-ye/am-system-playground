import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LlinsVillageTargetComponent } from '../list/llins-village-target.component';
import { LlinsVillageTargetDetailComponent } from '../detail/llins-village-target-detail.component';
import { LlinsVillageTargetUpdateComponent } from '../update/llins-village-target-update.component';
import { LlinsVillageTargetRoutingResolveService } from './llins-village-target-routing-resolve.service';

const llinsVillageTargetRoute: Routes = [
  {
    path: '',
    component: LlinsVillageTargetComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LlinsVillageTargetDetailComponent,
    resolve: {
      llinsVillageTarget: LlinsVillageTargetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LlinsVillageTargetUpdateComponent,
    resolve: {
      llinsVillageTarget: LlinsVillageTargetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LlinsVillageTargetUpdateComponent,
    resolve: {
      llinsVillageTarget: LlinsVillageTargetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(llinsVillageTargetRoute)],
  exports: [RouterModule],
})
export class LlinsVillageTargetRoutingModule {}
