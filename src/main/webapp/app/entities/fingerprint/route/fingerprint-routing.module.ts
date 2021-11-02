import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FingerprintComponent } from '../list/fingerprint.component';
import { FingerprintDetailComponent } from '../detail/fingerprint-detail.component';
import { FingerprintUpdateComponent } from '../update/fingerprint-update.component';
import { FingerprintRoutingResolveService } from './fingerprint-routing-resolve.service';

const fingerprintRoute: Routes = [
  {
    path: '',
    component: FingerprintComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FingerprintDetailComponent,
    resolve: {
      fingerprint: FingerprintRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FingerprintUpdateComponent,
    resolve: {
      fingerprint: FingerprintRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FingerprintUpdateComponent,
    resolve: {
      fingerprint: FingerprintRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fingerprintRoute)],
  exports: [RouterModule],
})
export class FingerprintRoutingModule {}
