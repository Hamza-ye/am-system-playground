jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPersonAuthorityGroup, PersonAuthorityGroup } from '../person-authority-group.model';
import { PersonAuthorityGroupService } from '../service/person-authority-group.service';

import { PersonAuthorityGroupRoutingResolveService } from './person-authority-group-routing-resolve.service';

describe('Service Tests', () => {
  describe('PersonAuthorityGroup routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PersonAuthorityGroupRoutingResolveService;
    let service: PersonAuthorityGroupService;
    let resultPersonAuthorityGroup: IPersonAuthorityGroup | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PersonAuthorityGroupRoutingResolveService);
      service = TestBed.inject(PersonAuthorityGroupService);
      resultPersonAuthorityGroup = undefined;
    });

    describe('resolve', () => {
      it('should return IPersonAuthorityGroup returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPersonAuthorityGroup = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPersonAuthorityGroup).toEqual({ id: 123 });
      });

      it('should return new IPersonAuthorityGroup if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPersonAuthorityGroup = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPersonAuthorityGroup).toEqual(new PersonAuthorityGroup());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PersonAuthorityGroup })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPersonAuthorityGroup = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPersonAuthorityGroup).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
