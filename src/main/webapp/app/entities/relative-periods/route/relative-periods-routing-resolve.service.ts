import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRelativePeriods, RelativePeriods } from '../relative-periods.model';
import { RelativePeriodsService } from '../service/relative-periods.service';

@Injectable({ providedIn: 'root' })
export class RelativePeriodsRoutingResolveService implements Resolve<IRelativePeriods> {
  constructor(protected service: RelativePeriodsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRelativePeriods> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((relativePeriods: HttpResponse<RelativePeriods>) => {
          if (relativePeriods.body) {
            return of(relativePeriods.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RelativePeriods());
  }
}
