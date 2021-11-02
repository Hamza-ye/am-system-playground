import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICHV, CHV } from '../chv.model';
import { CHVService } from '../service/chv.service';

@Injectable({ providedIn: 'root' })
export class CHVRoutingResolveService implements Resolve<ICHV> {
  constructor(protected service: CHVService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICHV> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cHV: HttpResponse<CHV>) => {
          if (cHV.body) {
            return of(cHV.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CHV());
  }
}
