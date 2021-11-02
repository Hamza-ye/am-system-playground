import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMalariaUnitStaffMember, getMalariaUnitStaffMemberIdentifier } from '../malaria-unit-staff-member.model';

export type EntityResponseType = HttpResponse<IMalariaUnitStaffMember>;
export type EntityArrayResponseType = HttpResponse<IMalariaUnitStaffMember[]>;

@Injectable({ providedIn: 'root' })
export class MalariaUnitStaffMemberService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/malaria-unit-staff-members');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(malariaUnitStaffMember: IMalariaUnitStaffMember): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(malariaUnitStaffMember);
    return this.http
      .post<IMalariaUnitStaffMember>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(malariaUnitStaffMember: IMalariaUnitStaffMember): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(malariaUnitStaffMember);
    return this.http
      .put<IMalariaUnitStaffMember>(`${this.resourceUrl}/${getMalariaUnitStaffMemberIdentifier(malariaUnitStaffMember) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(malariaUnitStaffMember: IMalariaUnitStaffMember): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(malariaUnitStaffMember);
    return this.http
      .patch<IMalariaUnitStaffMember>(
        `${this.resourceUrl}/${getMalariaUnitStaffMemberIdentifier(malariaUnitStaffMember) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMalariaUnitStaffMember>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMalariaUnitStaffMember[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMalariaUnitStaffMemberToCollectionIfMissing(
    malariaUnitStaffMemberCollection: IMalariaUnitStaffMember[],
    ...malariaUnitStaffMembersToCheck: (IMalariaUnitStaffMember | null | undefined)[]
  ): IMalariaUnitStaffMember[] {
    const malariaUnitStaffMembers: IMalariaUnitStaffMember[] = malariaUnitStaffMembersToCheck.filter(isPresent);
    if (malariaUnitStaffMembers.length > 0) {
      const malariaUnitStaffMemberCollectionIdentifiers = malariaUnitStaffMemberCollection.map(
        malariaUnitStaffMemberItem => getMalariaUnitStaffMemberIdentifier(malariaUnitStaffMemberItem)!
      );
      const malariaUnitStaffMembersToAdd = malariaUnitStaffMembers.filter(malariaUnitStaffMemberItem => {
        const malariaUnitStaffMemberIdentifier = getMalariaUnitStaffMemberIdentifier(malariaUnitStaffMemberItem);
        if (
          malariaUnitStaffMemberIdentifier == null ||
          malariaUnitStaffMemberCollectionIdentifiers.includes(malariaUnitStaffMemberIdentifier)
        ) {
          return false;
        }
        malariaUnitStaffMemberCollectionIdentifiers.push(malariaUnitStaffMemberIdentifier);
        return true;
      });
      return [...malariaUnitStaffMembersToAdd, ...malariaUnitStaffMemberCollection];
    }
    return malariaUnitStaffMemberCollection;
  }

  protected convertDateFromClient(malariaUnitStaffMember: IMalariaUnitStaffMember): IMalariaUnitStaffMember {
    return Object.assign({}, malariaUnitStaffMember, {
      created: malariaUnitStaffMember.created?.isValid() ? malariaUnitStaffMember.created.toJSON() : undefined,
      lastUpdated: malariaUnitStaffMember.lastUpdated?.isValid() ? malariaUnitStaffMember.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((malariaUnitStaffMember: IMalariaUnitStaffMember) => {
        malariaUnitStaffMember.created = malariaUnitStaffMember.created ? dayjs(malariaUnitStaffMember.created) : undefined;
        malariaUnitStaffMember.lastUpdated = malariaUnitStaffMember.lastUpdated ? dayjs(malariaUnitStaffMember.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
