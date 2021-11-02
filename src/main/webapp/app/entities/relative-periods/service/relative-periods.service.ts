import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRelativePeriods, getRelativePeriodsIdentifier } from '../relative-periods.model';

export type EntityResponseType = HttpResponse<IRelativePeriods>;
export type EntityArrayResponseType = HttpResponse<IRelativePeriods[]>;

@Injectable({ providedIn: 'root' })
export class RelativePeriodsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/relative-periods');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(relativePeriods: IRelativePeriods): Observable<EntityResponseType> {
    return this.http.post<IRelativePeriods>(this.resourceUrl, relativePeriods, { observe: 'response' });
  }

  update(relativePeriods: IRelativePeriods): Observable<EntityResponseType> {
    return this.http.put<IRelativePeriods>(
      `${this.resourceUrl}/${getRelativePeriodsIdentifier(relativePeriods) as number}`,
      relativePeriods,
      { observe: 'response' }
    );
  }

  partialUpdate(relativePeriods: IRelativePeriods): Observable<EntityResponseType> {
    return this.http.patch<IRelativePeriods>(
      `${this.resourceUrl}/${getRelativePeriodsIdentifier(relativePeriods) as number}`,
      relativePeriods,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRelativePeriods>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRelativePeriods[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRelativePeriodsToCollectionIfMissing(
    relativePeriodsCollection: IRelativePeriods[],
    ...relativePeriodsToCheck: (IRelativePeriods | null | undefined)[]
  ): IRelativePeriods[] {
    const relativePeriods: IRelativePeriods[] = relativePeriodsToCheck.filter(isPresent);
    if (relativePeriods.length > 0) {
      const relativePeriodsCollectionIdentifiers = relativePeriodsCollection.map(
        relativePeriodsItem => getRelativePeriodsIdentifier(relativePeriodsItem)!
      );
      const relativePeriodsToAdd = relativePeriods.filter(relativePeriodsItem => {
        const relativePeriodsIdentifier = getRelativePeriodsIdentifier(relativePeriodsItem);
        if (relativePeriodsIdentifier == null || relativePeriodsCollectionIdentifiers.includes(relativePeriodsIdentifier)) {
          return false;
        }
        relativePeriodsCollectionIdentifiers.push(relativePeriodsIdentifier);
        return true;
      });
      return [...relativePeriodsToAdd, ...relativePeriodsCollection];
    }
    return relativePeriodsCollection;
  }
}
