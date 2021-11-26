jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICHVMalariaCaseReport, CHVMalariaCaseReport } from '../chv-malaria-case-report.model';
import { CHVMalariaCaseReportService } from '../service/chv-malaria-case-report.service';

import { CHVMalariaCaseReportRoutingResolveService } from './chv-malaria-case-report-routing-resolve.service';

describe('Service Tests', () => {
  describe('ChvMalariaCaseReport routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CHVMalariaCaseReportRoutingResolveService;
    let service: CHVMalariaCaseReportService;
    let resultCHVMalariaCaseReport: ICHVMalariaCaseReport | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CHVMalariaCaseReportRoutingResolveService);
      service = TestBed.inject(CHVMalariaCaseReportService);
      resultCHVMalariaCaseReport = undefined;
    });

    describe('resolve', () => {
      it('should return ICHVMalariaCaseReport returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCHVMalariaCaseReport = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCHVMalariaCaseReport).toEqual({ id: 123 });
      });

      it('should return new ICHVMalariaCaseReport if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCHVMalariaCaseReport = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCHVMalariaCaseReport).toEqual(new CHVMalariaCaseReport());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CHVMalariaCaseReport })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCHVMalariaCaseReport = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCHVMalariaCaseReport).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
