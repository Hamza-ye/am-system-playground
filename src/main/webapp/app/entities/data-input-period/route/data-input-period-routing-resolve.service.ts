import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDataInputPeriod, DataInputPeriod } from '../data-input-period.model';
import { DataInputPeriodService } from '../service/data-input-period.service';

@Injectable({ providedIn: 'root' })
export class DataInputPeriodRoutingResolveService implements Resolve<IDataInputPeriod> {
  constructor(protected service: DataInputPeriodService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDataInputPeriod> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dataInputPeriod: HttpResponse<DataInputPeriod>) => {
          if (dataInputPeriod.body) {
            return of(dataInputPeriod.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DataInputPeriod());
  }
}
