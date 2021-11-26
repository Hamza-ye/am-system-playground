jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IWhMovement, WhMovement } from '../wh-movement.model';
import { WhMovementService } from '../service/wh-movement.service';

import { WhMovementRoutingResolveService } from './wh-movement-routing-resolve.service';

describe('Service Tests', () => {
  describe('WhMovement routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: WhMovementRoutingResolveService;
    let service: WhMovementService;
    let resultWhMovement: IWhMovement | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(WhMovementRoutingResolveService);
      service = TestBed.inject(WhMovementService);
      resultWhMovement = undefined;
    });

    describe('resolve', () => {
      it('should return IWhMovement returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultWhMovement = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultWhMovement).toEqual({ id: 123 });
      });

      it('should return new IWhMovement if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultWhMovement = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultWhMovement).toEqual(new WhMovement());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as WhMovement })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultWhMovement = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultWhMovement).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
