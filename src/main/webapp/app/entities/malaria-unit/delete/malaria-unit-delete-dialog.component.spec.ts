jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { MalariaUnitService } from '../service/malaria-unit.service';

import { MalariaUnitDeleteDialogComponent } from './malaria-unit-delete-dialog.component';

describe('Component Tests', () => {
  describe('MalariaUnit Management Delete Component', () => {
    let comp: MalariaUnitDeleteDialogComponent;
    let fixture: ComponentFixture<MalariaUnitDeleteDialogComponent>;
    let service: MalariaUnitService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MalariaUnitDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(MalariaUnitDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MalariaUnitDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(MalariaUnitService);
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
