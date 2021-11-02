jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILLINSFamilyReportHistory, LLINSFamilyReportHistory } from '../llins-family-report-history.model';
import { LLINSFamilyReportHistoryService } from '../service/llins-family-report-history.service';

import { LLINSFamilyReportHistoryRoutingResolveService } from './llins-family-report-history-routing-resolve.service';

describe('Service Tests', () => {
  describe('LLINSFamilyReportHistory routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LLINSFamilyReportHistoryRoutingResolveService;
    let service: LLINSFamilyReportHistoryService;
    let resultLLINSFamilyReportHistory: ILLINSFamilyReportHistory | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LLINSFamilyReportHistoryRoutingResolveService);
      service = TestBed.inject(LLINSFamilyReportHistoryService);
      resultLLINSFamilyReportHistory = undefined;
    });

    describe('resolve', () => {
      it('should return ILLINSFamilyReportHistory returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLLINSFamilyReportHistory = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLLINSFamilyReportHistory).toEqual({ id: 123 });
      });

      it('should return new ILLINSFamilyReportHistory if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLLINSFamilyReportHistory = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLLINSFamilyReportHistory).toEqual(new LLINSFamilyReportHistory());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LLINSFamilyReportHistory })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLLINSFamilyReportHistory = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLLINSFamilyReportHistory).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
