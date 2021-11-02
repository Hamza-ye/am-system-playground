import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DemographicDataSourceComponent } from '../list/demographic-data-source.component';
import { DemographicDataSourceDetailComponent } from '../detail/demographic-data-source-detail.component';
import { DemographicDataSourceUpdateComponent } from '../update/demographic-data-source-update.component';
import { DemographicDataSourceRoutingResolveService } from './demographic-data-source-routing-resolve.service';

const demographicDataSourceRoute: Routes = [
  {
    path: '',
    component: DemographicDataSourceComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DemographicDataSourceDetailComponent,
    resolve: {
      demographicDataSource: DemographicDataSourceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DemographicDataSourceUpdateComponent,
    resolve: {
      demographicDataSource: DemographicDataSourceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DemographicDataSourceUpdateComponent,
    resolve: {
      demographicDataSource: DemographicDataSourceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(demographicDataSourceRoute)],
  exports: [RouterModule],
})
export class DemographicDataSourceRoutingModule {}
