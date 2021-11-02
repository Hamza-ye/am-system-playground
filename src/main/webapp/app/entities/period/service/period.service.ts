import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPeriod, getPeriodIdentifier } from '../period.model';

export type EntityResponseType = HttpResponse<IPeriod>;
export type EntityArrayResponseType = HttpResponse<IPeriod[]>;

@Injectable({ providedIn: 'root' })
export class PeriodService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/periods');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(period: IPeriod): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(period);
    return this.http
      .post<IPeriod>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(period: IPeriod): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(period);
    return this.http
      .put<IPeriod>(`${this.resourceUrl}/${getPeriodIdentifier(period) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(period: IPeriod): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(period);
    return this.http
      .patch<IPeriod>(`${this.resourceUrl}/${getPeriodIdentifier(period) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPeriod>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPeriod[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPeriodToCollectionIfMissing(periodCollection: IPeriod[], ...periodsToCheck: (IPeriod | null | undefined)[]): IPeriod[] {
    const periods: IPeriod[] = periodsToCheck.filter(isPresent);
    if (periods.length > 0) {
      const periodCollectionIdentifiers = periodCollection.map(periodItem => getPeriodIdentifier(periodItem)!);
      const periodsToAdd = periods.filter(periodItem => {
        const periodIdentifier = getPeriodIdentifier(periodItem);
        if (periodIdentifier == null || periodCollectionIdentifiers.includes(periodIdentifier)) {
          return false;
        }
        periodCollectionIdentifiers.push(periodIdentifier);
        return true;
      });
      return [...periodsToAdd, ...periodCollection];
    }
    return periodCollection;
  }

  protected convertDateFromClient(period: IPeriod): IPeriod {
    return Object.assign({}, period, {
      startDate: period.startDate?.isValid() ? period.startDate.format(DATE_FORMAT) : undefined,
      endDate: period.endDate?.isValid() ? period.endDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? dayjs(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? dayjs(res.body.endDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((period: IPeriod) => {
        period.startDate = period.startDate ? dayjs(period.startDate) : undefined;
        period.endDate = period.endDate ? dayjs(period.endDate) : undefined;
      });
    }
    return res;
  }
}
