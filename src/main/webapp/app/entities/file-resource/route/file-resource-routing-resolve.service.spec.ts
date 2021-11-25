jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFileResource, FileResource } from '../file-resource.model';
import { FileResourceService } from '../service/file-resource.service';

import { FileResourceRoutingResolveService } from './file-resource-routing-resolve.service';

describe('Service Tests', () => {
  describe('FileResource routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: FileResourceRoutingResolveService;
    let service: FileResourceService;
    let resultFileResource: IFileResource | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(FileResourceRoutingResolveService);
      service = TestBed.inject(FileResourceService);
      resultFileResource = undefined;
    });

    describe('resolve', () => {
      it('should return IFileResource returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFileResource = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFileResource).toEqual({ id: 123 });
      });

      it('should return new IFileResource if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFileResource = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultFileResource).toEqual(new FileResource());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as FileResource })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFileResource = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFileResource).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
