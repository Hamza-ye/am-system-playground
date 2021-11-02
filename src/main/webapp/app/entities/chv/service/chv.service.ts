import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICHV, getCHVIdentifier } from '../chv.model';

export type EntityResponseType = HttpResponse<ICHV>;
export type EntityArrayResponseType = HttpResponse<ICHV[]>;

@Injectable({ providedIn: 'root' })
export class CHVService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/chvs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cHV: ICHV): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cHV);
    return this.http
      .post<ICHV>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(cHV: ICHV): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cHV);
    return this.http
      .put<ICHV>(`${this.resourceUrl}/${getCHVIdentifier(cHV) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(cHV: ICHV): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cHV);
    return this.http
      .patch<ICHV>(`${this.resourceUrl}/${getCHVIdentifier(cHV) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICHV>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICHV[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCHVToCollectionIfMissing(cHVCollection: ICHV[], ...cHVSToCheck: (ICHV | null | undefined)[]): ICHV[] {
    const cHVS: ICHV[] = cHVSToCheck.filter(isPresent);
    if (cHVS.length > 0) {
      const cHVCollectionIdentifiers = cHVCollection.map(cHVItem => getCHVIdentifier(cHVItem)!);
      const cHVSToAdd = cHVS.filter(cHVItem => {
        const cHVIdentifier = getCHVIdentifier(cHVItem);
        if (cHVIdentifier == null || cHVCollectionIdentifiers.includes(cHVIdentifier)) {
          return false;
        }
        cHVCollectionIdentifiers.push(cHVIdentifier);
        return true;
      });
      return [...cHVSToAdd, ...cHVCollection];
    }
    return cHVCollection;
  }

  protected convertDateFromClient(cHV: ICHV): ICHV {
    return Object.assign({}, cHV, {
      created: cHV.created?.isValid() ? cHV.created.toJSON() : undefined,
      lastUpdated: cHV.lastUpdated?.isValid() ? cHV.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((cHV: ICHV) => {
        cHV.created = cHV.created ? dayjs(cHV.created) : undefined;
        cHV.lastUpdated = cHV.lastUpdated ? dayjs(cHV.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
