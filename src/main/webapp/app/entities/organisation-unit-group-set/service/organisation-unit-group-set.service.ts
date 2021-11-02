import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrganisationUnitGroupSet, getOrganisationUnitGroupSetIdentifier } from '../organisation-unit-group-set.model';

export type EntityResponseType = HttpResponse<IOrganisationUnitGroupSet>;
export type EntityArrayResponseType = HttpResponse<IOrganisationUnitGroupSet[]>;

@Injectable({ providedIn: 'root' })
export class OrganisationUnitGroupSetService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/organisation-unit-group-sets');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(organisationUnitGroupSet: IOrganisationUnitGroupSet): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(organisationUnitGroupSet);
    return this.http
      .post<IOrganisationUnitGroupSet>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(organisationUnitGroupSet: IOrganisationUnitGroupSet): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(organisationUnitGroupSet);
    return this.http
      .put<IOrganisationUnitGroupSet>(
        `${this.resourceUrl}/${getOrganisationUnitGroupSetIdentifier(organisationUnitGroupSet) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(organisationUnitGroupSet: IOrganisationUnitGroupSet): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(organisationUnitGroupSet);
    return this.http
      .patch<IOrganisationUnitGroupSet>(
        `${this.resourceUrl}/${getOrganisationUnitGroupSetIdentifier(organisationUnitGroupSet) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IOrganisationUnitGroupSet>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOrganisationUnitGroupSet[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOrganisationUnitGroupSetToCollectionIfMissing(
    organisationUnitGroupSetCollection: IOrganisationUnitGroupSet[],
    ...organisationUnitGroupSetsToCheck: (IOrganisationUnitGroupSet | null | undefined)[]
  ): IOrganisationUnitGroupSet[] {
    const organisationUnitGroupSets: IOrganisationUnitGroupSet[] = organisationUnitGroupSetsToCheck.filter(isPresent);
    if (organisationUnitGroupSets.length > 0) {
      const organisationUnitGroupSetCollectionIdentifiers = organisationUnitGroupSetCollection.map(
        organisationUnitGroupSetItem => getOrganisationUnitGroupSetIdentifier(organisationUnitGroupSetItem)!
      );
      const organisationUnitGroupSetsToAdd = organisationUnitGroupSets.filter(organisationUnitGroupSetItem => {
        const organisationUnitGroupSetIdentifier = getOrganisationUnitGroupSetIdentifier(organisationUnitGroupSetItem);
        if (
          organisationUnitGroupSetIdentifier == null ||
          organisationUnitGroupSetCollectionIdentifiers.includes(organisationUnitGroupSetIdentifier)
        ) {
          return false;
        }
        organisationUnitGroupSetCollectionIdentifiers.push(organisationUnitGroupSetIdentifier);
        return true;
      });
      return [...organisationUnitGroupSetsToAdd, ...organisationUnitGroupSetCollection];
    }
    return organisationUnitGroupSetCollection;
  }

  protected convertDateFromClient(organisationUnitGroupSet: IOrganisationUnitGroupSet): IOrganisationUnitGroupSet {
    return Object.assign({}, organisationUnitGroupSet, {
      created: organisationUnitGroupSet.created?.isValid() ? organisationUnitGroupSet.created.toJSON() : undefined,
      lastUpdated: organisationUnitGroupSet.lastUpdated?.isValid() ? organisationUnitGroupSet.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((organisationUnitGroupSet: IOrganisationUnitGroupSet) => {
        organisationUnitGroupSet.created = organisationUnitGroupSet.created ? dayjs(organisationUnitGroupSet.created) : undefined;
        organisationUnitGroupSet.lastUpdated = organisationUnitGroupSet.lastUpdated
          ? dayjs(organisationUnitGroupSet.lastUpdated)
          : undefined;
      });
    }
    return res;
  }
}
