import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IImageAlbum } from '../image-album.model';
import { ImageAlbumService } from '../service/image-album.service';
import { ImageAlbumDeleteDialogComponent } from '../delete/image-album-delete-dialog.component';

@Component({
  selector: 'app-image-album',
  templateUrl: './image-album.component.html',
})
export class ImageAlbumComponent implements OnInit {
  imageAlbums?: IImageAlbum[];
  isLoading = false;

  constructor(protected imageAlbumService: ImageAlbumService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.imageAlbumService.query().subscribe(
      (res: HttpResponse<IImageAlbum[]>) => {
        this.isLoading = false;
        this.imageAlbums = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IImageAlbum): number {
    return item.id!;
  }

  delete(imageAlbum: IImageAlbum): void {
    const modalRef = this.modalService.open(ImageAlbumDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.imageAlbum = imageAlbum;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
