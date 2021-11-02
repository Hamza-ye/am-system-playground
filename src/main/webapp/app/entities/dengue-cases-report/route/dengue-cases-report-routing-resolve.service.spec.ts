jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDengueCasesReport, DengueCasesReport } from '../dengue-cases-report.model';
import { DengueCasesReportService } from '../service/dengue-cases-report.service';

import { DengueCasesReportRoutingResolveService } from './dengue-cases-report-routing-resolve.service';

describe('Service Tests', () => {
  describe('DengueCasesReport routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DengueCasesReportRoutingResolveService;
    let service: DengueCasesReportService;
    let resultDengueCasesReport: IDengueCasesReport | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DengueCasesReportRoutingResolveService);
      service = TestBed.inject(DengueCasesReportService);
      resultDengueCasesReport = undefined;
    });

    describe('resolve', () => {
      it('should return IDengueCasesReport returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDengueCasesReport = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDengueCasesReport).toEqual({ id: 123 });
      });

      it('should return new IDengueCasesReport if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDengueCasesReport = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDengueCasesReport).toEqual(new DengueCasesReport());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DengueCasesReport })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDengueCasesReport = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDengueCasesReport).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
