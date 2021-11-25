import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFileResource, FileResource } from '../file-resource.model';
import { FileResourceService } from '../service/file-resource.service';

@Injectable({ providedIn: 'root' })
export class FileResourceRoutingResolveService implements Resolve<IFileResource> {
  constructor(protected service: FileResourceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFileResource> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fileResource: HttpResponse<FileResource>) => {
          if (fileResource.body) {
            return of(fileResource.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FileResource());
  }
}
