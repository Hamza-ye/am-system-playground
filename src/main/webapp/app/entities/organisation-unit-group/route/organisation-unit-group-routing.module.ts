import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OrganisationUnitGroupComponent } from '../list/organisation-unit-group.component';
import { OrganisationUnitGroupDetailComponent } from '../detail/organisation-unit-group-detail.component';
import { OrganisationUnitGroupUpdateComponent } from '../update/organisation-unit-group-update.component';
import { OrganisationUnitGroupRoutingResolveService } from './organisation-unit-group-routing-resolve.service';

const organisationUnitGroupRoute: Routes = [
  {
    path: '',
    component: OrganisationUnitGroupComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrganisationUnitGroupDetailComponent,
    resolve: {
      organisationUnitGroup: OrganisationUnitGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrganisationUnitGroupUpdateComponent,
    resolve: {
      organisationUnitGroup: OrganisationUnitGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrganisationUnitGroupUpdateComponent,
    resolve: {
      organisationUnitGroup: OrganisationUnitGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(organisationUnitGroupRoute)],
  exports: [RouterModule],
})
export class OrganisationUnitGroupRoutingModule {}
