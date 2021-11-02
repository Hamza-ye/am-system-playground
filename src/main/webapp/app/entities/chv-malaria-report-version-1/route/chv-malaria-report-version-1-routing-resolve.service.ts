import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICHVMalariaReportVersion1, CHVMalariaReportVersion1 } from '../chv-malaria-report-version-1.model';
import { CHVMalariaReportVersion1Service } from '../service/chv-malaria-report-version-1.service';

@Injectable({ providedIn: 'root' })
export class CHVMalariaReportVersion1RoutingResolveService implements Resolve<ICHVMalariaReportVersion1> {
  constructor(protected service: CHVMalariaReportVersion1Service, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICHVMalariaReportVersion1> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cHVMalariaReportVersion1: HttpResponse<CHVMalariaReportVersion1>) => {
          if (cHVMalariaReportVersion1.body) {
            return of(cHVMalariaReportVersion1.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CHVMalariaReportVersion1());
  }
}
