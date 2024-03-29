jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IChv, Chv } from '../chv.model';
import { ChvService } from '../service/chv.service';

import { ChvRoutingResolveService } from './chv-routing-resolve.service';

describe('Service Tests', () => {
  describe('Chv routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ChvRoutingResolveService;
    let service: ChvService;
    let resultChv: IChv | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ChvRoutingResolveService);
      service = TestBed.inject(ChvService);
      resultChv = undefined;
    });

    describe('resolve', () => {
      it('should return IChv returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultChv = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultChv).toEqual({ id: 123 });
      });

      it('should return new IChv if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultChv = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultChv).toEqual(new Chv());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Chv })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultChv = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultChv).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
