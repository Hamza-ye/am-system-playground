import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICHVMalariaCaseReport, CHVMalariaCaseReport } from '../chv-malaria-case-report.model';
import { CHVMalariaCaseReportService } from '../service/chv-malaria-case-report.service';

@Injectable({ providedIn: 'root' })
export class CHVMalariaCaseReportRoutingResolveService implements Resolve<ICHVMalariaCaseReport> {
  constructor(protected service: CHVMalariaCaseReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICHVMalariaCaseReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cHVMalariaCaseReport: HttpResponse<CHVMalariaCaseReport>) => {
          if (cHVMalariaCaseReport.body) {
            return of(cHVMalariaCaseReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CHVMalariaCaseReport());
  }
}
