jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMalariaUnitStaffMember, MalariaUnitStaffMember } from '../malaria-unit-staff-member.model';
import { MalariaUnitStaffMemberService } from '../service/malaria-unit-staff-member.service';

import { MalariaUnitStaffMemberRoutingResolveService } from './malaria-unit-staff-member-routing-resolve.service';

describe('Service Tests', () => {
  describe('MalariaUnitStaffMember routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: MalariaUnitStaffMemberRoutingResolveService;
    let service: MalariaUnitStaffMemberService;
    let resultMalariaUnitStaffMember: IMalariaUnitStaffMember | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(MalariaUnitStaffMemberRoutingResolveService);
      service = TestBed.inject(MalariaUnitStaffMemberService);
      resultMalariaUnitStaffMember = undefined;
    });

    describe('resolve', () => {
      it('should return IMalariaUnitStaffMember returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMalariaUnitStaffMember = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMalariaUnitStaffMember).toEqual({ id: 123 });
      });

      it('should return new IMalariaUnitStaffMember if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMalariaUnitStaffMember = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMalariaUnitStaffMember).toEqual(new MalariaUnitStaffMember());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as MalariaUnitStaffMember })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMalariaUnitStaffMember = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMalariaUnitStaffMember).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
