import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMalariaCasesReport, MalariaCasesReport } from '../malaria-cases-report.model';
import { MalariaCasesReportService } from '../service/malaria-cases-report.service';

@Injectable({ providedIn: 'root' })
export class MalariaCasesReportRoutingResolveService implements Resolve<IMalariaCasesReport> {
  constructor(protected service: MalariaCasesReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMalariaCasesReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((malariaCasesReport: HttpResponse<MalariaCasesReport>) => {
          if (malariaCasesReport.body) {
            return of(malariaCasesReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MalariaCasesReport());
  }
}
