import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProjectPageComponent } from '../list/project-page.component';
import { ProjectPageDetailComponent } from '../detail/project-page-detail.component';
import { ProjectPageRoutingResolveService } from './project-page-routing-resolve.service';

const projectRoute: Routes = [
  {
    path: '',
    component: ProjectPageComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProjectPageDetailComponent,
    resolve: {
      project: ProjectPageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(projectRoute)],
  exports: [RouterModule],
})
export class ProjectPageRoutingModule {}
