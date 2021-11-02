jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDataProvider, DataProvider } from '../data-provider.model';
import { DataProviderService } from '../service/data-provider.service';

import { DataProviderRoutingResolveService } from './data-provider-routing-resolve.service';

describe('Service Tests', () => {
  describe('DataProvider routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DataProviderRoutingResolveService;
    let service: DataProviderService;
    let resultDataProvider: IDataProvider | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DataProviderRoutingResolveService);
      service = TestBed.inject(DataProviderService);
      resultDataProvider = undefined;
    });

    describe('resolve', () => {
      it('should return IDataProvider returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDataProvider = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDataProvider).toEqual({ id: 123 });
      });

      it('should return new IDataProvider if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDataProvider = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDataProvider).toEqual(new DataProvider());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DataProvider })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDataProvider = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDataProvider).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
