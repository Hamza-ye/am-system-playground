jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IExternalFileResource, ExternalFileResource } from '../external-file-resource.model';
import { ExternalFileResourceService } from '../service/external-file-resource.service';

import { ExternalFileResourceRoutingResolveService } from './external-file-resource-routing-resolve.service';

describe('Service Tests', () => {
  describe('ExternalFileResource routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ExternalFileResourceRoutingResolveService;
    let service: ExternalFileResourceService;
    let resultExternalFileResource: IExternalFileResource | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ExternalFileResourceRoutingResolveService);
      service = TestBed.inject(ExternalFileResourceService);
      resultExternalFileResource = undefined;
    });

    describe('resolve', () => {
      it('should return IExternalFileResource returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultExternalFileResource = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultExternalFileResource).toEqual({ id: 123 });
      });

      it('should return new IExternalFileResource if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultExternalFileResource = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultExternalFileResource).toEqual(new ExternalFileResource());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ExternalFileResource })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultExternalFileResource = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultExternalFileResource).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
