import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IImageAlbum } from '../image-album.model';

@Component({
  selector: 'app-image-album-detail',
  templateUrl: './image-album-detail.component.html',
})
export class ImageAlbumDetailComponent implements OnInit {
  imageAlbum: IImageAlbum | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ imageAlbum }) => {
      this.imageAlbum = imageAlbum;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
