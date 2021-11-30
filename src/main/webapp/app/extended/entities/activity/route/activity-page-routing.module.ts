import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ActivityPageComponent } from '../list/activity-page.component';
import { ActivityPageDetailComponent } from '../detail/activity-page-detail.component';
import { ActivityPageRoutingResolveService } from './activity-page-routing-resolve.service';

const activityRoute: Routes = [
  {
    path: '',
    component: ActivityPageComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ActivityPageDetailComponent,
    resolve: {
      activity: ActivityPageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(activityRoute)],
  exports: [RouterModule],
})
export class ActivityPageRoutingModule {}
