jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILLINSVillageReportHistory, LLINSVillageReportHistory } from '../llins-village-report-history.model';
import { LLINSVillageReportHistoryService } from '../service/llins-village-report-history.service';

import { LLINSVillageReportHistoryRoutingResolveService } from './llins-village-report-history-routing-resolve.service';

describe('Service Tests', () => {
  describe('LlinsVillageReportHistory routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LLINSVillageReportHistoryRoutingResolveService;
    let service: LLINSVillageReportHistoryService;
    let resultLLINSVillageReportHistory: ILLINSVillageReportHistory | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LLINSVillageReportHistoryRoutingResolveService);
      service = TestBed.inject(LLINSVillageReportHistoryService);
      resultLLINSVillageReportHistory = undefined;
    });

    describe('resolve', () => {
      it('should return ILLINSVillageReportHistory returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLLINSVillageReportHistory = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLLINSVillageReportHistory).toEqual({ id: 123 });
      });

      it('should return new ILLINSVillageReportHistory if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLLINSVillageReportHistory = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLLINSVillageReportHistory).toEqual(new LLINSVillageReportHistory());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LLINSVillageReportHistory })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLLINSVillageReportHistory = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLLINSVillageReportHistory).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
