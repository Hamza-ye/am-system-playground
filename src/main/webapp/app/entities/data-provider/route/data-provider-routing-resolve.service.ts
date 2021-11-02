import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDataProvider, DataProvider } from '../data-provider.model';
import { DataProviderService } from '../service/data-provider.service';

@Injectable({ providedIn: 'root' })
export class DataProviderRoutingResolveService implements Resolve<IDataProvider> {
  constructor(protected service: DataProviderService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDataProvider> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dataProvider: HttpResponse<DataProvider>) => {
          if (dataProvider.body) {
            return of(dataProvider.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DataProvider());
  }
}
