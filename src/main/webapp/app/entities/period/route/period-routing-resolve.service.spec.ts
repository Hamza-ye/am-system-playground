jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPeriod, Period } from '../period.model';
import { PeriodService } from '../service/period.service';

import { PeriodRoutingResolveService } from './period-routing-resolve.service';

describe('Service Tests', () => {
  describe('Period routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PeriodRoutingResolveService;
    let service: PeriodService;
    let resultPeriod: IPeriod | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PeriodRoutingResolveService);
      service = TestBed.inject(PeriodService);
      resultPeriod = undefined;
    });

    describe('resolve', () => {
      it('should return IPeriod returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPeriod = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPeriod).toEqual({ id: 123 });
      });

      it('should return new IPeriod if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPeriod = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPeriod).toEqual(new Period());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Period })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPeriod = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPeriod).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
