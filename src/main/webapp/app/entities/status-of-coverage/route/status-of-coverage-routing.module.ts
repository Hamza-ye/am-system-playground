import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StatusOfCoverageComponent } from '../list/status-of-coverage.component';
import { StatusOfCoverageDetailComponent } from '../detail/status-of-coverage-detail.component';
import { StatusOfCoverageUpdateComponent } from '../update/status-of-coverage-update.component';
import { StatusOfCoverageRoutingResolveService } from './status-of-coverage-routing-resolve.service';

const statusOfCoverageRoute: Routes = [
  {
    path: '',
    component: StatusOfCoverageComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StatusOfCoverageDetailComponent,
    resolve: {
      statusOfCoverage: StatusOfCoverageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StatusOfCoverageUpdateComponent,
    resolve: {
      statusOfCoverage: StatusOfCoverageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StatusOfCoverageUpdateComponent,
    resolve: {
      statusOfCoverage: StatusOfCoverageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(statusOfCoverageRoute)],
  exports: [RouterModule],
})
export class StatusOfCoverageRoutingModule {}
