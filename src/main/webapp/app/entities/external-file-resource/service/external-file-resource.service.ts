import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExternalFileResource, getExternalFileResourceIdentifier } from '../external-file-resource.model';

export type EntityResponseType = HttpResponse<IExternalFileResource>;
export type EntityArrayResponseType = HttpResponse<IExternalFileResource[]>;

@Injectable({ providedIn: 'root' })
export class ExternalFileResourceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/external-file-resources');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(externalFileResource: IExternalFileResource): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(externalFileResource);
    return this.http
      .post<IExternalFileResource>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(externalFileResource: IExternalFileResource): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(externalFileResource);
    return this.http
      .put<IExternalFileResource>(`${this.resourceUrl}/${getExternalFileResourceIdentifier(externalFileResource) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(externalFileResource: IExternalFileResource): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(externalFileResource);
    return this.http
      .patch<IExternalFileResource>(`${this.resourceUrl}/${getExternalFileResourceIdentifier(externalFileResource) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IExternalFileResource>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IExternalFileResource[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addExternalFileResourceToCollectionIfMissing(
    externalFileResourceCollection: IExternalFileResource[],
    ...externalFileResourcesToCheck: (IExternalFileResource | null | undefined)[]
  ): IExternalFileResource[] {
    const externalFileResources: IExternalFileResource[] = externalFileResourcesToCheck.filter(isPresent);
    if (externalFileResources.length > 0) {
      const externalFileResourceCollectionIdentifiers = externalFileResourceCollection.map(
        externalFileResourceItem => getExternalFileResourceIdentifier(externalFileResourceItem)!
      );
      const externalFileResourcesToAdd = externalFileResources.filter(externalFileResourceItem => {
        const externalFileResourceIdentifier = getExternalFileResourceIdentifier(externalFileResourceItem);
        if (externalFileResourceIdentifier == null || externalFileResourceCollectionIdentifiers.includes(externalFileResourceIdentifier)) {
          return false;
        }
        externalFileResourceCollectionIdentifiers.push(externalFileResourceIdentifier);
        return true;
      });
      return [...externalFileResourcesToAdd, ...externalFileResourceCollection];
    }
    return externalFileResourceCollection;
  }

  protected convertDateFromClient(externalFileResource: IExternalFileResource): IExternalFileResource {
    return Object.assign({}, externalFileResource, {
      created: externalFileResource.created?.isValid() ? externalFileResource.created.toJSON() : undefined,
      lastUpdated: externalFileResource.lastUpdated?.isValid() ? externalFileResource.lastUpdated.toJSON() : undefined,
      expires: externalFileResource.expires?.isValid() ? externalFileResource.expires.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.created = res.body.created ? dayjs(res.body.created) : undefined;
      res.body.lastUpdated = res.body.lastUpdated ? dayjs(res.body.lastUpdated) : undefined;
      res.body.expires = res.body.expires ? dayjs(res.body.expires) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((externalFileResource: IExternalFileResource) => {
        externalFileResource.created = externalFileResource.created ? dayjs(externalFileResource.created) : undefined;
        externalFileResource.lastUpdated = externalFileResource.lastUpdated ? dayjs(externalFileResource.lastUpdated) : undefined;
        externalFileResource.expires = externalFileResource.expires ? dayjs(externalFileResource.expires) : undefined;
      });
    }
    return res;
  }
}
