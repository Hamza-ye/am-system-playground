import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IImageAlbum, getImageAlbumIdentifier } from '../image-album.model';

export type EntityResponseType = HttpResponse<IImageAlbum>;
export type EntityArrayResponseType = HttpResponse<IImageAlbum[]>;

@Injectable({ providedIn: 'root' })
export class ImageAlbumService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/image-albums');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(imageAlbum: IImageAlbum): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(imageAlbum);
    return this.http
      .post<IImageAlbum>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(imageAlbum: IImageAlbum): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(imageAlbum);
    return this.http
      .put<IImageAlbum>(`${this.resourceUrl}/${getImageAlbumIdentifier(imageAlbum) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(imageAlbum: IImageAlbum): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(imageAlbum);
    return this.http
      .patch<IImageAlbum>(`${this.resourceUrl}/${getImageAlbumIdentifier(imageAlbum) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IImageAlbum>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IImageAlbum[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addImageAlbumToCollectionIfMissing(
    imageAlbumCollection: IImageAlbum[],
    ...imageAlbumsToCheck: (IImageAlbum | null | undefined)[]
  ): IImageAlbum[] {
    const imageAlbums: IImageAlbum[] = imageAlbumsToCheck.filter(isPresent);
    if (imageAlbums.length > 0) {
      const imageAlbumCollectionIdentifiers = imageAlbumCollection.map(imageAlbumItem => getImageAlbumIdentifier(imageAlbumItem)!);
      const imageAlbumsToAdd = imageAlbums.filter(imageAlbumItem => {
        const imageAlbumIdentifier = getImageAlbumIdentifier(imageAlbumItem);
        if (imageAlbumIdentifier == null || imageAlbumCollectionIdentifiers.includes(imageAlbumIdentifier)) {
          return false;
        }
        imageAlbumCollectionIdentifiers.push(imageAlbumIdentifier);
        return true;
      });
      return [...imageAlbumsToAdd, ...imageAlbumCollection];
    }
    return imageAlbumCollection;
  }

  protected convertDateFromClient(imageAlbum: IImageAlbum): IImageAlbum {
    return Object.assign({}, imageAlbum, {
      created: imageAlbum.created?.isValid() ? imageAlbum.created.toJSON() : undefined,
      lastUpdated: imageAlbum.lastUpdated?.isValid() ? imageAlbum.lastUpdated.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.created = res.body.created ? dayjs(res.body.created) : undefined;
      res.body.lastUpdated = res.body.lastUpdated ? dayjs(res.body.lastUpdated) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((imageAlbum: IImageAlbum) => {
        imageAlbum.created = imageAlbum.created ? dayjs(imageAlbum.created) : undefined;
        imageAlbum.lastUpdated = imageAlbum.lastUpdated ? dayjs(imageAlbum.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
