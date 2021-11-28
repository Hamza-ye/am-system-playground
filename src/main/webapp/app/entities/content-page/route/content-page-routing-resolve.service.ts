import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContentPage, ContentPage } from '../content-page.model';
import { ContentPageService } from '../service/content-page.service';

@Injectable({ providedIn: 'root' })
export class ContentPageRoutingResolveService implements Resolve<IContentPage> {
  constructor(protected service: ContentPageService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContentPage> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contentPage: HttpResponse<ContentPage>) => {
          if (contentPage.body) {
            return of(contentPage.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContentPage());
  }
}
