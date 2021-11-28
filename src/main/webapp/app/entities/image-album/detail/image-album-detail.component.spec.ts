import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ImageAlbumDetailComponent } from './image-album-detail.component';

describe('Component Tests', () => {
  describe('ImageAlbum Management Detail Component', () => {
    let comp: ImageAlbumDetailComponent;
    let fixture: ComponentFixture<ImageAlbumDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ImageAlbumDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ imageAlbum: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ImageAlbumDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ImageAlbumDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load imageAlbum on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.imageAlbum).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
