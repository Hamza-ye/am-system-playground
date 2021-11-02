jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFingerprint, Fingerprint } from '../fingerprint.model';
import { FingerprintService } from '../service/fingerprint.service';

import { FingerprintRoutingResolveService } from './fingerprint-routing-resolve.service';

describe('Service Tests', () => {
  describe('Fingerprint routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: FingerprintRoutingResolveService;
    let service: FingerprintService;
    let resultFingerprint: IFingerprint | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(FingerprintRoutingResolveService);
      service = TestBed.inject(FingerprintService);
      resultFingerprint = undefined;
    });

    describe('resolve', () => {
      it('should return IFingerprint returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFingerprint = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFingerprint).toEqual({ id: 123 });
      });

      it('should return new IFingerprint if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFingerprint = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultFingerprint).toEqual(new Fingerprint());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Fingerprint })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFingerprint = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFingerprint).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
