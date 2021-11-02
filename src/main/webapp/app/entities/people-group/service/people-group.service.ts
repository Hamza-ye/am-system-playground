import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPeopleGroup, getPeopleGroupIdentifier } from '../people-group.model';

export type EntityResponseType = HttpResponse<IPeopleGroup>;
export type EntityArrayResponseType = HttpResponse<IPeopleGroup[]>;

@Injectable({ providedIn: 'root' })
export class PeopleGroupService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/people-groups');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(peopleGroup: IPeopleGroup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(peopleGroup);
    return this.http
      .post<IPeopleGroup>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(peopleGroup: IPeopleGroup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(peopleGroup);
    return this.http
      .put<IPeopleGroup>(`${this.resourceUrl}/${getPeopleGroupIdentifier(peopleGroup) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(peopleGroup: IPeopleGroup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(peopleGroup);
    return this.http
      .patch<IPeopleGroup>(`${this.resourceUrl}/${getPeopleGroupIdentifier(peopleGroup) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPeopleGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPeopleGroup[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPeopleGroupToCollectionIfMissing(
    peopleGroupCollection: IPeopleGroup[],
    ...peopleGroupsToCheck: (IPeopleGroup | null | undefined)[]
  ): IPeopleGroup[] {
    const peopleGroups: IPeopleGroup[] = peopleGroupsToCheck.filter(isPresent);
    if (peopleGroups.length > 0) {
      const peopleGroupCollectionIdentifiers = peopleGroupCollection.map(peopleGroupItem => getPeopleGroupIdentifier(peopleGroupItem)!);
      const peopleGroupsToAdd = peopleGroups.filter(peopleGroupItem => {
        const peopleGroupIdentifier = getPeopleGroupIdentifier(peopleGroupItem);
        if (peopleGroupIdentifier == null || peopleGroupCollectionIdentifiers.includes(peopleGroupIdentifier)) {
          return false;
        }
        peopleGroupCollectionIdentifiers.push(peopleGroupIdentifier);
        return true;
      });
      return [...peopleGroupsToAdd, ...peopleGroupCollection];
    }
    return peopleGroupCollection;
  }

  protected convertDateFromClient(peopleGroup: IPeopleGroup): IPeopleGroup {
    return Object.assign({}, peopleGroup, {
      created: peopleGroup.created?.isValid() ? peopleGroup.created.toJSON() : undefined,
      lastUpdated: peopleGroup.lastUpdated?.isValid() ? peopleGroup.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((peopleGroup: IPeopleGroup) => {
        peopleGroup.created = peopleGroup.created ? dayjs(peopleGroup.created) : undefined;
        peopleGroup.lastUpdated = peopleGroup.lastUpdated ? dayjs(peopleGroup.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
