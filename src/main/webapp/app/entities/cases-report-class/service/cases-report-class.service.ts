import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICasesReportClass, getCasesReportClassIdentifier } from '../cases-report-class.model';

export type EntityResponseType = HttpResponse<ICasesReportClass>;
export type EntityArrayResponseType = HttpResponse<ICasesReportClass[]>;

@Injectable({ providedIn: 'root' })
export class CasesReportClassService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cases-report-classes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(casesReportClass: ICasesReportClass): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(casesReportClass);
    return this.http
      .post<ICasesReportClass>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(casesReportClass: ICasesReportClass): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(casesReportClass);
    return this.http
      .put<ICasesReportClass>(`${this.resourceUrl}/${getCasesReportClassIdentifier(casesReportClass) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(casesReportClass: ICasesReportClass): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(casesReportClass);
    return this.http
      .patch<ICasesReportClass>(`${this.resourceUrl}/${getCasesReportClassIdentifier(casesReportClass) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICasesReportClass>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICasesReportClass[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCasesReportClassToCollectionIfMissing(
    casesReportClassCollection: ICasesReportClass[],
    ...casesReportClassesToCheck: (ICasesReportClass | null | undefined)[]
  ): ICasesReportClass[] {
    const casesReportClasses: ICasesReportClass[] = casesReportClassesToCheck.filter(isPresent);
    if (casesReportClasses.length > 0) {
      const casesReportClassCollectionIdentifiers = casesReportClassCollection.map(
        casesReportClassItem => getCasesReportClassIdentifier(casesReportClassItem)!
      );
      const casesReportClassesToAdd = casesReportClasses.filter(casesReportClassItem => {
        const casesReportClassIdentifier = getCasesReportClassIdentifier(casesReportClassItem);
        if (casesReportClassIdentifier == null || casesReportClassCollectionIdentifiers.includes(casesReportClassIdentifier)) {
          return false;
        }
        casesReportClassCollectionIdentifiers.push(casesReportClassIdentifier);
        return true;
      });
      return [...casesReportClassesToAdd, ...casesReportClassCollection];
    }
    return casesReportClassCollection;
  }

  protected convertDateFromClient(casesReportClass: ICasesReportClass): ICasesReportClass {
    return Object.assign({}, casesReportClass, {
      created: casesReportClass.created?.isValid() ? casesReportClass.created.toJSON() : undefined,
      lastUpdated: casesReportClass.lastUpdated?.isValid() ? casesReportClass.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((casesReportClass: ICasesReportClass) => {
        casesReportClass.created = casesReportClass.created ? dayjs(casesReportClass.created) : undefined;
        casesReportClass.lastUpdated = casesReportClass.lastUpdated ? dayjs(casesReportClass.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
