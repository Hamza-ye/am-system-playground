import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProject, Project } from '../../../../entities/project/project.model';
import { ProjectService } from '../service/project.service';

@Injectable({ providedIn: 'root' })
export class ProjectPageRoutingResolveService implements Resolve<IProject> {
  constructor(protected service: ProjectService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProject> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((project: HttpResponse<Project>) => {
          if (project.body) {
            return of(project.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Project());
  }
}
