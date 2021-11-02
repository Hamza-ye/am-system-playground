import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDataProvider, getDataProviderIdentifier } from '../data-provider.model';

export type EntityResponseType = HttpResponse<IDataProvider>;
export type EntityArrayResponseType = HttpResponse<IDataProvider[]>;

@Injectable({ providedIn: 'root' })
export class DataProviderService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/data-providers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dataProvider: IDataProvider): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dataProvider);
    return this.http
      .post<IDataProvider>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(dataProvider: IDataProvider): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dataProvider);
    return this.http
      .put<IDataProvider>(`${this.resourceUrl}/${getDataProviderIdentifier(dataProvider) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(dataProvider: IDataProvider): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dataProvider);
    return this.http
      .patch<IDataProvider>(`${this.resourceUrl}/${getDataProviderIdentifier(dataProvider) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDataProvider>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDataProvider[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDataProviderToCollectionIfMissing(
    dataProviderCollection: IDataProvider[],
    ...dataProvidersToCheck: (IDataProvider | null | undefined)[]
  ): IDataProvider[] {
    const dataProviders: IDataProvider[] = dataProvidersToCheck.filter(isPresent);
    if (dataProviders.length > 0) {
      const dataProviderCollectionIdentifiers = dataProviderCollection.map(
        dataProviderItem => getDataProviderIdentifier(dataProviderItem)!
      );
      const dataProvidersToAdd = dataProviders.filter(dataProviderItem => {
        const dataProviderIdentifier = getDataProviderIdentifier(dataProviderItem);
        if (dataProviderIdentifier == null || dataProviderCollectionIdentifiers.includes(dataProviderIdentifier)) {
          return false;
        }
        dataProviderCollectionIdentifiers.push(dataProviderIdentifier);
        return true;
      });
      return [...dataProvidersToAdd, ...dataProviderCollection];
    }
    return dataProviderCollection;
  }

  protected convertDateFromClient(dataProvider: IDataProvider): IDataProvider {
    return Object.assign({}, dataProvider, {
      created: dataProvider.created?.isValid() ? dataProvider.created.toJSON() : undefined,
      lastUpdated: dataProvider.lastUpdated?.isValid() ? dataProvider.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((dataProvider: IDataProvider) => {
        dataProvider.created = dataProvider.created ? dayjs(dataProvider.created) : undefined;
        dataProvider.lastUpdated = dataProvider.lastUpdated ? dayjs(dataProvider.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
