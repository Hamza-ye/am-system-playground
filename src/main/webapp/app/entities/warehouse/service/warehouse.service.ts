import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWarehouse, getWarehouseIdentifier } from '../warehouse.model';

export type EntityResponseType = HttpResponse<IWarehouse>;
export type EntityArrayResponseType = HttpResponse<IWarehouse[]>;

@Injectable({ providedIn: 'root' })
export class WarehouseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/warehouses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(warehouse: IWarehouse): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(warehouse);
    return this.http
      .post<IWarehouse>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(warehouse: IWarehouse): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(warehouse);
    return this.http
      .put<IWarehouse>(`${this.resourceUrl}/${getWarehouseIdentifier(warehouse) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(warehouse: IWarehouse): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(warehouse);
    return this.http
      .patch<IWarehouse>(`${this.resourceUrl}/${getWarehouseIdentifier(warehouse) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IWarehouse>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWarehouse[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addWarehouseToCollectionIfMissing(
    warehouseCollection: IWarehouse[],
    ...warehousesToCheck: (IWarehouse | null | undefined)[]
  ): IWarehouse[] {
    const warehouses: IWarehouse[] = warehousesToCheck.filter(isPresent);
    if (warehouses.length > 0) {
      const warehouseCollectionIdentifiers = warehouseCollection.map(warehouseItem => getWarehouseIdentifier(warehouseItem)!);
      const warehousesToAdd = warehouses.filter(warehouseItem => {
        const warehouseIdentifier = getWarehouseIdentifier(warehouseItem);
        if (warehouseIdentifier == null || warehouseCollectionIdentifiers.includes(warehouseIdentifier)) {
          return false;
        }
        warehouseCollectionIdentifiers.push(warehouseIdentifier);
        return true;
      });
      return [...warehousesToAdd, ...warehouseCollection];
    }
    return warehouseCollection;
  }

  protected convertDateFromClient(warehouse: IWarehouse): IWarehouse {
    return Object.assign({}, warehouse, {
      created: warehouse.created?.isValid() ? warehouse.created.toJSON() : undefined,
      lastUpdated: warehouse.lastUpdated?.isValid() ? warehouse.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((warehouse: IWarehouse) => {
        warehouse.created = warehouse.created ? dayjs(warehouse.created) : undefined;
        warehouse.lastUpdated = warehouse.lastUpdated ? dayjs(warehouse.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
