jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IImageAlbum, ImageAlbum } from '../image-album.model';
import { ImageAlbumService } from '../service/image-album.service';

import { ImageAlbumRoutingResolveService } from './image-album-routing-resolve.service';

describe('Service Tests', () => {
  describe('ImageAlbum routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ImageAlbumRoutingResolveService;
    let service: ImageAlbumService;
    let resultImageAlbum: IImageAlbum | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ImageAlbumRoutingResolveService);
      service = TestBed.inject(ImageAlbumService);
      resultImageAlbum = undefined;
    });

    describe('resolve', () => {
      it('should return IImageAlbum returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultImageAlbum = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultImageAlbum).toEqual({ id: 123 });
      });

      it('should return new IImageAlbum if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultImageAlbum = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultImageAlbum).toEqual(new ImageAlbum());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ImageAlbum })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultImageAlbum = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultImageAlbum).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
