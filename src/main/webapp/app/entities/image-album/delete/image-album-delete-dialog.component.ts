import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IImageAlbum } from '../image-album.model';
import { ImageAlbumService } from '../service/image-album.service';

@Component({
  templateUrl: './image-album-delete-dialog.component.html',
})
export class ImageAlbumDeleteDialogComponent {
  imageAlbum?: IImageAlbum;

  constructor(protected imageAlbumService: ImageAlbumService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.imageAlbumService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
