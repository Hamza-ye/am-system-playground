import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRelatedLink, RelatedLink } from '../related-link.model';
import { RelatedLinkService } from '../service/related-link.service';

@Injectable({ providedIn: 'root' })
export class RelatedLinkRoutingResolveService implements Resolve<IRelatedLink> {
  constructor(protected service: RelatedLinkService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRelatedLink> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((relatedLink: HttpResponse<RelatedLink>) => {
          if (relatedLink.body) {
            return of(relatedLink.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RelatedLink());
  }
}
