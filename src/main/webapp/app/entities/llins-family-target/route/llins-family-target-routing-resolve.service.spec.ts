jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILlinsFamilyTarget, LlinsFamilyTarget } from '../llins-family-target.model';
import { LlinsFamilyTargetService } from '../service/llins-family-target.service';

import { LlinsFamilyTargetRoutingResolveService } from './llins-family-target-routing-resolve.service';

describe('Service Tests', () => {
  describe('LlinsFamilyTarget routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LlinsFamilyTargetRoutingResolveService;
    let service: LlinsFamilyTargetService;
    let resultLlinsFamilyTarget: ILlinsFamilyTarget | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LlinsFamilyTargetRoutingResolveService);
      service = TestBed.inject(LlinsFamilyTargetService);
      resultLlinsFamilyTarget = undefined;
    });

    describe('resolve', () => {
      it('should return ILlinsFamilyTarget returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLlinsFamilyTarget = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLlinsFamilyTarget).toEqual({ id: 123 });
      });

      it('should return new ILlinsFamilyTarget if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLlinsFamilyTarget = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLlinsFamilyTarget).toEqual(new LlinsFamilyTarget());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LlinsFamilyTarget })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLlinsFamilyTarget = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLlinsFamilyTarget).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
