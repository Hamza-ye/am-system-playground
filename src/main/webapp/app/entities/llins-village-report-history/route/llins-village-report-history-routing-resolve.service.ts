import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILLINSVillageReportHistory, LLINSVillageReportHistory } from '../llins-village-report-history.model';
import { LLINSVillageReportHistoryService } from '../service/llins-village-report-history.service';

@Injectable({ providedIn: 'root' })
export class LLINSVillageReportHistoryRoutingResolveService implements Resolve<ILLINSVillageReportHistory> {
  constructor(protected service: LLINSVillageReportHistoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILLINSVillageReportHistory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((lLINSVillageReportHistory: HttpResponse<LLINSVillageReportHistory>) => {
          if (lLINSVillageReportHistory.body) {
            return of(lLINSVillageReportHistory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LLINSVillageReportHistory());
  }
}
