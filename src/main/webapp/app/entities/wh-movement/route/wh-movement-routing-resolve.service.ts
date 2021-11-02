import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWHMovement, WHMovement } from '../wh-movement.model';
import { WHMovementService } from '../service/wh-movement.service';

@Injectable({ providedIn: 'root' })
export class WHMovementRoutingResolveService implements Resolve<IWHMovement> {
  constructor(protected service: WHMovementService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWHMovement> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((wHMovement: HttpResponse<WHMovement>) => {
          if (wHMovement.body) {
            return of(wHMovement.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new WHMovement());
  }
}
