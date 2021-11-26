import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILlinsVillageReportHistory, LlinsVillageReportHistory } from '../llins-village-report-history.model';
import { LlinsVillageReportHistoryService } from '../service/llins-village-report-history.service';

@Injectable({ providedIn: 'root' })
export class LlinsVillageReportHistoryRoutingResolveService implements Resolve<ILlinsVillageReportHistory> {
  constructor(protected service: LlinsVillageReportHistoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILlinsVillageReportHistory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((llinsVillageReportHistory: HttpResponse<LlinsVillageReportHistory>) => {
          if (llinsVillageReportHistory.body) {
            return of(llinsVillageReportHistory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LlinsVillageReportHistory());
  }
}
