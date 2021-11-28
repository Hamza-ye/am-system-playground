import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RelatedLinkComponent } from '../list/related-link.component';
import { RelatedLinkDetailComponent } from '../detail/related-link-detail.component';
import { RelatedLinkUpdateComponent } from '../update/related-link-update.component';
import { RelatedLinkRoutingResolveService } from './related-link-routing-resolve.service';

const relatedLinkRoute: Routes = [
  {
    path: '',
    component: RelatedLinkComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RelatedLinkDetailComponent,
    resolve: {
      relatedLink: RelatedLinkRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RelatedLinkUpdateComponent,
    resolve: {
      relatedLink: RelatedLinkRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RelatedLinkUpdateComponent,
    resolve: {
      relatedLink: RelatedLinkRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(relatedLinkRoute)],
  exports: [RouterModule],
})
export class RelatedLinkRoutingModule {}
