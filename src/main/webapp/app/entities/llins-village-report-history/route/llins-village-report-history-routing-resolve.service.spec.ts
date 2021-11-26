jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILlinsVillageReportHistory, LlinsVillageReportHistory } from '../llins-village-report-history.model';
import { LlinsVillageReportHistoryService } from '../service/llins-village-report-history.service';

import { LlinsVillageReportHistoryRoutingResolveService } from './llins-village-report-history-routing-resolve.service';

describe('Service Tests', () => {
  describe('LlinsVillageReportHistory routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LlinsVillageReportHistoryRoutingResolveService;
    let service: LlinsVillageReportHistoryService;
    let resultLlinsVillageReportHistory: ILlinsVillageReportHistory | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LlinsVillageReportHistoryRoutingResolveService);
      service = TestBed.inject(LlinsVillageReportHistoryService);
      resultLlinsVillageReportHistory = undefined;
    });

    describe('resolve', () => {
      it('should return ILlinsVillageReportHistory returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLlinsVillageReportHistory = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLlinsVillageReportHistory).toEqual({ id: 123 });
      });

      it('should return new ILlinsVillageReportHistory if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLlinsVillageReportHistory = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLlinsVillageReportHistory).toEqual(new LlinsVillageReportHistory());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LlinsVillageReportHistory })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLlinsVillageReportHistory = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLlinsVillageReportHistory).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
