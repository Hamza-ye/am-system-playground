import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WorkingDayComponent } from '../list/working-day.component';
import { WorkingDayDetailComponent } from '../detail/working-day-detail.component';
import { WorkingDayUpdateComponent } from '../update/working-day-update.component';
import { WorkingDayRoutingResolveService } from './working-day-routing-resolve.service';

const workingDayRoute: Routes = [
  {
    path: '',
    component: WorkingDayComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WorkingDayDetailComponent,
    resolve: {
      workingDay: WorkingDayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WorkingDayUpdateComponent,
    resolve: {
      workingDay: WorkingDayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WorkingDayUpdateComponent,
    resolve: {
      workingDay: WorkingDayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(workingDayRoute)],
  exports: [RouterModule],
})
export class WorkingDayRoutingModule {}
