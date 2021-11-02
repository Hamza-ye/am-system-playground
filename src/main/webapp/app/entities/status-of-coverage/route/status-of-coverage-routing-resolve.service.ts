import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStatusOfCoverage, StatusOfCoverage } from '../status-of-coverage.model';
import { StatusOfCoverageService } from '../service/status-of-coverage.service';

@Injectable({ providedIn: 'root' })
export class StatusOfCoverageRoutingResolveService implements Resolve<IStatusOfCoverage> {
  constructor(protected service: StatusOfCoverageService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStatusOfCoverage> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((statusOfCoverage: HttpResponse<StatusOfCoverage>) => {
          if (statusOfCoverage.body) {
            return of(statusOfCoverage.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new StatusOfCoverage());
  }
}
