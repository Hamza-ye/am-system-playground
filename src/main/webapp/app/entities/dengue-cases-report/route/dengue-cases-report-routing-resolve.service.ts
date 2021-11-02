import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDengueCasesReport, DengueCasesReport } from '../dengue-cases-report.model';
import { DengueCasesReportService } from '../service/dengue-cases-report.service';

@Injectable({ providedIn: 'root' })
export class DengueCasesReportRoutingResolveService implements Resolve<IDengueCasesReport> {
  constructor(protected service: DengueCasesReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDengueCasesReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dengueCasesReport: HttpResponse<DengueCasesReport>) => {
          if (dengueCasesReport.body) {
            return of(dengueCasesReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DengueCasesReport());
  }
}
