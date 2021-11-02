import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFamily, getFamilyIdentifier } from '../family.model';

export type EntityResponseType = HttpResponse<IFamily>;
export type EntityArrayResponseType = HttpResponse<IFamily[]>;

@Injectable({ providedIn: 'root' })
export class FamilyService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/families');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(family: IFamily): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(family);
    return this.http
      .post<IFamily>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(family: IFamily): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(family);
    return this.http
      .put<IFamily>(`${this.resourceUrl}/${getFamilyIdentifier(family) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(family: IFamily): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(family);
    return this.http
      .patch<IFamily>(`${this.resourceUrl}/${getFamilyIdentifier(family) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFamily>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFamily[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFamilyToCollectionIfMissing(familyCollection: IFamily[], ...familiesToCheck: (IFamily | null | undefined)[]): IFamily[] {
    const families: IFamily[] = familiesToCheck.filter(isPresent);
    if (families.length > 0) {
      const familyCollectionIdentifiers = familyCollection.map(familyItem => getFamilyIdentifier(familyItem)!);
      const familiesToAdd = families.filter(familyItem => {
        const familyIdentifier = getFamilyIdentifier(familyItem);
        if (familyIdentifier == null || familyCollectionIdentifiers.includes(familyIdentifier)) {
          return false;
        }
        familyCollectionIdentifiers.push(familyIdentifier);
        return true;
      });
      return [...familiesToAdd, ...familyCollection];
    }
    return familyCollection;
  }

  protected convertDateFromClient(family: IFamily): IFamily {
    return Object.assign({}, family, {
      created: family.created?.isValid() ? family.created.toJSON() : undefined,
      lastUpdated: family.lastUpdated?.isValid() ? family.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((family: IFamily) => {
        family.created = family.created ? dayjs(family.created) : undefined;
        family.lastUpdated = family.lastUpdated ? dayjs(family.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
