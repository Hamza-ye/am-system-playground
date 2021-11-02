import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMalariaUnit, MalariaUnit } from '../malaria-unit.model';
import { MalariaUnitService } from '../service/malaria-unit.service';

@Injectable({ providedIn: 'root' })
export class MalariaUnitRoutingResolveService implements Resolve<IMalariaUnit> {
  constructor(protected service: MalariaUnitService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMalariaUnit> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((malariaUnit: HttpResponse<MalariaUnit>) => {
          if (malariaUnit.body) {
            return of(malariaUnit.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MalariaUnit());
  }
}
