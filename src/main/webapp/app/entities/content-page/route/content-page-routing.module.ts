import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContentPageComponent } from '../list/content-page.component';
import { ContentPageDetailComponent } from '../detail/content-page-detail.component';
import { ContentPageUpdateComponent } from '../update/content-page-update.component';
import { ContentPageRoutingResolveService } from './content-page-routing-resolve.service';

const contentPageRoute: Routes = [
  {
    path: '',
    component: ContentPageComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContentPageDetailComponent,
    resolve: {
      contentPage: ContentPageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContentPageUpdateComponent,
    resolve: {
      contentPage: ContentPageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContentPageUpdateComponent,
    resolve: {
      contentPage: ContentPageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contentPageRoute)],
  exports: [RouterModule],
})
export class ContentPageRoutingModule {}
