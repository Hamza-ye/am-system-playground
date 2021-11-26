import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IChvTeam, ChvTeam } from '../chv-team.model';
import { ChvTeamService } from '../service/chv-team.service';

@Injectable({ providedIn: 'root' })
export class ChvTeamRoutingResolveService implements Resolve<IChvTeam> {
  constructor(protected service: ChvTeamService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IChvTeam> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((chvTeam: HttpResponse<ChvTeam>) => {
          if (chvTeam.body) {
            return of(chvTeam.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ChvTeam());
  }
}
