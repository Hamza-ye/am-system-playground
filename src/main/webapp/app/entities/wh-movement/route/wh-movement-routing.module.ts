import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WHMovementComponent } from '../list/wh-movement.component';
import { WHMovementDetailComponent } from '../detail/wh-movement-detail.component';
import { WHMovementUpdateComponent } from '../update/wh-movement-update.component';
import { WHMovementRoutingResolveService } from './wh-movement-routing-resolve.service';

const wHMovementRoute: Routes = [
  {
    path: '',
    component: WHMovementComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WHMovementDetailComponent,
    resolve: {
      wHMovement: WHMovementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WHMovementUpdateComponent,
    resolve: {
      wHMovement: WHMovementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WHMovementUpdateComponent,
    resolve: {
      wHMovement: WHMovementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(wHMovementRoute)],
  exports: [RouterModule],
})
export class WHMovementRoutingModule {}
