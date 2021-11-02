jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMalariaUnit, MalariaUnit } from '../malaria-unit.model';
import { MalariaUnitService } from '../service/malaria-unit.service';

import { MalariaUnitRoutingResolveService } from './malaria-unit-routing-resolve.service';

describe('Service Tests', () => {
  describe('MalariaUnit routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: MalariaUnitRoutingResolveService;
    let service: MalariaUnitService;
    let resultMalariaUnit: IMalariaUnit | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(MalariaUnitRoutingResolveService);
      service = TestBed.inject(MalariaUnitService);
      resultMalariaUnit = undefined;
    });

    describe('resolve', () => {
      it('should return IMalariaUnit returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMalariaUnit = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMalariaUnit).toEqual({ id: 123 });
      });

      it('should return new IMalariaUnit if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMalariaUnit = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMalariaUnit).toEqual(new MalariaUnit());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as MalariaUnit })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMalariaUnit = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMalariaUnit).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
