jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILLINSFamilyTarget, LLINSFamilyTarget } from '../llins-family-target.model';
import { LLINSFamilyTargetService } from '../service/llins-family-target.service';

import { LLINSFamilyTargetRoutingResolveService } from './llins-family-target-routing-resolve.service';

describe('Service Tests', () => {
  describe('LLINSFamilyTarget routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LLINSFamilyTargetRoutingResolveService;
    let service: LLINSFamilyTargetService;
    let resultLLINSFamilyTarget: ILLINSFamilyTarget | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LLINSFamilyTargetRoutingResolveService);
      service = TestBed.inject(LLINSFamilyTargetService);
      resultLLINSFamilyTarget = undefined;
    });

    describe('resolve', () => {
      it('should return ILLINSFamilyTarget returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLLINSFamilyTarget = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLLINSFamilyTarget).toEqual({ id: 123 });
      });

      it('should return new ILLINSFamilyTarget if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLLINSFamilyTarget = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLLINSFamilyTarget).toEqual(new LLINSFamilyTarget());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LLINSFamilyTarget })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLLINSFamilyTarget = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLLINSFamilyTarget).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
