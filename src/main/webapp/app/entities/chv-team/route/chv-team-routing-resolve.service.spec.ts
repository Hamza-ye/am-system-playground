jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IChvTeam, ChvTeam } from '../chv-team.model';
import { ChvTeamService } from '../service/chv-team.service';

import { ChvTeamRoutingResolveService } from './chv-team-routing-resolve.service';

describe('Service Tests', () => {
  describe('ChvTeam routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ChvTeamRoutingResolveService;
    let service: ChvTeamService;
    let resultChvTeam: IChvTeam | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ChvTeamRoutingResolveService);
      service = TestBed.inject(ChvTeamService);
      resultChvTeam = undefined;
    });

    describe('resolve', () => {
      it('should return IChvTeam returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultChvTeam = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultChvTeam).toEqual({ id: 123 });
      });

      it('should return new IChvTeam if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultChvTeam = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultChvTeam).toEqual(new ChvTeam());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ChvTeam })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultChvTeam = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultChvTeam).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
