jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { LlinsVillageTargetService } from '../service/llins-village-target.service';

import { LlinsVillageTargetDeleteDialogComponent } from './llins-village-target-delete-dialog.component';

describe('Component Tests', () => {
  describe('LlinsVillageTarget Management Delete Component', () => {
    let comp: LlinsVillageTargetDeleteDialogComponent;
    let fixture: ComponentFixture<LlinsVillageTargetDeleteDialogComponent>;
    let service: LlinsVillageTargetService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LlinsVillageTargetDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(LlinsVillageTargetDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LlinsVillageTargetDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(LlinsVillageTargetService);
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
