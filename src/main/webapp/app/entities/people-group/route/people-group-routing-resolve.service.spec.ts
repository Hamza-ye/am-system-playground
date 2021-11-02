jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPeopleGroup, PeopleGroup } from '../people-group.model';
import { PeopleGroupService } from '../service/people-group.service';

import { PeopleGroupRoutingResolveService } from './people-group-routing-resolve.service';

describe('Service Tests', () => {
  describe('PeopleGroup routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PeopleGroupRoutingResolveService;
    let service: PeopleGroupService;
    let resultPeopleGroup: IPeopleGroup | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PeopleGroupRoutingResolveService);
      service = TestBed.inject(PeopleGroupService);
      resultPeopleGroup = undefined;
    });

    describe('resolve', () => {
      it('should return IPeopleGroup returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPeopleGroup = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPeopleGroup).toEqual({ id: 123 });
      });

      it('should return new IPeopleGroup if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPeopleGroup = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPeopleGroup).toEqual(new PeopleGroup());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PeopleGroup })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPeopleGroup = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPeopleGroup).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
