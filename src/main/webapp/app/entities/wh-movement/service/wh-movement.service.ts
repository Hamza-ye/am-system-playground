import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWhMovement, getWhMovementIdentifier } from '../wh-movement.model';

export type EntityResponseType = HttpResponse<IWhMovement>;
export type EntityArrayResponseType = HttpResponse<IWhMovement[]>;

@Injectable({ providedIn: 'root' })
export class WhMovementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/wh-movements');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(whMovement: IWhMovement): Observable<EntityResponseType> {
    return this.http.post<IWhMovement>(this.resourceUrl, whMovement, { observe: 'response' });
  }

  update(whMovement: IWhMovement): Observable<EntityResponseType> {
    return this.http.put<IWhMovement>(`${this.resourceUrl}/${getWhMovementIdentifier(whMovement) as number}`, whMovement, {
      observe: 'response',
    });
  }

  partialUpdate(whMovement: IWhMovement): Observable<EntityResponseType> {
    return this.http.patch<IWhMovement>(`${this.resourceUrl}/${getWhMovementIdentifier(whMovement) as number}`, whMovement, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWhMovement>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWhMovement[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addWhMovementToCollectionIfMissing(
    whMovementCollection: IWhMovement[],
    ...whMovementsToCheck: (IWhMovement | null | undefined)[]
  ): IWhMovement[] {
    const whMovements: IWhMovement[] = whMovementsToCheck.filter(isPresent);
    if (whMovements.length > 0) {
      const whMovementCollectionIdentifiers = whMovementCollection.map(whMovementItem => getWhMovementIdentifier(whMovementItem)!);
      const whMovementsToAdd = whMovements.filter(whMovementItem => {
        const whMovementIdentifier = getWhMovementIdentifier(whMovementItem);
        if (whMovementIdentifier == null || whMovementCollectionIdentifiers.includes(whMovementIdentifier)) {
          return false;
        }
        whMovementCollectionIdentifiers.push(whMovementIdentifier);
        return true;
      });
      return [...whMovementsToAdd, ...whMovementCollection];
    }
    return whMovementCollection;
  }
}
