import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DataSetComponent } from '../list/data-set.component';
import { DataSetDetailComponent } from '../detail/data-set-detail.component';
import { DataSetUpdateComponent } from '../update/data-set-update.component';
import { DataSetRoutingResolveService } from './data-set-routing-resolve.service';

const dataSetRoute: Routes = [
  {
    path: '',
    component: DataSetComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DataSetDetailComponent,
    resolve: {
      dataSet: DataSetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DataSetUpdateComponent,
    resolve: {
      dataSet: DataSetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DataSetUpdateComponent,
    resolve: {
      dataSet: DataSetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dataSetRoute)],
  exports: [RouterModule],
})
export class DataSetRoutingModule {}
