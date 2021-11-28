import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ImageAlbumComponent } from './list/image-album.component';
import { ImageAlbumDetailComponent } from './detail/image-album-detail.component';
import { ImageAlbumUpdateComponent } from './update/image-album-update.component';
import { ImageAlbumDeleteDialogComponent } from './delete/image-album-delete-dialog.component';
import { ImageAlbumRoutingModule } from './route/image-album-routing.module';

@NgModule({
  imports: [SharedModule, ImageAlbumRoutingModule],
  declarations: [ImageAlbumComponent, ImageAlbumDetailComponent, ImageAlbumUpdateComponent, ImageAlbumDeleteDialogComponent],
  entryComponents: [ImageAlbumDeleteDialogComponent],
})
export class ImageAlbumModule {}
