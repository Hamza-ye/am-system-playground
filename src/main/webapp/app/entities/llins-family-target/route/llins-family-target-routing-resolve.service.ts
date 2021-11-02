import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILLINSFamilyTarget, LLINSFamilyTarget } from '../llins-family-target.model';
import { LLINSFamilyTargetService } from '../service/llins-family-target.service';

@Injectable({ providedIn: 'root' })
export class LLINSFamilyTargetRoutingResolveService implements Resolve<ILLINSFamilyTarget> {
  constructor(protected service: LLINSFamilyTargetService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILLINSFamilyTarget> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((lLINSFamilyTarget: HttpResponse<LLINSFamilyTarget>) => {
          if (lLINSFamilyTarget.body) {
            return of(lLINSFamilyTarget.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LLINSFamilyTarget());
  }
}
