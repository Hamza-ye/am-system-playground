jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDataSet, DataSet } from '../data-set.model';
import { DataSetService } from '../service/data-set.service';

import { DataSetRoutingResolveService } from './data-set-routing-resolve.service';

describe('Service Tests', () => {
  describe('DataSet routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DataSetRoutingResolveService;
    let service: DataSetService;
    let resultDataSet: IDataSet | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DataSetRoutingResolveService);
      service = TestBed.inject(DataSetService);
      resultDataSet = undefined;
    });

    describe('resolve', () => {
      it('should return IDataSet returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDataSet = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDataSet).toEqual({ id: 123 });
      });

      it('should return new IDataSet if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDataSet = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDataSet).toEqual(new DataSet());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DataSet })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDataSet = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDataSet).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
