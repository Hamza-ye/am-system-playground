import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ImageAlbumService } from '../service/image-album.service';

import { ImageAlbumComponent } from './image-album.component';

describe('Component Tests', () => {
  describe('ImageAlbum Management Component', () => {
    let comp: ImageAlbumComponent;
    let fixture: ComponentFixture<ImageAlbumComponent>;
    let service: ImageAlbumService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ImageAlbumComponent],
      })
        .overrideTemplate(ImageAlbumComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ImageAlbumComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ImageAlbumService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.imageAlbums?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
