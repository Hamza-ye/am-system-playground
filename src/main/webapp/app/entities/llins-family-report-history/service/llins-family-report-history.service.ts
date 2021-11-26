import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILlinsFamilyReportHistory, getLlinsFamilyReportHistoryIdentifier } from '../llins-family-report-history.model';

export type EntityResponseType = HttpResponse<ILlinsFamilyReportHistory>;
export type EntityArrayResponseType = HttpResponse<ILlinsFamilyReportHistory[]>;

@Injectable({ providedIn: 'root' })
export class LlinsFamilyReportHistoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/llins-family-report-histories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(llinsFamilyReportHistory: ILlinsFamilyReportHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(llinsFamilyReportHistory);
    return this.http
      .post<ILlinsFamilyReportHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(llinsFamilyReportHistory: ILlinsFamilyReportHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(llinsFamilyReportHistory);
    return this.http
      .put<ILlinsFamilyReportHistory>(
        `${this.resourceUrl}/${getLlinsFamilyReportHistoryIdentifier(llinsFamilyReportHistory) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(llinsFamilyReportHistory: ILlinsFamilyReportHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(llinsFamilyReportHistory);
    return this.http
      .patch<ILlinsFamilyReportHistory>(
        `${this.resourceUrl}/${getLlinsFamilyReportHistoryIdentifier(llinsFamilyReportHistory) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILlinsFamilyReportHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILlinsFamilyReportHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLlinsFamilyReportHistoryToCollectionIfMissing(
    llinsFamilyReportHistoryCollection: ILlinsFamilyReportHistory[],
    ...llinsFamilyReportHistoriesToCheck: (ILlinsFamilyReportHistory | null | undefined)[]
  ): ILlinsFamilyReportHistory[] {
    const llinsFamilyReportHistories: ILlinsFamilyReportHistory[] = llinsFamilyReportHistoriesToCheck.filter(isPresent);
    if (llinsFamilyReportHistories.length > 0) {
      const llinsFamilyReportHistoryCollectionIdentifiers = llinsFamilyReportHistoryCollection.map(
        llinsFamilyReportHistoryItem => getLlinsFamilyReportHistoryIdentifier(llinsFamilyReportHistoryItem)!
      );
      const llinsFamilyReportHistoriesToAdd = llinsFamilyReportHistories.filter(llinsFamilyReportHistoryItem => {
        const llinsFamilyReportHistoryIdentifier = getLlinsFamilyReportHistoryIdentifier(llinsFamilyReportHistoryItem);
        if (
          llinsFamilyReportHistoryIdentifier == null ||
          llinsFamilyReportHistoryCollectionIdentifiers.includes(llinsFamilyReportHistoryIdentifier)
        ) {
          return false;
        }
        llinsFamilyReportHistoryCollectionIdentifiers.push(llinsFamilyReportHistoryIdentifier);
        return true;
      });
      return [...llinsFamilyReportHistoriesToAdd, ...llinsFamilyReportHistoryCollection];
    }
    return llinsFamilyReportHistoryCollection;
  }

  protected convertDateFromClient(llinsFamilyReportHistory: ILlinsFamilyReportHistory): ILlinsFamilyReportHistory {
    return Object.assign({}, llinsFamilyReportHistory, {
      created: llinsFamilyReportHistory.created?.isValid() ? llinsFamilyReportHistory.created.toJSON() : undefined,
      lastUpdated: llinsFamilyReportHistory.lastUpdated?.isValid() ? llinsFamilyReportHistory.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((llinsFamilyReportHistory: ILlinsFamilyReportHistory) => {
        llinsFamilyReportHistory.created = llinsFamilyReportHistory.created ? dayjs(llinsFamilyReportHistory.created) : undefined;
        llinsFamilyReportHistory.lastUpdated = llinsFamilyReportHistory.lastUpdated
          ? dayjs(llinsFamilyReportHistory.lastUpdated)
          : undefined;
      });
    }
    return res;
  }
}
