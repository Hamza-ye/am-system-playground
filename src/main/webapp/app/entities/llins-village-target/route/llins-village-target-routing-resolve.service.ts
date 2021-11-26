import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILlinsVillageTarget, LlinsVillageTarget } from '../llins-village-target.model';
import { LlinsVillageTargetService } from '../service/llins-village-target.service';

@Injectable({ providedIn: 'root' })
export class LlinsVillageTargetRoutingResolveService implements Resolve<ILlinsVillageTarget> {
  constructor(protected service: LlinsVillageTargetService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILlinsVillageTarget> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((llinsVillageTarget: HttpResponse<LlinsVillageTarget>) => {
          if (llinsVillageTarget.body) {
            return of(llinsVillageTarget.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LlinsVillageTarget());
  }
}
