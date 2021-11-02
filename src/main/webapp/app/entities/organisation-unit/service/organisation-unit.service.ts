import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrganisationUnit, getOrganisationUnitIdentifier } from '../organisation-unit.model';

export type EntityResponseType = HttpResponse<IOrganisationUnit>;
export type EntityArrayResponseType = HttpResponse<IOrganisationUnit[]>;

@Injectable({ providedIn: 'root' })
export class OrganisationUnitService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/organisation-units');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(organisationUnit: IOrganisationUnit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(organisationUnit);
    return this.http
      .post<IOrganisationUnit>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(organisationUnit: IOrganisationUnit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(organisationUnit);
    return this.http
      .put<IOrganisationUnit>(`${this.resourceUrl}/${getOrganisationUnitIdentifier(organisationUnit) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(organisationUnit: IOrganisationUnit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(organisationUnit);
    return this.http
      .patch<IOrganisationUnit>(`${this.resourceUrl}/${getOrganisationUnitIdentifier(organisationUnit) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IOrganisationUnit>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOrganisationUnit[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOrganisationUnitToCollectionIfMissing(
    organisationUnitCollection: IOrganisationUnit[],
    ...organisationUnitsToCheck: (IOrganisationUnit | null | undefined)[]
  ): IOrganisationUnit[] {
    const organisationUnits: IOrganisationUnit[] = organisationUnitsToCheck.filter(isPresent);
    if (organisationUnits.length > 0) {
      const organisationUnitCollectionIdentifiers = organisationUnitCollection.map(
        organisationUnitItem => getOrganisationUnitIdentifier(organisationUnitItem)!
      );
      const organisationUnitsToAdd = organisationUnits.filter(organisationUnitItem => {
        const organisationUnitIdentifier = getOrganisationUnitIdentifier(organisationUnitItem);
        if (organisationUnitIdentifier == null || organisationUnitCollectionIdentifiers.includes(organisationUnitIdentifier)) {
          return false;
        }
        organisationUnitCollectionIdentifiers.push(organisationUnitIdentifier);
        return true;
      });
      return [...organisationUnitsToAdd, ...organisationUnitCollection];
    }
    return organisationUnitCollection;
  }

  protected convertDateFromClient(organisationUnit: IOrganisationUnit): IOrganisationUnit {
    return Object.assign({}, organisationUnit, {
      created: organisationUnit.created?.isValid() ? organisationUnit.created.toJSON() : undefined,
      lastUpdated: organisationUnit.lastUpdated?.isValid() ? organisationUnit.lastUpdated.toJSON() : undefined,
      openingDate: organisationUnit.openingDate?.isValid() ? organisationUnit.openingDate.format(DATE_FORMAT) : undefined,
      closedDate: organisationUnit.closedDate?.isValid() ? organisationUnit.closedDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.created = res.body.created ? dayjs(res.body.created) : undefined;
      res.body.lastUpdated = res.body.lastUpdated ? dayjs(res.body.lastUpdated) : undefined;
      res.body.openingDate = res.body.openingDate ? dayjs(res.body.openingDate) : undefined;
      res.body.closedDate = res.body.closedDate ? dayjs(res.body.closedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((organisationUnit: IOrganisationUnit) => {
        organisationUnit.created = organisationUnit.created ? dayjs(organisationUnit.created) : undefined;
        organisationUnit.lastUpdated = organisationUnit.lastUpdated ? dayjs(organisationUnit.lastUpdated) : undefined;
        organisationUnit.openingDate = organisationUnit.openingDate ? dayjs(organisationUnit.openingDate) : undefined;
        organisationUnit.closedDate = organisationUnit.closedDate ? dayjs(organisationUnit.closedDate) : undefined;
      });
    }
    return res;
  }
}
