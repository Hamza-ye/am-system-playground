jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDemographicData, DemographicData } from '../demographic-data.model';
import { DemographicDataService } from '../service/demographic-data.service';

import { DemographicDataRoutingResolveService } from './demographic-data-routing-resolve.service';

describe('Service Tests', () => {
  describe('DemographicData routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DemographicDataRoutingResolveService;
    let service: DemographicDataService;
    let resultDemographicData: IDemographicData | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DemographicDataRoutingResolveService);
      service = TestBed.inject(DemographicDataService);
      resultDemographicData = undefined;
    });

    describe('resolve', () => {
      it('should return IDemographicData returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDemographicData = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDemographicData).toEqual({ id: 123 });
      });

      it('should return new IDemographicData if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDemographicData = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDemographicData).toEqual(new DemographicData());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DemographicData })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDemographicData = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDemographicData).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
