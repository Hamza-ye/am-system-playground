import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CHVTeamComponent } from '../list/chv-team.component';
import { CHVTeamDetailComponent } from '../detail/chv-team-detail.component';
import { CHVTeamUpdateComponent } from '../update/chv-team-update.component';
import { CHVTeamRoutingResolveService } from './chv-team-routing-resolve.service';

const cHVTeamRoute: Routes = [
  {
    path: '',
    component: CHVTeamComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CHVTeamDetailComponent,
    resolve: {
      cHVTeam: CHVTeamRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CHVTeamUpdateComponent,
    resolve: {
      cHVTeam: CHVTeamRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CHVTeamUpdateComponent,
    resolve: {
      cHVTeam: CHVTeamRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cHVTeamRoute)],
  exports: [RouterModule],
})
export class CHVTeamRoutingModule {}
