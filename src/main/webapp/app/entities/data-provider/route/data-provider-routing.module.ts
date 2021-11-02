import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DataProviderComponent } from '../list/data-provider.component';
import { DataProviderDetailComponent } from '../detail/data-provider-detail.component';
import { DataProviderUpdateComponent } from '../update/data-provider-update.component';
import { DataProviderRoutingResolveService } from './data-provider-routing-resolve.service';

const dataProviderRoute: Routes = [
  {
    path: '',
    component: DataProviderComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DataProviderDetailComponent,
    resolve: {
      dataProvider: DataProviderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DataProviderUpdateComponent,
    resolve: {
      dataProvider: DataProviderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DataProviderUpdateComponent,
    resolve: {
      dataProvider: DataProviderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dataProviderRoute)],
  exports: [RouterModule],
})
export class DataProviderRoutingModule {}
