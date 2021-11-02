jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IOrganisationUnit, OrganisationUnit } from '../organisation-unit.model';
import { OrganisationUnitService } from '../service/organisation-unit.service';

import { OrganisationUnitRoutingResolveService } from './organisation-unit-routing-resolve.service';

describe('Service Tests', () => {
  describe('OrganisationUnit routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: OrganisationUnitRoutingResolveService;
    let service: OrganisationUnitService;
    let resultOrganisationUnit: IOrganisationUnit | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(OrganisationUnitRoutingResolveService);
      service = TestBed.inject(OrganisationUnitService);
      resultOrganisationUnit = undefined;
    });

    describe('resolve', () => {
      it('should return IOrganisationUnit returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrganisationUnit = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOrganisationUnit).toEqual({ id: 123 });
      });

      it('should return new IOrganisationUnit if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrganisationUnit = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultOrganisationUnit).toEqual(new OrganisationUnit());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as OrganisationUnit })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrganisationUnit = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOrganisationUnit).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
