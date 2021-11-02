jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICasesReportClass, CasesReportClass } from '../cases-report-class.model';
import { CasesReportClassService } from '../service/cases-report-class.service';

import { CasesReportClassRoutingResolveService } from './cases-report-class-routing-resolve.service';

describe('Service Tests', () => {
  describe('CasesReportClass routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CasesReportClassRoutingResolveService;
    let service: CasesReportClassService;
    let resultCasesReportClass: ICasesReportClass | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CasesReportClassRoutingResolveService);
      service = TestBed.inject(CasesReportClassService);
      resultCasesReportClass = undefined;
    });

    describe('resolve', () => {
      it('should return ICasesReportClass returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCasesReportClass = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCasesReportClass).toEqual({ id: 123 });
      });

      it('should return new ICasesReportClass if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCasesReportClass = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCasesReportClass).toEqual(new CasesReportClass());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CasesReportClass })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCasesReportClass = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCasesReportClass).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
