import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILLINSVillageReportHistory, getLLINSVillageReportHistoryIdentifier } from '../llins-village-report-history.model';

export type EntityResponseType = HttpResponse<ILLINSVillageReportHistory>;
export type EntityArrayResponseType = HttpResponse<ILLINSVillageReportHistory[]>;

@Injectable({ providedIn: 'root' })
export class LLINSVillageReportHistoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/llins-village-report-histories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(lLINSVillageReportHistory: ILLINSVillageReportHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lLINSVillageReportHistory);
    return this.http
      .post<ILLINSVillageReportHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(lLINSVillageReportHistory: ILLINSVillageReportHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lLINSVillageReportHistory);
    return this.http
      .put<ILLINSVillageReportHistory>(
        `${this.resourceUrl}/${getLLINSVillageReportHistoryIdentifier(lLINSVillageReportHistory) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(lLINSVillageReportHistory: ILLINSVillageReportHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lLINSVillageReportHistory);
    return this.http
      .patch<ILLINSVillageReportHistory>(
        `${this.resourceUrl}/${getLLINSVillageReportHistoryIdentifier(lLINSVillageReportHistory) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILLINSVillageReportHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILLINSVillageReportHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLLINSVillageReportHistoryToCollectionIfMissing(
    lLINSVillageReportHistoryCollection: ILLINSVillageReportHistory[],
    ...lLINSVillageReportHistoriesToCheck: (ILLINSVillageReportHistory | null | undefined)[]
  ): ILLINSVillageReportHistory[] {
    const lLINSVillageReportHistories: ILLINSVillageReportHistory[] = lLINSVillageReportHistoriesToCheck.filter(isPresent);
    if (lLINSVillageReportHistories.length > 0) {
      const lLINSVillageReportHistoryCollectionIdentifiers = lLINSVillageReportHistoryCollection.map(
        lLINSVillageReportHistoryItem => getLLINSVillageReportHistoryIdentifier(lLINSVillageReportHistoryItem)!
      );
      const lLINSVillageReportHistoriesToAdd = lLINSVillageReportHistories.filter(lLINSVillageReportHistoryItem => {
        const lLINSVillageReportHistoryIdentifier = getLLINSVillageReportHistoryIdentifier(lLINSVillageReportHistoryItem);
        if (
          lLINSVillageReportHistoryIdentifier == null ||
          lLINSVillageReportHistoryCollectionIdentifiers.includes(lLINSVillageReportHistoryIdentifier)
        ) {
          return false;
        }
        lLINSVillageReportHistoryCollectionIdentifiers.push(lLINSVillageReportHistoryIdentifier);
        return true;
      });
      return [...lLINSVillageReportHistoriesToAdd, ...lLINSVillageReportHistoryCollection];
    }
    return lLINSVillageReportHistoryCollection;
  }

  protected convertDateFromClient(lLINSVillageReportHistory: ILLINSVillageReportHistory): ILLINSVillageReportHistory {
    return Object.assign({}, lLINSVillageReportHistory, {
      created: lLINSVillageReportHistory.created?.isValid() ? lLINSVillageReportHistory.created.toJSON() : undefined,
      lastUpdated: lLINSVillageReportHistory.lastUpdated?.isValid() ? lLINSVillageReportHistory.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((lLINSVillageReportHistory: ILLINSVillageReportHistory) => {
        lLINSVillageReportHistory.created = lLINSVillageReportHistory.created ? dayjs(lLINSVillageReportHistory.created) : undefined;
        lLINSVillageReportHistory.lastUpdated = lLINSVillageReportHistory.lastUpdated
          ? dayjs(lLINSVillageReportHistory.lastUpdated)
          : undefined;
      });
    }
    return res;
  }
}
