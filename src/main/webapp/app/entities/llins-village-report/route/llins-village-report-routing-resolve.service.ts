import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILlinsVillageReport, LlinsVillageReport } from '../llins-village-report.model';
import { LlinsVillageReportService } from '../service/llins-village-report.service';

@Injectable({ providedIn: 'root' })
export class LlinsVillageReportRoutingResolveService implements Resolve<ILlinsVillageReport> {
  constructor(protected service: LlinsVillageReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILlinsVillageReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((llinsVillageReport: HttpResponse<LlinsVillageReport>) => {
          if (llinsVillageReport.body) {
            return of(llinsVillageReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LlinsVillageReport());
  }
}
