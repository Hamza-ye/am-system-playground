import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILLINSFamilyReportHistory, LLINSFamilyReportHistory } from '../llins-family-report-history.model';
import { LLINSFamilyReportHistoryService } from '../service/llins-family-report-history.service';

@Injectable({ providedIn: 'root' })
export class LLINSFamilyReportHistoryRoutingResolveService implements Resolve<ILLINSFamilyReportHistory> {
  constructor(protected service: LLINSFamilyReportHistoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILLINSFamilyReportHistory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((lLINSFamilyReportHistory: HttpResponse<LLINSFamilyReportHistory>) => {
          if (lLINSFamilyReportHistory.body) {
            return of(lLINSFamilyReportHistory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LLINSFamilyReportHistory());
  }
}
