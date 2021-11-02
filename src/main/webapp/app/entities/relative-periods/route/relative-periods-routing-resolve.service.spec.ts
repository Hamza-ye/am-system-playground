jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRelativePeriods, RelativePeriods } from '../relative-periods.model';
import { RelativePeriodsService } from '../service/relative-periods.service';

import { RelativePeriodsRoutingResolveService } from './relative-periods-routing-resolve.service';

describe('Service Tests', () => {
  describe('RelativePeriods routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: RelativePeriodsRoutingResolveService;
    let service: RelativePeriodsService;
    let resultRelativePeriods: IRelativePeriods | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(RelativePeriodsRoutingResolveService);
      service = TestBed.inject(RelativePeriodsService);
      resultRelativePeriods = undefined;
    });

    describe('resolve', () => {
      it('should return IRelativePeriods returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRelativePeriods = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRelativePeriods).toEqual({ id: 123 });
      });

      it('should return new IRelativePeriods if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRelativePeriods = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultRelativePeriods).toEqual(new RelativePeriods());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as RelativePeriods })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRelativePeriods = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRelativePeriods).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
