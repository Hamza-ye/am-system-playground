import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICasesReportClass, CasesReportClass } from '../cases-report-class.model';
import { CasesReportClassService } from '../service/cases-report-class.service';

@Injectable({ providedIn: 'root' })
export class CasesReportClassRoutingResolveService implements Resolve<ICasesReportClass> {
  constructor(protected service: CasesReportClassService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICasesReportClass> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((casesReportClass: HttpResponse<CasesReportClass>) => {
          if (casesReportClass.body) {
            return of(casesReportClass.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CasesReportClass());
  }
}
