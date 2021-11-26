import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IChvMalariaReportVersion1, ChvMalariaReportVersion1 } from '../chv-malaria-report-version-1.model';
import { ChvMalariaReportVersion1Service } from '../service/chv-malaria-report-version-1.service';

@Injectable({ providedIn: 'root' })
export class ChvMalariaReportVersion1RoutingResolveService implements Resolve<IChvMalariaReportVersion1> {
  constructor(protected service: ChvMalariaReportVersion1Service, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IChvMalariaReportVersion1> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((chvMalariaReportVersion1: HttpResponse<ChvMalariaReportVersion1>) => {
          if (chvMalariaReportVersion1.body) {
            return of(chvMalariaReportVersion1.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ChvMalariaReportVersion1());
  }
}
