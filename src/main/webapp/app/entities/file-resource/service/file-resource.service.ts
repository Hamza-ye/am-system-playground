import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFileResource, getFileResourceIdentifier } from '../file-resource.model';

export type EntityResponseType = HttpResponse<IFileResource>;
export type EntityArrayResponseType = HttpResponse<IFileResource[]>;

@Injectable({ providedIn: 'root' })
export class FileResourceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/file-resources');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fileResource: IFileResource): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fileResource);
    return this.http
      .post<IFileResource>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(fileResource: IFileResource): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fileResource);
    return this.http
      .put<IFileResource>(`${this.resourceUrl}/${getFileResourceIdentifier(fileResource) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(fileResource: IFileResource): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fileResource);
    return this.http
      .patch<IFileResource>(`${this.resourceUrl}/${getFileResourceIdentifier(fileResource) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFileResource>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFileResource[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFileResourceToCollectionIfMissing(
    fileResourceCollection: IFileResource[],
    ...fileResourcesToCheck: (IFileResource | null | undefined)[]
  ): IFileResource[] {
    const fileResources: IFileResource[] = fileResourcesToCheck.filter(isPresent);
    if (fileResources.length > 0) {
      const fileResourceCollectionIdentifiers = fileResourceCollection.map(
        fileResourceItem => getFileResourceIdentifier(fileResourceItem)!
      );
      const fileResourcesToAdd = fileResources.filter(fileResourceItem => {
        const fileResourceIdentifier = getFileResourceIdentifier(fileResourceItem);
        if (fileResourceIdentifier == null || fileResourceCollectionIdentifiers.includes(fileResourceIdentifier)) {
          return false;
        }
        fileResourceCollectionIdentifiers.push(fileResourceIdentifier);
        return true;
      });
      return [...fileResourcesToAdd, ...fileResourceCollection];
    }
    return fileResourceCollection;
  }

  protected convertDateFromClient(fileResource: IFileResource): IFileResource {
    return Object.assign({}, fileResource, {
      created: fileResource.created?.isValid() ? fileResource.created.toJSON() : undefined,
      lastUpdated: fileResource.lastUpdated?.isValid() ? fileResource.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((fileResource: IFileResource) => {
        fileResource.created = fileResource.created ? dayjs(fileResource.created) : undefined;
        fileResource.lastUpdated = fileResource.lastUpdated ? dayjs(fileResource.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
