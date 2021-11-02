jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IOrganisationUnitGroupSet, OrganisationUnitGroupSet } from '../organisation-unit-group-set.model';
import { OrganisationUnitGroupSetService } from '../service/organisation-unit-group-set.service';

import { OrganisationUnitGroupSetRoutingResolveService } from './organisation-unit-group-set-routing-resolve.service';

describe('Service Tests', () => {
  describe('OrganisationUnitGroupSet routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: OrganisationUnitGroupSetRoutingResolveService;
    let service: OrganisationUnitGroupSetService;
    let resultOrganisationUnitGroupSet: IOrganisationUnitGroupSet | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(OrganisationUnitGroupSetRoutingResolveService);
      service = TestBed.inject(OrganisationUnitGroupSetService);
      resultOrganisationUnitGroupSet = undefined;
    });

    describe('resolve', () => {
      it('should return IOrganisationUnitGroupSet returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrganisationUnitGroupSet = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOrganisationUnitGroupSet).toEqual({ id: 123 });
      });

      it('should return new IOrganisationUnitGroupSet if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrganisationUnitGroupSet = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultOrganisationUnitGroupSet).toEqual(new OrganisationUnitGroupSet());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as OrganisationUnitGroupSet })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrganisationUnitGroupSet = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOrganisationUnitGroupSet).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
