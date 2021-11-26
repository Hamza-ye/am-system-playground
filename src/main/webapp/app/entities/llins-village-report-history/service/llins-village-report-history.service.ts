import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILlinsVillageReportHistory, getLlinsVillageReportHistoryIdentifier } from '../llins-village-report-history.model';

export type EntityResponseType = HttpResponse<ILlinsVillageReportHistory>;
export type EntityArrayResponseType = HttpResponse<ILlinsVillageReportHistory[]>;

@Injectable({ providedIn: 'root' })
export class LlinsVillageReportHistoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/llins-village-report-histories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(llinsVillageReportHistory: ILlinsVillageReportHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(llinsVillageReportHistory);
    return this.http
      .post<ILlinsVillageReportHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(llinsVillageReportHistory: ILlinsVillageReportHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(llinsVillageReportHistory);
    return this.http
      .put<ILlinsVillageReportHistory>(
        `${this.resourceUrl}/${getLlinsVillageReportHistoryIdentifier(llinsVillageReportHistory) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(llinsVillageReportHistory: ILlinsVillageReportHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(llinsVillageReportHistory);
    return this.http
      .patch<ILlinsVillageReportHistory>(
        `${this.resourceUrl}/${getLlinsVillageReportHistoryIdentifier(llinsVillageReportHistory) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILlinsVillageReportHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILlinsVillageReportHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLlinsVillageReportHistoryToCollectionIfMissing(
    llinsVillageReportHistoryCollection: ILlinsVillageReportHistory[],
    ...llinsVillageReportHistoriesToCheck: (ILlinsVillageReportHistory | null | undefined)[]
  ): ILlinsVillageReportHistory[] {
    const llinsVillageReportHistories: ILlinsVillageReportHistory[] = llinsVillageReportHistoriesToCheck.filter(isPresent);
    if (llinsVillageReportHistories.length > 0) {
      const llinsVillageReportHistoryCollectionIdentifiers = llinsVillageReportHistoryCollection.map(
        llinsVillageReportHistoryItem => getLlinsVillageReportHistoryIdentifier(llinsVillageReportHistoryItem)!
      );
      const llinsVillageReportHistoriesToAdd = llinsVillageReportHistories.filter(llinsVillageReportHistoryItem => {
        const llinsVillageReportHistoryIdentifier = getLlinsVillageReportHistoryIdentifier(llinsVillageReportHistoryItem);
        if (
          llinsVillageReportHistoryIdentifier == null ||
          llinsVillageReportHistoryCollectionIdentifiers.includes(llinsVillageReportHistoryIdentifier)
        ) {
          return false;
        }
        llinsVillageReportHistoryCollectionIdentifiers.push(llinsVillageReportHistoryIdentifier);
        return true;
      });
      return [...llinsVillageReportHistoriesToAdd, ...llinsVillageReportHistoryCollection];
    }
    return llinsVillageReportHistoryCollection;
  }

  protected convertDateFromClient(llinsVillageReportHistory: ILlinsVillageReportHistory): ILlinsVillageReportHistory {
    return Object.assign({}, llinsVillageReportHistory, {
      created: llinsVillageReportHistory.created?.isValid() ? llinsVillageReportHistory.created.toJSON() : undefined,
      lastUpdated: llinsVillageReportHistory.lastUpdated?.isValid() ? llinsVillageReportHistory.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((llinsVillageReportHistory: ILlinsVillageReportHistory) => {
        llinsVillageReportHistory.created = llinsVillageReportHistory.created ? dayjs(llinsVillageReportHistory.created) : undefined;
        llinsVillageReportHistory.lastUpdated = llinsVillageReportHistory.lastUpdated
          ? dayjs(llinsVillageReportHistory.lastUpdated)
          : undefined;
      });
    }
    return res;
  }
}
