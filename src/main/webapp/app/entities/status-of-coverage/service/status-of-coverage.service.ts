import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStatusOfCoverage, getStatusOfCoverageIdentifier } from '../status-of-coverage.model';

export type EntityResponseType = HttpResponse<IStatusOfCoverage>;
export type EntityArrayResponseType = HttpResponse<IStatusOfCoverage[]>;

@Injectable({ providedIn: 'root' })
export class StatusOfCoverageService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/status-of-coverages');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(statusOfCoverage: IStatusOfCoverage): Observable<EntityResponseType> {
    return this.http.post<IStatusOfCoverage>(this.resourceUrl, statusOfCoverage, { observe: 'response' });
  }

  update(statusOfCoverage: IStatusOfCoverage): Observable<EntityResponseType> {
    return this.http.put<IStatusOfCoverage>(
      `${this.resourceUrl}/${getStatusOfCoverageIdentifier(statusOfCoverage) as number}`,
      statusOfCoverage,
      { observe: 'response' }
    );
  }

  partialUpdate(statusOfCoverage: IStatusOfCoverage): Observable<EntityResponseType> {
    return this.http.patch<IStatusOfCoverage>(
      `${this.resourceUrl}/${getStatusOfCoverageIdentifier(statusOfCoverage) as number}`,
      statusOfCoverage,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStatusOfCoverage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStatusOfCoverage[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addStatusOfCoverageToCollectionIfMissing(
    statusOfCoverageCollection: IStatusOfCoverage[],
    ...statusOfCoveragesToCheck: (IStatusOfCoverage | null | undefined)[]
  ): IStatusOfCoverage[] {
    const statusOfCoverages: IStatusOfCoverage[] = statusOfCoveragesToCheck.filter(isPresent);
    if (statusOfCoverages.length > 0) {
      const statusOfCoverageCollectionIdentifiers = statusOfCoverageCollection.map(
        statusOfCoverageItem => getStatusOfCoverageIdentifier(statusOfCoverageItem)!
      );
      const statusOfCoveragesToAdd = statusOfCoverages.filter(statusOfCoverageItem => {
        const statusOfCoverageIdentifier = getStatusOfCoverageIdentifier(statusOfCoverageItem);
        if (statusOfCoverageIdentifier == null || statusOfCoverageCollectionIdentifiers.includes(statusOfCoverageIdentifier)) {
          return false;
        }
        statusOfCoverageCollectionIdentifiers.push(statusOfCoverageIdentifier);
        return true;
      });
      return [...statusOfCoveragesToAdd, ...statusOfCoverageCollection];
    }
    return statusOfCoverageCollection;
  }
}
