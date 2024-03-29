import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPeriodType, PeriodType } from '../period-type.model';
import { PeriodTypeService } from '../service/period-type.service';

@Injectable({ providedIn: 'root' })
export class PeriodTypeRoutingResolveService implements Resolve<IPeriodType> {
  constructor(protected service: PeriodTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPeriodType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((periodType: HttpResponse<PeriodType>) => {
          if (periodType.body) {
            return of(periodType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PeriodType());
  }
}
