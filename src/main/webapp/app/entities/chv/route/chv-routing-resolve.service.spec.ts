jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICHV, CHV } from '../chv.model';
import { CHVService } from '../service/chv.service';

import { CHVRoutingResolveService } from './chv-routing-resolve.service';

describe('Service Tests', () => {
  describe('Chv routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CHVRoutingResolveService;
    let service: CHVService;
    let resultCHV: ICHV | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CHVRoutingResolveService);
      service = TestBed.inject(CHVService);
      resultCHV = undefined;
    });

    describe('resolve', () => {
      it('should return ICHV returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCHV = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCHV).toEqual({ id: 123 });
      });

      it('should return new ICHV if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCHV = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCHV).toEqual(new CHV());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CHV })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCHV = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCHV).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
