jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILlinsVillageReport, LlinsVillageReport } from '../llins-village-report.model';
import { LlinsVillageReportService } from '../service/llins-village-report.service';

import { LlinsVillageReportRoutingResolveService } from './llins-village-report-routing-resolve.service';

describe('Service Tests', () => {
  describe('LlinsVillageReport routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LlinsVillageReportRoutingResolveService;
    let service: LlinsVillageReportService;
    let resultLlinsVillageReport: ILlinsVillageReport | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LlinsVillageReportRoutingResolveService);
      service = TestBed.inject(LlinsVillageReportService);
      resultLlinsVillageReport = undefined;
    });

    describe('resolve', () => {
      it('should return ILlinsVillageReport returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLlinsVillageReport = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLlinsVillageReport).toEqual({ id: 123 });
      });

      it('should return new ILlinsVillageReport if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLlinsVillageReport = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLlinsVillageReport).toEqual(new LlinsVillageReport());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LlinsVillageReport })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLlinsVillageReport = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLlinsVillageReport).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
