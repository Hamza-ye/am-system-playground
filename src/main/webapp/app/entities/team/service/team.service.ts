import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITeam, getTeamIdentifier } from '../team.model';

export type EntityResponseType = HttpResponse<ITeam>;
export type EntityArrayResponseType = HttpResponse<ITeam[]>;

@Injectable({ providedIn: 'root' })
export class TeamService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/teams');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(team: ITeam): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(team);
    return this.http
      .post<ITeam>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(team: ITeam): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(team);
    return this.http
      .put<ITeam>(`${this.resourceUrl}/${getTeamIdentifier(team) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(team: ITeam): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(team);
    return this.http
      .patch<ITeam>(`${this.resourceUrl}/${getTeamIdentifier(team) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITeam>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITeam[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTeamToCollectionIfMissing(teamCollection: ITeam[], ...teamsToCheck: (ITeam | null | undefined)[]): ITeam[] {
    const teams: ITeam[] = teamsToCheck.filter(isPresent);
    if (teams.length > 0) {
      const teamCollectionIdentifiers = teamCollection.map(teamItem => getTeamIdentifier(teamItem)!);
      const teamsToAdd = teams.filter(teamItem => {
        const teamIdentifier = getTeamIdentifier(teamItem);
        if (teamIdentifier == null || teamCollectionIdentifiers.includes(teamIdentifier)) {
          return false;
        }
        teamCollectionIdentifiers.push(teamIdentifier);
        return true;
      });
      return [...teamsToAdd, ...teamCollection];
    }
    return teamCollection;
  }

  protected convertDateFromClient(team: ITeam): ITeam {
    return Object.assign({}, team, {
      created: team.created?.isValid() ? team.created.toJSON() : undefined,
      lastUpdated: team.lastUpdated?.isValid() ? team.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((team: ITeam) => {
        team.created = team.created ? dayjs(team.created) : undefined;
        team.lastUpdated = team.lastUpdated ? dayjs(team.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
