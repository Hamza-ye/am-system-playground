import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMalariaUnitStaffMember, MalariaUnitStaffMember } from '../malaria-unit-staff-member.model';
import { MalariaUnitStaffMemberService } from '../service/malaria-unit-staff-member.service';

@Injectable({ providedIn: 'root' })
export class MalariaUnitStaffMemberRoutingResolveService implements Resolve<IMalariaUnitStaffMember> {
  constructor(protected service: MalariaUnitStaffMemberService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMalariaUnitStaffMember> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((malariaUnitStaffMember: HttpResponse<MalariaUnitStaffMember>) => {
          if (malariaUnitStaffMember.body) {
            return of(malariaUnitStaffMember.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MalariaUnitStaffMember());
  }
}
