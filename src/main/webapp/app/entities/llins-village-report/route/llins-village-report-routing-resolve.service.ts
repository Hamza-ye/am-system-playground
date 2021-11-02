import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILLINSVillageReport, LLINSVillageReport } from '../llins-village-report.model';
import { LLINSVillageReportService } from '../service/llins-village-report.service';

@Injectable({ providedIn: 'root' })
export class LLINSVillageReportRoutingResolveService implements Resolve<ILLINSVillageReport> {
  constructor(protected service: LLINSVillageReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILLINSVillageReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((lLINSVillageReport: HttpResponse<LLINSVillageReport>) => {
          if (lLINSVillageReport.body) {
            return of(lLINSVillageReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LLINSVillageReport());
  }
}
