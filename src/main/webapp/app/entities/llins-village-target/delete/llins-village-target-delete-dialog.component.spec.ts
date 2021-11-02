jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { LLINSVillageTargetService } from '../service/llins-village-target.service';

import { LLINSVillageTargetDeleteDialogComponent } from './llins-village-target-delete-dialog.component';

describe('Component Tests', () => {
  describe('LLINSVillageTarget Management Delete Component', () => {
    let comp: LLINSVillageTargetDeleteDialogComponent;
    let fixture: ComponentFixture<LLINSVillageTargetDeleteDialogComponent>;
    let service: LLINSVillageTargetService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LLINSVillageTargetDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(LLINSVillageTargetDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LLINSVillageTargetDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(LLINSVillageTargetService);
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
