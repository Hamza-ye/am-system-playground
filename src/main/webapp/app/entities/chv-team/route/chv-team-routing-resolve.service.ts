import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICHVTeam, CHVTeam } from '../chv-team.model';
import { CHVTeamService } from '../service/chv-team.service';

@Injectable({ providedIn: 'root' })
export class CHVTeamRoutingResolveService implements Resolve<ICHVTeam> {
  constructor(protected service: CHVTeamService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICHVTeam> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cHVTeam: HttpResponse<CHVTeam>) => {
          if (cHVTeam.body) {
            return of(cHVTeam.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CHVTeam());
  }
}
