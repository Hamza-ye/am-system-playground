import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFamilyHead, getFamilyHeadIdentifier } from '../family-head.model';

export type EntityResponseType = HttpResponse<IFamilyHead>;
export type EntityArrayResponseType = HttpResponse<IFamilyHead[]>;

@Injectable({ providedIn: 'root' })
export class FamilyHeadService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/family-heads');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(familyHead: IFamilyHead): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(familyHead);
    return this.http
      .post<IFamilyHead>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(familyHead: IFamilyHead): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(familyHead);
    return this.http
      .put<IFamilyHead>(`${this.resourceUrl}/${getFamilyHeadIdentifier(familyHead) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(familyHead: IFamilyHead): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(familyHead);
    return this.http
      .patch<IFamilyHead>(`${this.resourceUrl}/${getFamilyHeadIdentifier(familyHead) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFamilyHead>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFamilyHead[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFamilyHeadToCollectionIfMissing(
    familyHeadCollection: IFamilyHead[],
    ...familyHeadsToCheck: (IFamilyHead | null | undefined)[]
  ): IFamilyHead[] {
    const familyHeads: IFamilyHead[] = familyHeadsToCheck.filter(isPresent);
    if (familyHeads.length > 0) {
      const familyHeadCollectionIdentifiers = familyHeadCollection.map(familyHeadItem => getFamilyHeadIdentifier(familyHeadItem)!);
      const familyHeadsToAdd = familyHeads.filter(familyHeadItem => {
        const familyHeadIdentifier = getFamilyHeadIdentifier(familyHeadItem);
        if (familyHeadIdentifier == null || familyHeadCollectionIdentifiers.includes(familyHeadIdentifier)) {
          return false;
        }
        familyHeadCollectionIdentifiers.push(familyHeadIdentifier);
        return true;
      });
      return [...familyHeadsToAdd, ...familyHeadCollection];
    }
    return familyHeadCollection;
  }

  protected convertDateFromClient(familyHead: IFamilyHead): IFamilyHead {
    return Object.assign({}, familyHead, {
      created: familyHead.created?.isValid() ? familyHead.created.toJSON() : undefined,
      lastUpdated: familyHead.lastUpdated?.isValid() ? familyHead.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((familyHead: IFamilyHead) => {
        familyHead.created = familyHead.created ? dayjs(familyHead.created) : undefined;
        familyHead.lastUpdated = familyHead.lastUpdated ? dayjs(familyHead.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
