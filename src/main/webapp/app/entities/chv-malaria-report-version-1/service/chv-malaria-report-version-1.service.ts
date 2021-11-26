import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IChvMalariaReportVersion1, getChvMalariaReportVersion1Identifier } from '../chv-malaria-report-version-1.model';

export type EntityResponseType = HttpResponse<IChvMalariaReportVersion1>;
export type EntityArrayResponseType = HttpResponse<IChvMalariaReportVersion1[]>;

@Injectable({ providedIn: 'root' })
export class ChvMalariaReportVersion1Service {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/chv-malaria-report-version-1-s');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(chvMalariaReportVersion1: IChvMalariaReportVersion1): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chvMalariaReportVersion1);
    return this.http
      .post<IChvMalariaReportVersion1>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(chvMalariaReportVersion1: IChvMalariaReportVersion1): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chvMalariaReportVersion1);
    return this.http
      .put<IChvMalariaReportVersion1>(
        `${this.resourceUrl}/${getChvMalariaReportVersion1Identifier(chvMalariaReportVersion1) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(chvMalariaReportVersion1: IChvMalariaReportVersion1): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chvMalariaReportVersion1);
    return this.http
      .patch<IChvMalariaReportVersion1>(
        `${this.resourceUrl}/${getChvMalariaReportVersion1Identifier(chvMalariaReportVersion1) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IChvMalariaReportVersion1>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IChvMalariaReportVersion1[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addChvMalariaReportVersion1ToCollectionIfMissing(
    chvMalariaReportVersion1Collection: IChvMalariaReportVersion1[],
    ...chvMalariaReportVersion1sToCheck: (IChvMalariaReportVersion1 | null | undefined)[]
  ): IChvMalariaReportVersion1[] {
    const chvMalariaReportVersion1s: IChvMalariaReportVersion1[] = chvMalariaReportVersion1sToCheck.filter(isPresent);
    if (chvMalariaReportVersion1s.length > 0) {
      const chvMalariaReportVersion1CollectionIdentifiers = chvMalariaReportVersion1Collection.map(
        chvMalariaReportVersion1Item => getChvMalariaReportVersion1Identifier(chvMalariaReportVersion1Item)!
      );
      const chvMalariaReportVersion1sToAdd = chvMalariaReportVersion1s.filter(chvMalariaReportVersion1Item => {
        const chvMalariaReportVersion1Identifier = getChvMalariaReportVersion1Identifier(chvMalariaReportVersion1Item);
        if (
          chvMalariaReportVersion1Identifier == null ||
          chvMalariaReportVersion1CollectionIdentifiers.includes(chvMalariaReportVersion1Identifier)
        ) {
          return false;
        }
        chvMalariaReportVersion1CollectionIdentifiers.push(chvMalariaReportVersion1Identifier);
        return true;
      });
      return [...chvMalariaReportVersion1sToAdd, ...chvMalariaReportVersion1Collection];
    }
    return chvMalariaReportVersion1Collection;
  }

  protected convertDateFromClient(chvMalariaReportVersion1: IChvMalariaReportVersion1): IChvMalariaReportVersion1 {
    return Object.assign({}, chvMalariaReportVersion1, {
      created: chvMalariaReportVersion1.created?.isValid() ? chvMalariaReportVersion1.created.toJSON() : undefined,
      lastUpdated: chvMalariaReportVersion1.lastUpdated?.isValid() ? chvMalariaReportVersion1.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((chvMalariaReportVersion1: IChvMalariaReportVersion1) => {
        chvMalariaReportVersion1.created = chvMalariaReportVersion1.created ? dayjs(chvMalariaReportVersion1.created) : undefined;
        chvMalariaReportVersion1.lastUpdated = chvMalariaReportVersion1.lastUpdated
          ? dayjs(chvMalariaReportVersion1.lastUpdated)
          : undefined;
      });
    }
    return res;
  }
}
