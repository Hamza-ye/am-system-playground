import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILLINSVillageTarget, LLINSVillageTarget } from '../llins-village-target.model';
import { LLINSVillageTargetService } from '../service/llins-village-target.service';

@Injectable({ providedIn: 'root' })
export class LLINSVillageTargetRoutingResolveService implements Resolve<ILLINSVillageTarget> {
  constructor(protected service: LLINSVillageTargetService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILLINSVillageTarget> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((lLINSVillageTarget: HttpResponse<LLINSVillageTarget>) => {
          if (lLINSVillageTarget.body) {
            return of(lLINSVillageTarget.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LLINSVillageTarget());
  }
}
