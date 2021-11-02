import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMalariaUnit, getMalariaUnitIdentifier } from '../malaria-unit.model';

export type EntityResponseType = HttpResponse<IMalariaUnit>;
export type EntityArrayResponseType = HttpResponse<IMalariaUnit[]>;

@Injectable({ providedIn: 'root' })
export class MalariaUnitService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/malaria-units');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(malariaUnit: IMalariaUnit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(malariaUnit);
    return this.http
      .post<IMalariaUnit>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(malariaUnit: IMalariaUnit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(malariaUnit);
    return this.http
      .put<IMalariaUnit>(`${this.resourceUrl}/${getMalariaUnitIdentifier(malariaUnit) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(malariaUnit: IMalariaUnit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(malariaUnit);
    return this.http
      .patch<IMalariaUnit>(`${this.resourceUrl}/${getMalariaUnitIdentifier(malariaUnit) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMalariaUnit>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMalariaUnit[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMalariaUnitToCollectionIfMissing(
    malariaUnitCollection: IMalariaUnit[],
    ...malariaUnitsToCheck: (IMalariaUnit | null | undefined)[]
  ): IMalariaUnit[] {
    const malariaUnits: IMalariaUnit[] = malariaUnitsToCheck.filter(isPresent);
    if (malariaUnits.length > 0) {
      const malariaUnitCollectionIdentifiers = malariaUnitCollection.map(malariaUnitItem => getMalariaUnitIdentifier(malariaUnitItem)!);
      const malariaUnitsToAdd = malariaUnits.filter(malariaUnitItem => {
        const malariaUnitIdentifier = getMalariaUnitIdentifier(malariaUnitItem);
        if (malariaUnitIdentifier == null || malariaUnitCollectionIdentifiers.includes(malariaUnitIdentifier)) {
          return false;
        }
        malariaUnitCollectionIdentifiers.push(malariaUnitIdentifier);
        return true;
      });
      return [...malariaUnitsToAdd, ...malariaUnitCollection];
    }
    return malariaUnitCollection;
  }

  protected convertDateFromClient(malariaUnit: IMalariaUnit): IMalariaUnit {
    return Object.assign({}, malariaUnit, {
      created: malariaUnit.created?.isValid() ? malariaUnit.created.toJSON() : undefined,
      lastUpdated: malariaUnit.lastUpdated?.isValid() ? malariaUnit.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((malariaUnit: IMalariaUnit) => {
        malariaUnit.created = malariaUnit.created ? dayjs(malariaUnit.created) : undefined;
        malariaUnit.lastUpdated = malariaUnit.lastUpdated ? dayjs(malariaUnit.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
