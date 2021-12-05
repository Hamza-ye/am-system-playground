import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PeopleGroupComponent } from '../list/people-group.component';
import { PeopleGroupDetailComponent } from '../detail/people-group-detail.component';
import { PeopleGroupUpdateComponent } from '../update/people-group-update.component';
import { PeopleGroupRoutingResolveService } from './people-group-routing-resolve.service';

const peopleGroupRoute: Routes = [
  {
    path: '',
    component: PeopleGroupComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PeopleGroupDetailComponent,
    resolve: {
      peopleGroup: PeopleGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PeopleGroupUpdateComponent,
    resolve: {
      peopleGroup: PeopleGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PeopleGroupUpdateComponent,
    resolve: {
      peopleGroup: PeopleGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(peopleGroupRoute)],
  exports: [RouterModule],
})
export class PeopleGroupRoutingModule {}
