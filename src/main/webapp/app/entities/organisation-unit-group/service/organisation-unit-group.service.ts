import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrganisationUnitGroup, getOrganisationUnitGroupIdentifier } from '../organisation-unit-group.model';

export type EntityResponseType = HttpResponse<IOrganisationUnitGroup>;
export type EntityArrayResponseType = HttpResponse<IOrganisationUnitGroup[]>;

@Injectable({ providedIn: 'root' })
export class OrganisationUnitGroupService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/organisation-unit-groups');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(organisationUnitGroup: IOrganisationUnitGroup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(organisationUnitGroup);
    return this.http
      .post<IOrganisationUnitGroup>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(organisationUnitGroup: IOrganisationUnitGroup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(organisationUnitGroup);
    return this.http
      .put<IOrganisationUnitGroup>(`${this.resourceUrl}/${getOrganisationUnitGroupIdentifier(organisationUnitGroup) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(organisationUnitGroup: IOrganisationUnitGroup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(organisationUnitGroup);
    return this.http
      .patch<IOrganisationUnitGroup>(`${this.resourceUrl}/${getOrganisationUnitGroupIdentifier(organisationUnitGroup) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IOrganisationUnitGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOrganisationUnitGroup[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOrganisationUnitGroupToCollectionIfMissing(
    organisationUnitGroupCollection: IOrganisationUnitGroup[],
    ...organisationUnitGroupsToCheck: (IOrganisationUnitGroup | null | undefined)[]
  ): IOrganisationUnitGroup[] {
    const organisationUnitGroups: IOrganisationUnitGroup[] = organisationUnitGroupsToCheck.filter(isPresent);
    if (organisationUnitGroups.length > 0) {
      const organisationUnitGroupCollectionIdentifiers = organisationUnitGroupCollection.map(
        organisationUnitGroupItem => getOrganisationUnitGroupIdentifier(organisationUnitGroupItem)!
      );
      const organisationUnitGroupsToAdd = organisationUnitGroups.filter(organisationUnitGroupItem => {
        const organisationUnitGroupIdentifier = getOrganisationUnitGroupIdentifier(organisationUnitGroupItem);
        if (
          organisationUnitGroupIdentifier == null ||
          organisationUnitGroupCollectionIdentifiers.includes(organisationUnitGroupIdentifier)
        ) {
          return false;
        }
        organisationUnitGroupCollectionIdentifiers.push(organisationUnitGroupIdentifier);
        return true;
      });
      return [...organisationUnitGroupsToAdd, ...organisationUnitGroupCollection];
    }
    return organisationUnitGroupCollection;
  }

  protected convertDateFromClient(organisationUnitGroup: IOrganisationUnitGroup): IOrganisationUnitGroup {
    return Object.assign({}, organisationUnitGroup, {
      created: organisationUnitGroup.created?.isValid() ? organisationUnitGroup.created.toJSON() : undefined,
      lastUpdated: organisationUnitGroup.lastUpdated?.isValid() ? organisationUnitGroup.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((organisationUnitGroup: IOrganisationUnitGroup) => {
        organisationUnitGroup.created = organisationUnitGroup.created ? dayjs(organisationUnitGroup.created) : undefined;
        organisationUnitGroup.lastUpdated = organisationUnitGroup.lastUpdated ? dayjs(organisationUnitGroup.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
