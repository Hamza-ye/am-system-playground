import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDemographicDataSource, DemographicDataSource } from '../demographic-data-source.model';
import { DemographicDataSourceService } from '../service/demographic-data-source.service';

@Injectable({ providedIn: 'root' })
export class DemographicDataSourceRoutingResolveService implements Resolve<IDemographicDataSource> {
  constructor(protected service: DemographicDataSourceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDemographicDataSource> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((demographicDataSource: HttpResponse<DemographicDataSource>) => {
          if (demographicDataSource.body) {
            return of(demographicDataSource.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DemographicDataSource());
  }
}
