import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDataSet, DataSet } from '../data-set.model';
import { DataSetService } from '../service/data-set.service';

@Injectable({ providedIn: 'root' })
export class DataSetRoutingResolveService implements Resolve<IDataSet> {
  constructor(protected service: DataSetService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDataSet> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dataSet: HttpResponse<DataSet>) => {
          if (dataSet.body) {
            return of(dataSet.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DataSet());
  }
}
