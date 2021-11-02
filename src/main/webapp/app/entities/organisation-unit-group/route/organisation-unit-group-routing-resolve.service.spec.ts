jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IOrganisationUnitGroup, OrganisationUnitGroup } from '../organisation-unit-group.model';
import { OrganisationUnitGroupService } from '../service/organisation-unit-group.service';

import { OrganisationUnitGroupRoutingResolveService } from './organisation-unit-group-routing-resolve.service';

describe('Service Tests', () => {
  describe('OrganisationUnitGroup routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: OrganisationUnitGroupRoutingResolveService;
    let service: OrganisationUnitGroupService;
    let resultOrganisationUnitGroup: IOrganisationUnitGroup | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(OrganisationUnitGroupRoutingResolveService);
      service = TestBed.inject(OrganisationUnitGroupService);
      resultOrganisationUnitGroup = undefined;
    });

    describe('resolve', () => {
      it('should return IOrganisationUnitGroup returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrganisationUnitGroup = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOrganisationUnitGroup).toEqual({ id: 123 });
      });

      it('should return new IOrganisationUnitGroup if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrganisationUnitGroup = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultOrganisationUnitGroup).toEqual(new OrganisationUnitGroup());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as OrganisationUnitGroup })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrganisationUnitGroup = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOrganisationUnitGroup).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
