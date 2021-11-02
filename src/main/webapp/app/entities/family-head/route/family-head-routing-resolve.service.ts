import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFamilyHead, FamilyHead } from '../family-head.model';
import { FamilyHeadService } from '../service/family-head.service';

@Injectable({ providedIn: 'root' })
export class FamilyHeadRoutingResolveService implements Resolve<IFamilyHead> {
  constructor(protected service: FamilyHeadService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFamilyHead> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((familyHead: HttpResponse<FamilyHead>) => {
          if (familyHead.body) {
            return of(familyHead.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FamilyHead());
  }
}
