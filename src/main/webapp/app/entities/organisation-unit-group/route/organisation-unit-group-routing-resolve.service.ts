import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrganisationUnitGroup, OrganisationUnitGroup } from '../organisation-unit-group.model';
import { OrganisationUnitGroupService } from '../service/organisation-unit-group.service';

@Injectable({ providedIn: 'root' })
export class OrganisationUnitGroupRoutingResolveService implements Resolve<IOrganisationUnitGroup> {
  constructor(protected service: OrganisationUnitGroupService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrganisationUnitGroup> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((organisationUnitGroup: HttpResponse<OrganisationUnitGroup>) => {
          if (organisationUnitGroup.body) {
            return of(organisationUnitGroup.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OrganisationUnitGroup());
  }
}
