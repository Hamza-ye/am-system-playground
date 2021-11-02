jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPeriodType, PeriodType } from '../period-type.model';
import { PeriodTypeService } from '../service/period-type.service';

import { PeriodTypeRoutingResolveService } from './period-type-routing-resolve.service';

describe('Service Tests', () => {
  describe('PeriodType routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PeriodTypeRoutingResolveService;
    let service: PeriodTypeService;
    let resultPeriodType: IPeriodType | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PeriodTypeRoutingResolveService);
      service = TestBed.inject(PeriodTypeService);
      resultPeriodType = undefined;
    });

    describe('resolve', () => {
      it('should return IPeriodType returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPeriodType = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPeriodType).toEqual({ id: 123 });
      });

      it('should return new IPeriodType if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPeriodType = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPeriodType).toEqual(new PeriodType());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PeriodType })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPeriodType = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPeriodType).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
