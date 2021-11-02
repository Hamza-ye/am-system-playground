import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWorkingDay, getWorkingDayIdentifier } from '../working-day.model';

export type EntityResponseType = HttpResponse<IWorkingDay>;
export type EntityArrayResponseType = HttpResponse<IWorkingDay[]>;

@Injectable({ providedIn: 'root' })
export class WorkingDayService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/working-days');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(workingDay: IWorkingDay): Observable<EntityResponseType> {
    return this.http.post<IWorkingDay>(this.resourceUrl, workingDay, { observe: 'response' });
  }

  update(workingDay: IWorkingDay): Observable<EntityResponseType> {
    return this.http.put<IWorkingDay>(`${this.resourceUrl}/${getWorkingDayIdentifier(workingDay) as number}`, workingDay, {
      observe: 'response',
    });
  }

  partialUpdate(workingDay: IWorkingDay): Observable<EntityResponseType> {
    return this.http.patch<IWorkingDay>(`${this.resourceUrl}/${getWorkingDayIdentifier(workingDay) as number}`, workingDay, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWorkingDay>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWorkingDay[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addWorkingDayToCollectionIfMissing(
    workingDayCollection: IWorkingDay[],
    ...workingDaysToCheck: (IWorkingDay | null | undefined)[]
  ): IWorkingDay[] {
    const workingDays: IWorkingDay[] = workingDaysToCheck.filter(isPresent);
    if (workingDays.length > 0) {
      const workingDayCollectionIdentifiers = workingDayCollection.map(workingDayItem => getWorkingDayIdentifier(workingDayItem)!);
      const workingDaysToAdd = workingDays.filter(workingDayItem => {
        const workingDayIdentifier = getWorkingDayIdentifier(workingDayItem);
        if (workingDayIdentifier == null || workingDayCollectionIdentifiers.includes(workingDayIdentifier)) {
          return false;
        }
        workingDayCollectionIdentifiers.push(workingDayIdentifier);
        return true;
      });
      return [...workingDaysToAdd, ...workingDayCollection];
    }
    return workingDayCollection;
  }
}
