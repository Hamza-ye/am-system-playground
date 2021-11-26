import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IChv, Chv } from '../chv.model';
import { ChvService } from '../service/chv.service';

@Injectable({ providedIn: 'root' })
export class ChvRoutingResolveService implements Resolve<IChv> {
  constructor(protected service: ChvService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IChv> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((chv: HttpResponse<Chv>) => {
          if (chv.body) {
            return of(chv.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Chv());
  }
}
