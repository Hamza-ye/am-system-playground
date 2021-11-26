jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILLINSFamilyReport, LLINSFamilyReport } from '../llins-family-report.model';
import { LLINSFamilyReportService } from '../service/llins-family-report.service';

import { LLINSFamilyReportRoutingResolveService } from './llins-family-report-routing-resolve.service';

describe('Service Tests', () => {
  describe('LlinsFamilyReport routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LLINSFamilyReportRoutingResolveService;
    let service: LLINSFamilyReportService;
    let resultLLINSFamilyReport: ILLINSFamilyReport | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LLINSFamilyReportRoutingResolveService);
      service = TestBed.inject(LLINSFamilyReportService);
      resultLLINSFamilyReport = undefined;
    });

    describe('resolve', () => {
      it('should return ILLINSFamilyReport returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLLINSFamilyReport = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLLINSFamilyReport).toEqual({ id: 123 });
      });

      it('should return new ILLINSFamilyReport if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLLINSFamilyReport = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLLINSFamilyReport).toEqual(new LLINSFamilyReport());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LLINSFamilyReport })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLLINSFamilyReport = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLLINSFamilyReport).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
