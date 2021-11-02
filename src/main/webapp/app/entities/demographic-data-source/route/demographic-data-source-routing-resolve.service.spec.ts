jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDemographicDataSource, DemographicDataSource } from '../demographic-data-source.model';
import { DemographicDataSourceService } from '../service/demographic-data-source.service';

import { DemographicDataSourceRoutingResolveService } from './demographic-data-source-routing-resolve.service';

describe('Service Tests', () => {
  describe('DemographicDataSource routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DemographicDataSourceRoutingResolveService;
    let service: DemographicDataSourceService;
    let resultDemographicDataSource: IDemographicDataSource | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DemographicDataSourceRoutingResolveService);
      service = TestBed.inject(DemographicDataSourceService);
      resultDemographicDataSource = undefined;
    });

    describe('resolve', () => {
      it('should return IDemographicDataSource returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDemographicDataSource = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDemographicDataSource).toEqual({ id: 123 });
      });

      it('should return new IDemographicDataSource if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDemographicDataSource = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDemographicDataSource).toEqual(new DemographicDataSource());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DemographicDataSource })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDemographicDataSource = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDemographicDataSource).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
