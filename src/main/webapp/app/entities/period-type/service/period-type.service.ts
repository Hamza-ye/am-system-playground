import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPeriodType, getPeriodTypeIdentifier } from '../period-type.model';

export type EntityResponseType = HttpResponse<IPeriodType>;
export type EntityArrayResponseType = HttpResponse<IPeriodType[]>;

@Injectable({ providedIn: 'root' })
export class PeriodTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/period-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(periodType: IPeriodType): Observable<EntityResponseType> {
    return this.http.post<IPeriodType>(this.resourceUrl, periodType, { observe: 'response' });
  }

  update(periodType: IPeriodType): Observable<EntityResponseType> {
    return this.http.put<IPeriodType>(`${this.resourceUrl}/${getPeriodTypeIdentifier(periodType) as number}`, periodType, {
      observe: 'response',
    });
  }

  partialUpdate(periodType: IPeriodType): Observable<EntityResponseType> {
    return this.http.patch<IPeriodType>(`${this.resourceUrl}/${getPeriodTypeIdentifier(periodType) as number}`, periodType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPeriodType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPeriodType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPeriodTypeToCollectionIfMissing(
    periodTypeCollection: IPeriodType[],
    ...periodTypesToCheck: (IPeriodType | null | undefined)[]
  ): IPeriodType[] {
    const periodTypes: IPeriodType[] = periodTypesToCheck.filter(isPresent);
    if (periodTypes.length > 0) {
      const periodTypeCollectionIdentifiers = periodTypeCollection.map(periodTypeItem => getPeriodTypeIdentifier(periodTypeItem)!);
      const periodTypesToAdd = periodTypes.filter(periodTypeItem => {
        const periodTypeIdentifier = getPeriodTypeIdentifier(periodTypeItem);
        if (periodTypeIdentifier == null || periodTypeCollectionIdentifiers.includes(periodTypeIdentifier)) {
          return false;
        }
        periodTypeCollectionIdentifiers.push(periodTypeIdentifier);
        return true;
      });
      return [...periodTypesToAdd, ...periodTypeCollection];
    }
    return periodTypeCollection;
  }
}
