import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrganisationUnitLevel, getOrganisationUnitLevelIdentifier } from '../organisation-unit-level.model';

export type EntityResponseType = HttpResponse<IOrganisationUnitLevel>;
export type EntityArrayResponseType = HttpResponse<IOrganisationUnitLevel[]>;

@Injectable({ providedIn: 'root' })
export class OrganisationUnitLevelService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/organisation-unit-levels');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(organisationUnitLevel: IOrganisationUnitLevel): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(organisationUnitLevel);
    return this.http
      .post<IOrganisationUnitLevel>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(organisationUnitLevel: IOrganisationUnitLevel): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(organisationUnitLevel);
    return this.http
      .put<IOrganisationUnitLevel>(`${this.resourceUrl}/${getOrganisationUnitLevelIdentifier(organisationUnitLevel) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(organisationUnitLevel: IOrganisationUnitLevel): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(organisationUnitLevel);
    return this.http
      .patch<IOrganisationUnitLevel>(`${this.resourceUrl}/${getOrganisationUnitLevelIdentifier(organisationUnitLevel) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IOrganisationUnitLevel>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOrganisationUnitLevel[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOrganisationUnitLevelToCollectionIfMissing(
    organisationUnitLevelCollection: IOrganisationUnitLevel[],
    ...organisationUnitLevelsToCheck: (IOrganisationUnitLevel | null | undefined)[]
  ): IOrganisationUnitLevel[] {
    const organisationUnitLevels: IOrganisationUnitLevel[] = organisationUnitLevelsToCheck.filter(isPresent);
    if (organisationUnitLevels.length > 0) {
      const organisationUnitLevelCollectionIdentifiers = organisationUnitLevelCollection.map(
        organisationUnitLevelItem => getOrganisationUnitLevelIdentifier(organisationUnitLevelItem)!
      );
      const organisationUnitLevelsToAdd = organisationUnitLevels.filter(organisationUnitLevelItem => {
        const organisationUnitLevelIdentifier = getOrganisationUnitLevelIdentifier(organisationUnitLevelItem);
        if (
          organisationUnitLevelIdentifier == null ||
          organisationUnitLevelCollectionIdentifiers.includes(organisationUnitLevelIdentifier)
        ) {
          return false;
        }
        organisationUnitLevelCollectionIdentifiers.push(organisationUnitLevelIdentifier);
        return true;
      });
      return [...organisationUnitLevelsToAdd, ...organisationUnitLevelCollection];
    }
    return organisationUnitLevelCollection;
  }

  protected convertDateFromClient(organisationUnitLevel: IOrganisationUnitLevel): IOrganisationUnitLevel {
    return Object.assign({}, organisationUnitLevel, {
      created: organisationUnitLevel.created?.isValid() ? organisationUnitLevel.created.toJSON() : undefined,
      lastUpdated: organisationUnitLevel.lastUpdated?.isValid() ? organisationUnitLevel.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((organisationUnitLevel: IOrganisationUnitLevel) => {
        organisationUnitLevel.created = organisationUnitLevel.created ? dayjs(organisationUnitLevel.created) : undefined;
        organisationUnitLevel.lastUpdated = organisationUnitLevel.lastUpdated ? dayjs(organisationUnitLevel.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
