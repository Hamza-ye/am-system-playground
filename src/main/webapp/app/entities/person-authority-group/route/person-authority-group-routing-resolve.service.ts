import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPersonAuthorityGroup, PersonAuthorityGroup } from '../person-authority-group.model';
import { PersonAuthorityGroupService } from '../service/person-authority-group.service';

@Injectable({ providedIn: 'root' })
export class PersonAuthorityGroupRoutingResolveService implements Resolve<IPersonAuthorityGroup> {
  constructor(protected service: PersonAuthorityGroupService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPersonAuthorityGroup> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((personAuthorityGroup: HttpResponse<PersonAuthorityGroup>) => {
          if (personAuthorityGroup.body) {
            return of(personAuthorityGroup.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PersonAuthorityGroup());
  }
}
