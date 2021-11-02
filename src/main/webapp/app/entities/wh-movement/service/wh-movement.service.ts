import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWHMovement, getWHMovementIdentifier } from '../wh-movement.model';

export type EntityResponseType = HttpResponse<IWHMovement>;
export type EntityArrayResponseType = HttpResponse<IWHMovement[]>;

@Injectable({ providedIn: 'root' })
export class WHMovementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/wh-movements');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(wHMovement: IWHMovement): Observable<EntityResponseType> {
    return this.http.post<IWHMovement>(this.resourceUrl, wHMovement, { observe: 'response' });
  }

  update(wHMovement: IWHMovement): Observable<EntityResponseType> {
    return this.http.put<IWHMovement>(`${this.resourceUrl}/${getWHMovementIdentifier(wHMovement) as number}`, wHMovement, {
      observe: 'response',
    });
  }

  partialUpdate(wHMovement: IWHMovement): Observable<EntityResponseType> {
    return this.http.patch<IWHMovement>(`${this.resourceUrl}/${getWHMovementIdentifier(wHMovement) as number}`, wHMovement, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWHMovement>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWHMovement[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addWHMovementToCollectionIfMissing(
    wHMovementCollection: IWHMovement[],
    ...wHMovementsToCheck: (IWHMovement | null | undefined)[]
  ): IWHMovement[] {
    const wHMovements: IWHMovement[] = wHMovementsToCheck.filter(isPresent);
    if (wHMovements.length > 0) {
      const wHMovementCollectionIdentifiers = wHMovementCollection.map(wHMovementItem => getWHMovementIdentifier(wHMovementItem)!);
      const wHMovementsToAdd = wHMovements.filter(wHMovementItem => {
        const wHMovementIdentifier = getWHMovementIdentifier(wHMovementItem);
        if (wHMovementIdentifier == null || wHMovementCollectionIdentifiers.includes(wHMovementIdentifier)) {
          return false;
        }
        wHMovementCollectionIdentifiers.push(wHMovementIdentifier);
        return true;
      });
      return [...wHMovementsToAdd, ...wHMovementCollection];
    }
    return wHMovementCollection;
  }
}
