import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WhMovementComponent } from '../list/wh-movement.component';
import { WhMovementDetailComponent } from '../detail/wh-movement-detail.component';
import { WhMovementUpdateComponent } from '../update/wh-movement-update.component';
import { WhMovementRoutingResolveService } from './wh-movement-routing-resolve.service';

const whMovementRoute: Routes = [
  {
    path: '',
    component: WhMovementComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WhMovementDetailComponent,
    resolve: {
      whMovement: WhMovementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WhMovementUpdateComponent,
    resolve: {
      whMovement: WhMovementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WhMovementUpdateComponent,
    resolve: {
      whMovement: WhMovementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(whMovementRoute)],
  exports: [RouterModule],
})
export class WhMovementRoutingModule {}
