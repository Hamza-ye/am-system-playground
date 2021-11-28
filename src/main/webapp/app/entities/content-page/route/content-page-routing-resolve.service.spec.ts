jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IContentPage, ContentPage } from '../content-page.model';
import { ContentPageService } from '../service/content-page.service';

import { ContentPageRoutingResolveService } from './content-page-routing-resolve.service';

describe('Service Tests', () => {
  describe('ContentPage routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ContentPageRoutingResolveService;
    let service: ContentPageService;
    let resultContentPage: IContentPage | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ContentPageRoutingResolveService);
      service = TestBed.inject(ContentPageService);
      resultContentPage = undefined;
    });

    describe('resolve', () => {
      it('should return IContentPage returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContentPage = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContentPage).toEqual({ id: 123 });
      });

      it('should return new IContentPage if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContentPage = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultContentPage).toEqual(new ContentPage());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ContentPage })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContentPage = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContentPage).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
