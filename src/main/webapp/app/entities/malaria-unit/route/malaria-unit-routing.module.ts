import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MalariaUnitComponent } from '../list/malaria-unit.component';
import { MalariaUnitDetailComponent } from '../detail/malaria-unit-detail.component';
import { MalariaUnitUpdateComponent } from '../update/malaria-unit-update.component';
import { MalariaUnitRoutingResolveService } from './malaria-unit-routing-resolve.service';

const malariaUnitRoute: Routes = [
  {
    path: '',
    component: MalariaUnitComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MalariaUnitDetailComponent,
    resolve: {
      malariaUnit: MalariaUnitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MalariaUnitUpdateComponent,
    resolve: {
      malariaUnit: MalariaUnitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MalariaUnitUpdateComponent,
    resolve: {
      malariaUnit: MalariaUnitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(malariaUnitRoute)],
  exports: [RouterModule],
})
export class MalariaUnitRoutingModule {}
