jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDataInputPeriod, DataInputPeriod } from '../data-input-period.model';
import { DataInputPeriodService } from '../service/data-input-period.service';

import { DataInputPeriodRoutingResolveService } from './data-input-period-routing-resolve.service';

describe('Service Tests', () => {
  describe('DataInputPeriod routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DataInputPeriodRoutingResolveService;
    let service: DataInputPeriodService;
    let resultDataInputPeriod: IDataInputPeriod | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DataInputPeriodRoutingResolveService);
      service = TestBed.inject(DataInputPeriodService);
      resultDataInputPeriod = undefined;
    });

    describe('resolve', () => {
      it('should return IDataInputPeriod returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDataInputPeriod = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDataInputPeriod).toEqual({ id: 123 });
      });

      it('should return new IDataInputPeriod if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDataInputPeriod = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDataInputPeriod).toEqual(new DataInputPeriod());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DataInputPeriod })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDataInputPeriod = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDataInputPeriod).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
