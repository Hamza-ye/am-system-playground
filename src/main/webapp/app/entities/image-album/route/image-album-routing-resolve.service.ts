import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IImageAlbum, ImageAlbum } from '../image-album.model';
import { ImageAlbumService } from '../service/image-album.service';

@Injectable({ providedIn: 'root' })
export class ImageAlbumRoutingResolveService implements Resolve<IImageAlbum> {
  constructor(protected service: ImageAlbumService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IImageAlbum> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((imageAlbum: HttpResponse<ImageAlbum>) => {
          if (imageAlbum.body) {
            return of(imageAlbum.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ImageAlbum());
  }
}
