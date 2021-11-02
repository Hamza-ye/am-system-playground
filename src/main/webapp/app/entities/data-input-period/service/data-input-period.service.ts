import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDataInputPeriod, getDataInputPeriodIdentifier } from '../data-input-period.model';

export type EntityResponseType = HttpResponse<IDataInputPeriod>;
export type EntityArrayResponseType = HttpResponse<IDataInputPeriod[]>;

@Injectable({ providedIn: 'root' })
export class DataInputPeriodService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/data-input-periods');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dataInputPeriod: IDataInputPeriod): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dataInputPeriod);
    return this.http
      .post<IDataInputPeriod>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(dataInputPeriod: IDataInputPeriod): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dataInputPeriod);
    return this.http
      .put<IDataInputPeriod>(`${this.resourceUrl}/${getDataInputPeriodIdentifier(dataInputPeriod) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(dataInputPeriod: IDataInputPeriod): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dataInputPeriod);
    return this.http
      .patch<IDataInputPeriod>(`${this.resourceUrl}/${getDataInputPeriodIdentifier(dataInputPeriod) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDataInputPeriod>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDataInputPeriod[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDataInputPeriodToCollectionIfMissing(
    dataInputPeriodCollection: IDataInputPeriod[],
    ...dataInputPeriodsToCheck: (IDataInputPeriod | null | undefined)[]
  ): IDataInputPeriod[] {
    const dataInputPeriods: IDataInputPeriod[] = dataInputPeriodsToCheck.filter(isPresent);
    if (dataInputPeriods.length > 0) {
      const dataInputPeriodCollectionIdentifiers = dataInputPeriodCollection.map(
        dataInputPeriodItem => getDataInputPeriodIdentifier(dataInputPeriodItem)!
      );
      const dataInputPeriodsToAdd = dataInputPeriods.filter(dataInputPeriodItem => {
        const dataInputPeriodIdentifier = getDataInputPeriodIdentifier(dataInputPeriodItem);
        if (dataInputPeriodIdentifier == null || dataInputPeriodCollectionIdentifiers.includes(dataInputPeriodIdentifier)) {
          return false;
        }
        dataInputPeriodCollectionIdentifiers.push(dataInputPeriodIdentifier);
        return true;
      });
      return [...dataInputPeriodsToAdd, ...dataInputPeriodCollection];
    }
    return dataInputPeriodCollection;
  }

  protected convertDateFromClient(dataInputPeriod: IDataInputPeriod): IDataInputPeriod {
    return Object.assign({}, dataInputPeriod, {
      openingDate: dataInputPeriod.openingDate?.isValid() ? dataInputPeriod.openingDate.format(DATE_FORMAT) : undefined,
      closingDate: dataInputPeriod.closingDate?.isValid() ? dataInputPeriod.closingDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.openingDate = res.body.openingDate ? dayjs(res.body.openingDate) : undefined;
      res.body.closingDate = res.body.closingDate ? dayjs(res.body.closingDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((dataInputPeriod: IDataInputPeriod) => {
        dataInputPeriod.openingDate = dataInputPeriod.openingDate ? dayjs(dataInputPeriod.openingDate) : undefined;
        dataInputPeriod.closingDate = dataInputPeriod.closingDate ? dayjs(dataInputPeriod.closingDate) : undefined;
      });
    }
    return res;
  }
}
