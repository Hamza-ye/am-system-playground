import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExternalFileResource, ExternalFileResource } from '../external-file-resource.model';
import { ExternalFileResourceService } from '../service/external-file-resource.service';

@Injectable({ providedIn: 'root' })
export class ExternalFileResourceRoutingResolveService implements Resolve<IExternalFileResource> {
  constructor(protected service: ExternalFileResourceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExternalFileResource> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((externalFileResource: HttpResponse<ExternalFileResource>) => {
          if (externalFileResource.body) {
            return of(externalFileResource.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ExternalFileResource());
  }
}
