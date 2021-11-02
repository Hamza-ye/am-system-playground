import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICHVMalariaReportVersion1, getCHVMalariaReportVersion1Identifier } from '../chv-malaria-report-version-1.model';

export type EntityResponseType = HttpResponse<ICHVMalariaReportVersion1>;
export type EntityArrayResponseType = HttpResponse<ICHVMalariaReportVersion1[]>;

@Injectable({ providedIn: 'root' })
export class CHVMalariaReportVersion1Service {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/chv-malaria-report-version-1-s');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cHVMalariaReportVersion1: ICHVMalariaReportVersion1): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cHVMalariaReportVersion1);
    return this.http
      .post<ICHVMalariaReportVersion1>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(cHVMalariaReportVersion1: ICHVMalariaReportVersion1): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cHVMalariaReportVersion1);
    return this.http
      .put<ICHVMalariaReportVersion1>(
        `${this.resourceUrl}/${getCHVMalariaReportVersion1Identifier(cHVMalariaReportVersion1) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(cHVMalariaReportVersion1: ICHVMalariaReportVersion1): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cHVMalariaReportVersion1);
    return this.http
      .patch<ICHVMalariaReportVersion1>(
        `${this.resourceUrl}/${getCHVMalariaReportVersion1Identifier(cHVMalariaReportVersion1) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICHVMalariaReportVersion1>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICHVMalariaReportVersion1[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCHVMalariaReportVersion1ToCollectionIfMissing(
    cHVMalariaReportVersion1Collection: ICHVMalariaReportVersion1[],
    ...cHVMalariaReportVersion1sToCheck: (ICHVMalariaReportVersion1 | null | undefined)[]
  ): ICHVMalariaReportVersion1[] {
    const cHVMalariaReportVersion1s: ICHVMalariaReportVersion1[] = cHVMalariaReportVersion1sToCheck.filter(isPresent);
    if (cHVMalariaReportVersion1s.length > 0) {
      const cHVMalariaReportVersion1CollectionIdentifiers = cHVMalariaReportVersion1Collection.map(
        cHVMalariaReportVersion1Item => getCHVMalariaReportVersion1Identifier(cHVMalariaReportVersion1Item)!
      );
      const cHVMalariaReportVersion1sToAdd = cHVMalariaReportVersion1s.filter(cHVMalariaReportVersion1Item => {
        const cHVMalariaReportVersion1Identifier = getCHVMalariaReportVersion1Identifier(cHVMalariaReportVersion1Item);
        if (
          cHVMalariaReportVersion1Identifier == null ||
          cHVMalariaReportVersion1CollectionIdentifiers.includes(cHVMalariaReportVersion1Identifier)
        ) {
          return false;
        }
        cHVMalariaReportVersion1CollectionIdentifiers.push(cHVMalariaReportVersion1Identifier);
        return true;
      });
      return [...cHVMalariaReportVersion1sToAdd, ...cHVMalariaReportVersion1Collection];
    }
    return cHVMalariaReportVersion1Collection;
  }

  protected convertDateFromClient(cHVMalariaReportVersion1: ICHVMalariaReportVersion1): ICHVMalariaReportVersion1 {
    return Object.assign({}, cHVMalariaReportVersion1, {
      created: cHVMalariaReportVersion1.created?.isValid() ? cHVMalariaReportVersion1.created.toJSON() : undefined,
      lastUpdated: cHVMalariaReportVersion1.lastUpdated?.isValid() ? cHVMalariaReportVersion1.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((cHVMalariaReportVersion1: ICHVMalariaReportVersion1) => {
        cHVMalariaReportVersion1.created = cHVMalariaReportVersion1.created ? dayjs(cHVMalariaReportVersion1.created) : undefined;
        cHVMalariaReportVersion1.lastUpdated = cHVMalariaReportVersion1.lastUpdated
          ? dayjs(cHVMalariaReportVersion1.lastUpdated)
          : undefined;
      });
    }
    return res;
  }
}
