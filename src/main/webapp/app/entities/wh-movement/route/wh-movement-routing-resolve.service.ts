import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWhMovement, WhMovement } from '../wh-movement.model';
import { WhMovementService } from '../service/wh-movement.service';

@Injectable({ providedIn: 'root' })
export class WhMovementRoutingResolveService implements Resolve<IWhMovement> {
  constructor(protected service: WhMovementService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWhMovement> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((whMovement: HttpResponse<WhMovement>) => {
          if (whMovement.body) {
            return of(whMovement.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new WhMovement());
  }
}
