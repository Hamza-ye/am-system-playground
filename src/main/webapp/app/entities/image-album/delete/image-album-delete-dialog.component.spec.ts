jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ImageAlbumService } from '../service/image-album.service';

import { ImageAlbumDeleteDialogComponent } from './image-album-delete-dialog.component';

describe('Component Tests', () => {
  describe('ImageAlbum Management Delete Component', () => {
    let comp: ImageAlbumDeleteDialogComponent;
    let fixture: ComponentFixture<ImageAlbumDeleteDialogComponent>;
    let service: ImageAlbumService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ImageAlbumDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(ImageAlbumDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ImageAlbumDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ImageAlbumService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        jest.spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
