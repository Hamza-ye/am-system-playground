import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ImageAlbumComponent } from '../list/image-album.component';
import { ImageAlbumDetailComponent } from '../detail/image-album-detail.component';
import { ImageAlbumUpdateComponent } from '../update/image-album-update.component';
import { ImageAlbumRoutingResolveService } from './image-album-routing-resolve.service';

const imageAlbumRoute: Routes = [
  {
    path: '',
    component: ImageAlbumComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ImageAlbumDetailComponent,
    resolve: {
      imageAlbum: ImageAlbumRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ImageAlbumUpdateComponent,
    resolve: {
      imageAlbum: ImageAlbumRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ImageAlbumUpdateComponent,
    resolve: {
      imageAlbum: ImageAlbumRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(imageAlbumRoute)],
  exports: [RouterModule],
})
export class ImageAlbumRoutingModule {}
