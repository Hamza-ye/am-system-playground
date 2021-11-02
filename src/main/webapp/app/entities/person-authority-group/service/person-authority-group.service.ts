import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPersonAuthorityGroup, getPersonAuthorityGroupIdentifier } from '../person-authority-group.model';

export type EntityResponseType = HttpResponse<IPersonAuthorityGroup>;
export type EntityArrayResponseType = HttpResponse<IPersonAuthorityGroup[]>;

@Injectable({ providedIn: 'root' })
export class PersonAuthorityGroupService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/person-authority-groups');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(personAuthorityGroup: IPersonAuthorityGroup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(personAuthorityGroup);
    return this.http
      .post<IPersonAuthorityGroup>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(personAuthorityGroup: IPersonAuthorityGroup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(personAuthorityGroup);
    return this.http
      .put<IPersonAuthorityGroup>(`${this.resourceUrl}/${getPersonAuthorityGroupIdentifier(personAuthorityGroup) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(personAuthorityGroup: IPersonAuthorityGroup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(personAuthorityGroup);
    return this.http
      .patch<IPersonAuthorityGroup>(`${this.resourceUrl}/${getPersonAuthorityGroupIdentifier(personAuthorityGroup) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPersonAuthorityGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPersonAuthorityGroup[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPersonAuthorityGroupToCollectionIfMissing(
    personAuthorityGroupCollection: IPersonAuthorityGroup[],
    ...personAuthorityGroupsToCheck: (IPersonAuthorityGroup | null | undefined)[]
  ): IPersonAuthorityGroup[] {
    const personAuthorityGroups: IPersonAuthorityGroup[] = personAuthorityGroupsToCheck.filter(isPresent);
    if (personAuthorityGroups.length > 0) {
      const personAuthorityGroupCollectionIdentifiers = personAuthorityGroupCollection.map(
        personAuthorityGroupItem => getPersonAuthorityGroupIdentifier(personAuthorityGroupItem)!
      );
      const personAuthorityGroupsToAdd = personAuthorityGroups.filter(personAuthorityGroupItem => {
        const personAuthorityGroupIdentifier = getPersonAuthorityGroupIdentifier(personAuthorityGroupItem);
        if (personAuthorityGroupIdentifier == null || personAuthorityGroupCollectionIdentifiers.includes(personAuthorityGroupIdentifier)) {
          return false;
        }
        personAuthorityGroupCollectionIdentifiers.push(personAuthorityGroupIdentifier);
        return true;
      });
      return [...personAuthorityGroupsToAdd, ...personAuthorityGroupCollection];
    }
    return personAuthorityGroupCollection;
  }

  protected convertDateFromClient(personAuthorityGroup: IPersonAuthorityGroup): IPersonAuthorityGroup {
    return Object.assign({}, personAuthorityGroup, {
      created: personAuthorityGroup.created?.isValid() ? personAuthorityGroup.created.toJSON() : undefined,
      lastUpdated: personAuthorityGroup.lastUpdated?.isValid() ? personAuthorityGroup.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((personAuthorityGroup: IPersonAuthorityGroup) => {
        personAuthorityGroup.created = personAuthorityGroup.created ? dayjs(personAuthorityGroup.created) : undefined;
        personAuthorityGroup.lastUpdated = personAuthorityGroup.lastUpdated ? dayjs(personAuthorityGroup.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
