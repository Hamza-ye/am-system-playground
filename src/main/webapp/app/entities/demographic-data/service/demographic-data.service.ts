import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDemographicData, getDemographicDataIdentifier } from '../demographic-data.model';

export type EntityResponseType = HttpResponse<IDemographicData>;
export type EntityArrayResponseType = HttpResponse<IDemographicData[]>;

@Injectable({ providedIn: 'root' })
export class DemographicDataService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/demographic-data');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(demographicData: IDemographicData): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demographicData);
    return this.http
      .post<IDemographicData>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(demographicData: IDemographicData): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demographicData);
    return this.http
      .put<IDemographicData>(`${this.resourceUrl}/${getDemographicDataIdentifier(demographicData) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(demographicData: IDemographicData): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demographicData);
    return this.http
      .patch<IDemographicData>(`${this.resourceUrl}/${getDemographicDataIdentifier(demographicData) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDemographicData>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDemographicData[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDemographicDataToCollectionIfMissing(
    demographicDataCollection: IDemographicData[],
    ...demographicDataToCheck: (IDemographicData | null | undefined)[]
  ): IDemographicData[] {
    const demographicData: IDemographicData[] = demographicDataToCheck.filter(isPresent);
    if (demographicData.length > 0) {
      const demographicDataCollectionIdentifiers = demographicDataCollection.map(
        demographicDataItem => getDemographicDataIdentifier(demographicDataItem)!
      );
      const demographicDataToAdd = demographicData.filter(demographicDataItem => {
        const demographicDataIdentifier = getDemographicDataIdentifier(demographicDataItem);
        if (demographicDataIdentifier == null || demographicDataCollectionIdentifiers.includes(demographicDataIdentifier)) {
          return false;
        }
        demographicDataCollectionIdentifiers.push(demographicDataIdentifier);
        return true;
      });
      return [...demographicDataToAdd, ...demographicDataCollection];
    }
    return demographicDataCollection;
  }

  protected convertDateFromClient(demographicData: IDemographicData): IDemographicData {
    return Object.assign({}, demographicData, {
      created: demographicData.created?.isValid() ? demographicData.created.toJSON() : undefined,
      lastUpdated: demographicData.lastUpdated?.isValid() ? demographicData.lastUpdated.toJSON() : undefined,
      date: demographicData.date?.isValid() ? demographicData.date.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.created = res.body.created ? dayjs(res.body.created) : undefined;
      res.body.lastUpdated = res.body.lastUpdated ? dayjs(res.body.lastUpdated) : undefined;
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((demographicData: IDemographicData) => {
        demographicData.created = demographicData.created ? dayjs(demographicData.created) : undefined;
        demographicData.lastUpdated = demographicData.lastUpdated ? dayjs(demographicData.lastUpdated) : undefined;
        demographicData.date = demographicData.date ? dayjs(demographicData.date) : undefined;
      });
    }
    return res;
  }
}
