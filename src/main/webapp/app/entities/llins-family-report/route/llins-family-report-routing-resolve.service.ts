import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILLINSFamilyReport, LLINSFamilyReport } from '../llins-family-report.model';
import { LLINSFamilyReportService } from '../service/llins-family-report.service';

@Injectable({ providedIn: 'root' })
export class LLINSFamilyReportRoutingResolveService implements Resolve<ILLINSFamilyReport> {
  constructor(protected service: LLINSFamilyReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILLINSFamilyReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((lLINSFamilyReport: HttpResponse<LLINSFamilyReport>) => {
          if (lLINSFamilyReport.body) {
            return of(lLINSFamilyReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LLINSFamilyReport());
  }
}
