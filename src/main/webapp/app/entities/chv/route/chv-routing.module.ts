import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CHVComponent } from '../list/chv.component';
import { CHVDetailComponent } from '../detail/chv-detail.component';
import { CHVUpdateComponent } from '../update/chv-update.component';
import { CHVRoutingResolveService } from './chv-routing-resolve.service';

const cHVRoute: Routes = [
  {
    path: '',
    component: CHVComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CHVDetailComponent,
    resolve: {
      cHV: CHVRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CHVUpdateComponent,
    resolve: {
      cHV: CHVRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CHVUpdateComponent,
    resolve: {
      cHV: CHVRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cHVRoute)],
  exports: [RouterModule],
})
export class CHVRoutingModule {}
