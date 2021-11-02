jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICHVTeam, CHVTeam } from '../chv-team.model';
import { CHVTeamService } from '../service/chv-team.service';

import { CHVTeamRoutingResolveService } from './chv-team-routing-resolve.service';

describe('Service Tests', () => {
  describe('CHVTeam routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CHVTeamRoutingResolveService;
    let service: CHVTeamService;
    let resultCHVTeam: ICHVTeam | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CHVTeamRoutingResolveService);
      service = TestBed.inject(CHVTeamService);
      resultCHVTeam = undefined;
    });

    describe('resolve', () => {
      it('should return ICHVTeam returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCHVTeam = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCHVTeam).toEqual({ id: 123 });
      });

      it('should return new ICHVTeam if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCHVTeam = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCHVTeam).toEqual(new CHVTeam());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CHVTeam })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCHVTeam = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCHVTeam).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
