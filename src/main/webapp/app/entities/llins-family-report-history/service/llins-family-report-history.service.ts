import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILLINSFamilyReportHistory, getLLINSFamilyReportHistoryIdentifier } from '../llins-family-report-history.model';

export type EntityResponseType = HttpResponse<ILLINSFamilyReportHistory>;
export type EntityArrayResponseType = HttpResponse<ILLINSFamilyReportHistory[]>;

@Injectable({ providedIn: 'root' })
export class LLINSFamilyReportHistoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/llins-family-report-histories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(lLINSFamilyReportHistory: ILLINSFamilyReportHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lLINSFamilyReportHistory);
    return this.http
      .post<ILLINSFamilyReportHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(lLINSFamilyReportHistory: ILLINSFamilyReportHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lLINSFamilyReportHistory);
    return this.http
      .put<ILLINSFamilyReportHistory>(
        `${this.resourceUrl}/${getLLINSFamilyReportHistoryIdentifier(lLINSFamilyReportHistory) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(lLINSFamilyReportHistory: ILLINSFamilyReportHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lLINSFamilyReportHistory);
    return this.http
      .patch<ILLINSFamilyReportHistory>(
        `${this.resourceUrl}/${getLLINSFamilyReportHistoryIdentifier(lLINSFamilyReportHistory) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILLINSFamilyReportHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILLINSFamilyReportHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLLINSFamilyReportHistoryToCollectionIfMissing(
    lLINSFamilyReportHistoryCollection: ILLINSFamilyReportHistory[],
    ...lLINSFamilyReportHistoriesToCheck: (ILLINSFamilyReportHistory | null | undefined)[]
  ): ILLINSFamilyReportHistory[] {
    const lLINSFamilyReportHistories: ILLINSFamilyReportHistory[] = lLINSFamilyReportHistoriesToCheck.filter(isPresent);
    if (lLINSFamilyReportHistories.length > 0) {
      const lLINSFamilyReportHistoryCollectionIdentifiers = lLINSFamilyReportHistoryCollection.map(
        lLINSFamilyReportHistoryItem => getLLINSFamilyReportHistoryIdentifier(lLINSFamilyReportHistoryItem)!
      );
      const lLINSFamilyReportHistoriesToAdd = lLINSFamilyReportHistories.filter(lLINSFamilyReportHistoryItem => {
        const lLINSFamilyReportHistoryIdentifier = getLLINSFamilyReportHistoryIdentifier(lLINSFamilyReportHistoryItem);
        if (
          lLINSFamilyReportHistoryIdentifier == null ||
          lLINSFamilyReportHistoryCollectionIdentifiers.includes(lLINSFamilyReportHistoryIdentifier)
        ) {
          return false;
        }
        lLINSFamilyReportHistoryCollectionIdentifiers.push(lLINSFamilyReportHistoryIdentifier);
        return true;
      });
      return [...lLINSFamilyReportHistoriesToAdd, ...lLINSFamilyReportHistoryCollection];
    }
    return lLINSFamilyReportHistoryCollection;
  }

  protected convertDateFromClient(lLINSFamilyReportHistory: ILLINSFamilyReportHistory): ILLINSFamilyReportHistory {
    return Object.assign({}, lLINSFamilyReportHistory, {
      created: lLINSFamilyReportHistory.created?.isValid() ? lLINSFamilyReportHistory.created.toJSON() : undefined,
      lastUpdated: lLINSFamilyReportHistory.lastUpdated?.isValid() ? lLINSFamilyReportHistory.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((lLINSFamilyReportHistory: ILLINSFamilyReportHistory) => {
        lLINSFamilyReportHistory.created = lLINSFamilyReportHistory.created ? dayjs(lLINSFamilyReportHistory.created) : undefined;
        lLINSFamilyReportHistory.lastUpdated = lLINSFamilyReportHistory.lastUpdated
          ? dayjs(lLINSFamilyReportHistory.lastUpdated)
          : undefined;
      });
    }
    return res;
  }
}
