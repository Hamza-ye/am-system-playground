jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMalariaCasesReport, MalariaCasesReport } from '../malaria-cases-report.model';
import { MalariaCasesReportService } from '../service/malaria-cases-report.service';

import { MalariaCasesReportRoutingResolveService } from './malaria-cases-report-routing-resolve.service';

describe('Service Tests', () => {
  describe('MalariaCasesReport routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: MalariaCasesReportRoutingResolveService;
    let service: MalariaCasesReportService;
    let resultMalariaCasesReport: IMalariaCasesReport | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(MalariaCasesReportRoutingResolveService);
      service = TestBed.inject(MalariaCasesReportService);
      resultMalariaCasesReport = undefined;
    });

    describe('resolve', () => {
      it('should return IMalariaCasesReport returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMalariaCasesReport = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMalariaCasesReport).toEqual({ id: 123 });
      });

      it('should return new IMalariaCasesReport if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMalariaCasesReport = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMalariaCasesReport).toEqual(new MalariaCasesReport());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as MalariaCasesReport })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMalariaCasesReport = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMalariaCasesReport).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
