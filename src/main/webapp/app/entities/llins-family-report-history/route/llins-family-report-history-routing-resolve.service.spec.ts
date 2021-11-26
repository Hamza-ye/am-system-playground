jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILlinsFamilyReportHistory, LlinsFamilyReportHistory } from '../llins-family-report-history.model';
import { LlinsFamilyReportHistoryService } from '../service/llins-family-report-history.service';

import { LlinsFamilyReportHistoryRoutingResolveService } from './llins-family-report-history-routing-resolve.service';

describe('Service Tests', () => {
  describe('LlinsFamilyReportHistory routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LlinsFamilyReportHistoryRoutingResolveService;
    let service: LlinsFamilyReportHistoryService;
    let resultLlinsFamilyReportHistory: ILlinsFamilyReportHistory | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LlinsFamilyReportHistoryRoutingResolveService);
      service = TestBed.inject(LlinsFamilyReportHistoryService);
      resultLlinsFamilyReportHistory = undefined;
    });

    describe('resolve', () => {
      it('should return ILlinsFamilyReportHistory returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLlinsFamilyReportHistory = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLlinsFamilyReportHistory).toEqual({ id: 123 });
      });

      it('should return new ILlinsFamilyReportHistory if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLlinsFamilyReportHistory = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLlinsFamilyReportHistory).toEqual(new LlinsFamilyReportHistory());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LlinsFamilyReportHistory })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLlinsFamilyReportHistory = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLlinsFamilyReportHistory).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
