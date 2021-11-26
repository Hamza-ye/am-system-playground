jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICHVMalariaReportVersion1, CHVMalariaReportVersion1 } from '../chv-malaria-report-version-1.model';
import { CHVMalariaReportVersion1Service } from '../service/chv-malaria-report-version-1.service';

import { CHVMalariaReportVersion1RoutingResolveService } from './chv-malaria-report-version-1-routing-resolve.service';

describe('Service Tests', () => {
  describe('ChvMalariaReportVersion1 routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CHVMalariaReportVersion1RoutingResolveService;
    let service: CHVMalariaReportVersion1Service;
    let resultCHVMalariaReportVersion1: ICHVMalariaReportVersion1 | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CHVMalariaReportVersion1RoutingResolveService);
      service = TestBed.inject(CHVMalariaReportVersion1Service);
      resultCHVMalariaReportVersion1 = undefined;
    });

    describe('resolve', () => {
      it('should return ICHVMalariaReportVersion1 returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCHVMalariaReportVersion1 = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCHVMalariaReportVersion1).toEqual({ id: 123 });
      });

      it('should return new ICHVMalariaReportVersion1 if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCHVMalariaReportVersion1 = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCHVMalariaReportVersion1).toEqual(new CHVMalariaReportVersion1());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CHVMalariaReportVersion1 })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCHVMalariaReportVersion1 = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCHVMalariaReportVersion1).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
