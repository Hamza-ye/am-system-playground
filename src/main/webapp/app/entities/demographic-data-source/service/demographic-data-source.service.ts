import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDemographicDataSource, getDemographicDataSourceIdentifier } from '../demographic-data-source.model';

export type EntityResponseType = HttpResponse<IDemographicDataSource>;
export type EntityArrayResponseType = HttpResponse<IDemographicDataSource[]>;

@Injectable({ providedIn: 'root' })
export class DemographicDataSourceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/demographic-data-sources');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(demographicDataSource: IDemographicDataSource): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demographicDataSource);
    return this.http
      .post<IDemographicDataSource>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(demographicDataSource: IDemographicDataSource): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demographicDataSource);
    return this.http
      .put<IDemographicDataSource>(`${this.resourceUrl}/${getDemographicDataSourceIdentifier(demographicDataSource) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(demographicDataSource: IDemographicDataSource): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demographicDataSource);
    return this.http
      .patch<IDemographicDataSource>(`${this.resourceUrl}/${getDemographicDataSourceIdentifier(demographicDataSource) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDemographicDataSource>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDemographicDataSource[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDemographicDataSourceToCollectionIfMissing(
    demographicDataSourceCollection: IDemographicDataSource[],
    ...demographicDataSourcesToCheck: (IDemographicDataSource | null | undefined)[]
  ): IDemographicDataSource[] {
    const demographicDataSources: IDemographicDataSource[] = demographicDataSourcesToCheck.filter(isPresent);
    if (demographicDataSources.length > 0) {
      const demographicDataSourceCollectionIdentifiers = demographicDataSourceCollection.map(
        demographicDataSourceItem => getDemographicDataSourceIdentifier(demographicDataSourceItem)!
      );
      const demographicDataSourcesToAdd = demographicDataSources.filter(demographicDataSourceItem => {
        const demographicDataSourceIdentifier = getDemographicDataSourceIdentifier(demographicDataSourceItem);
        if (
          demographicDataSourceIdentifier == null ||
          demographicDataSourceCollectionIdentifiers.includes(demographicDataSourceIdentifier)
        ) {
          return false;
        }
        demographicDataSourceCollectionIdentifiers.push(demographicDataSourceIdentifier);
        return true;
      });
      return [...demographicDataSourcesToAdd, ...demographicDataSourceCollection];
    }
    return demographicDataSourceCollection;
  }

  protected convertDateFromClient(demographicDataSource: IDemographicDataSource): IDemographicDataSource {
    return Object.assign({}, demographicDataSource, {
      created: demographicDataSource.created?.isValid() ? demographicDataSource.created.toJSON() : undefined,
      lastUpdated: demographicDataSource.lastUpdated?.isValid() ? demographicDataSource.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((demographicDataSource: IDemographicDataSource) => {
        demographicDataSource.created = demographicDataSource.created ? dayjs(demographicDataSource.created) : undefined;
        demographicDataSource.lastUpdated = demographicDataSource.lastUpdated ? dayjs(demographicDataSource.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
