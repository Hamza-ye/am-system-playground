import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILlinsFamilyReportHistory, LlinsFamilyReportHistory } from '../llins-family-report-history.model';
import { LlinsFamilyReportHistoryService } from '../service/llins-family-report-history.service';

@Injectable({ providedIn: 'root' })
export class LlinsFamilyReportHistoryRoutingResolveService implements Resolve<ILlinsFamilyReportHistory> {
  constructor(protected service: LlinsFamilyReportHistoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILlinsFamilyReportHistory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((llinsFamilyReportHistory: HttpResponse<LlinsFamilyReportHistory>) => {
          if (llinsFamilyReportHistory.body) {
            return of(llinsFamilyReportHistory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LlinsFamilyReportHistory());
  }
}
