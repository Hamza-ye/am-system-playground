import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILlinsFamilyTarget, LlinsFamilyTarget } from '../llins-family-target.model';
import { LlinsFamilyTargetService } from '../service/llins-family-target.service';

@Injectable({ providedIn: 'root' })
export class LlinsFamilyTargetRoutingResolveService implements Resolve<ILlinsFamilyTarget> {
  constructor(protected service: LlinsFamilyTargetService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILlinsFamilyTarget> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((llinsFamilyTarget: HttpResponse<LlinsFamilyTarget>) => {
          if (llinsFamilyTarget.body) {
            return of(llinsFamilyTarget.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LlinsFamilyTarget());
  }
}
