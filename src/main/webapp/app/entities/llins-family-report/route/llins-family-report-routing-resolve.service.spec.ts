jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILlinsFamilyReport, LlinsFamilyReport } from '../llins-family-report.model';
import { LlinsFamilyReportService } from '../service/llins-family-report.service';

import { LlinsFamilyReportRoutingResolveService } from './llins-family-report-routing-resolve.service';

describe('Service Tests', () => {
  describe('LlinsFamilyReport routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LlinsFamilyReportRoutingResolveService;
    let service: LlinsFamilyReportService;
    let resultLlinsFamilyReport: ILlinsFamilyReport | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LlinsFamilyReportRoutingResolveService);
      service = TestBed.inject(LlinsFamilyReportService);
      resultLlinsFamilyReport = undefined;
    });

    describe('resolve', () => {
      it('should return ILlinsFamilyReport returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLlinsFamilyReport = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLlinsFamilyReport).toEqual({ id: 123 });
      });

      it('should return new ILlinsFamilyReport if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLlinsFamilyReport = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLlinsFamilyReport).toEqual(new LlinsFamilyReport());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LlinsFamilyReport })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLlinsFamilyReport = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLlinsFamilyReport).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
