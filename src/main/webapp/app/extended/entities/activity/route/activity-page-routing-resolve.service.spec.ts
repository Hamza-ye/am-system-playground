jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IActivity, Activity } from "../../../../entities/activity/activity.model";
import { ActivityService } from '../service/activity.service';

import { ActivityPageRoutingResolveService } from './activity-page-routing-resolve.service';

describe('Service Tests', () => {
  describe('Activity routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ActivityPageRoutingResolveService;
    let service: ActivityService;
    let resultActivity: IActivity | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ActivityPageRoutingResolveService);
      service = TestBed.inject(ActivityService);
      resultActivity = undefined;
    });

    describe('resolve', () => {
      it('should return IActivity returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultActivity = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultActivity).toEqual({ id: 123 });
      });

      it('should return new IActivity if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultActivity = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultActivity).toEqual(new Activity());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Activity })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultActivity = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultActivity).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
