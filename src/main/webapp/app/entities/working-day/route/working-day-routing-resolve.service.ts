import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWorkingDay, WorkingDay } from '../working-day.model';
import { WorkingDayService } from '../service/working-day.service';

@Injectable({ providedIn: 'root' })
export class WorkingDayRoutingResolveService implements Resolve<IWorkingDay> {
  constructor(protected service: WorkingDayService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWorkingDay> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((workingDay: HttpResponse<WorkingDay>) => {
          if (workingDay.body) {
            return of(workingDay.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new WorkingDay());
  }
}
