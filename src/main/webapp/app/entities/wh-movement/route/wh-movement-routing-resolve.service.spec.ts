jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IWHMovement, WHMovement } from '../wh-movement.model';
import { WHMovementService } from '../service/wh-movement.service';

import { WHMovementRoutingResolveService } from './wh-movement-routing-resolve.service';

describe('Service Tests', () => {
  describe('WhMovement routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: WHMovementRoutingResolveService;
    let service: WHMovementService;
    let resultWHMovement: IWHMovement | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(WHMovementRoutingResolveService);
      service = TestBed.inject(WHMovementService);
      resultWHMovement = undefined;
    });

    describe('resolve', () => {
      it('should return IWHMovement returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultWHMovement = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultWHMovement).toEqual({ id: 123 });
      });

      it('should return new IWHMovement if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultWHMovement = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultWHMovement).toEqual(new WHMovement());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as WHMovement })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultWHMovement = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultWHMovement).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
