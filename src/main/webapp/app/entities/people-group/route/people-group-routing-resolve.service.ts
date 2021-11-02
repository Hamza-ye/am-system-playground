import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPeopleGroup, PeopleGroup } from '../people-group.model';
import { PeopleGroupService } from '../service/people-group.service';

@Injectable({ providedIn: 'root' })
export class PeopleGroupRoutingResolveService implements Resolve<IPeopleGroup> {
  constructor(protected service: PeopleGroupService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPeopleGroup> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((peopleGroup: HttpResponse<PeopleGroup>) => {
          if (peopleGroup.body) {
            return of(peopleGroup.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PeopleGroup());
  }
}
