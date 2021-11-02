import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFingerprint, Fingerprint } from '../fingerprint.model';
import { FingerprintService } from '../service/fingerprint.service';

@Injectable({ providedIn: 'root' })
export class FingerprintRoutingResolveService implements Resolve<IFingerprint> {
  constructor(protected service: FingerprintService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFingerprint> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fingerprint: HttpResponse<Fingerprint>) => {
          if (fingerprint.body) {
            return of(fingerprint.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Fingerprint());
  }
}
