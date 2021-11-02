import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDataSet, getDataSetIdentifier } from '../data-set.model';

export type EntityResponseType = HttpResponse<IDataSet>;
export type EntityArrayResponseType = HttpResponse<IDataSet[]>;

@Injectable({ providedIn: 'root' })
export class DataSetService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/data-sets');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dataSet: IDataSet): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dataSet);
    return this.http
      .post<IDataSet>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(dataSet: IDataSet): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dataSet);
    return this.http
      .put<IDataSet>(`${this.resourceUrl}/${getDataSetIdentifier(dataSet) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(dataSet: IDataSet): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dataSet);
    return this.http
      .patch<IDataSet>(`${this.resourceUrl}/${getDataSetIdentifier(dataSet) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDataSet>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDataSet[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDataSetToCollectionIfMissing(dataSetCollection: IDataSet[], ...dataSetsToCheck: (IDataSet | null | undefined)[]): IDataSet[] {
    const dataSets: IDataSet[] = dataSetsToCheck.filter(isPresent);
    if (dataSets.length > 0) {
      const dataSetCollectionIdentifiers = dataSetCollection.map(dataSetItem => getDataSetIdentifier(dataSetItem)!);
      const dataSetsToAdd = dataSets.filter(dataSetItem => {
        const dataSetIdentifier = getDataSetIdentifier(dataSetItem);
        if (dataSetIdentifier == null || dataSetCollectionIdentifiers.includes(dataSetIdentifier)) {
          return false;
        }
        dataSetCollectionIdentifiers.push(dataSetIdentifier);
        return true;
      });
      return [...dataSetsToAdd, ...dataSetCollection];
    }
    return dataSetCollection;
  }

  protected convertDateFromClient(dataSet: IDataSet): IDataSet {
    return Object.assign({}, dataSet, {
      created: dataSet.created?.isValid() ? dataSet.created.toJSON() : undefined,
      lastUpdated: dataSet.lastUpdated?.isValid() ? dataSet.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((dataSet: IDataSet) => {
        dataSet.created = dataSet.created ? dayjs(dataSet.created) : undefined;
        dataSet.lastUpdated = dataSet.lastUpdated ? dayjs(dataSet.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
