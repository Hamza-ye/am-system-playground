import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILlinsFamilyReport, LlinsFamilyReport } from '../llins-family-report.model';
import { LlinsFamilyReportService } from '../service/llins-family-report.service';

@Injectable({ providedIn: 'root' })
export class LlinsFamilyReportRoutingResolveService implements Resolve<ILlinsFamilyReport> {
  constructor(protected service: LlinsFamilyReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILlinsFamilyReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((llinsFamilyReport: HttpResponse<LlinsFamilyReport>) => {
          if (llinsFamilyReport.body) {
            return of(llinsFamilyReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LlinsFamilyReport());
  }
}
