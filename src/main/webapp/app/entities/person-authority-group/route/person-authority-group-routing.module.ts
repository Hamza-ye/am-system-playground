import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PersonAuthorityGroupComponent } from '../list/person-authority-group.component';
import { PersonAuthorityGroupDetailComponent } from '../detail/person-authority-group-detail.component';
import { PersonAuthorityGroupUpdateComponent } from '../update/person-authority-group-update.component';
import { PersonAuthorityGroupRoutingResolveService } from './person-authority-group-routing-resolve.service';

const personAuthorityGroupRoute: Routes = [
  {
    path: '',
    component: PersonAuthorityGroupComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PersonAuthorityGroupDetailComponent,
    resolve: {
      personAuthorityGroup: PersonAuthorityGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PersonAuthorityGroupUpdateComponent,
    resolve: {
      personAuthorityGroup: PersonAuthorityGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PersonAuthorityGroupUpdateComponent,
    resolve: {
      personAuthorityGroup: PersonAuthorityGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(personAuthorityGroupRoute)],
  exports: [RouterModule],
})
export class PersonAuthorityGroupRoutingModule {}
