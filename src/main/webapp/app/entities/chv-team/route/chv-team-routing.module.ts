import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ChvTeamComponent } from '../list/chv-team.component';
import { ChvTeamDetailComponent } from '../detail/chv-team-detail.component';
import { ChvTeamUpdateComponent } from '../update/chv-team-update.component';
import { ChvTeamRoutingResolveService } from './chv-team-routing-resolve.service';

const chvTeamRoute: Routes = [
  {
    path: '',
    component: ChvTeamComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ChvTeamDetailComponent,
    resolve: {
      chvTeam: ChvTeamRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ChvTeamUpdateComponent,
    resolve: {
      chvTeam: ChvTeamRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ChvTeamUpdateComponent,
    resolve: {
      chvTeam: ChvTeamRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(chvTeamRoute)],
  exports: [RouterModule],
})
export class ChvTeamRoutingModule {}
