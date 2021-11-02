jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { CHVMalariaReportVersion1Service } from '../service/chv-malaria-report-version-1.service';

import { CHVMalariaReportVersion1DeleteDialogComponent } from './chv-malaria-report-version-1-delete-dialog.component';

describe('Component Tests', () => {
  describe('CHVMalariaReportVersion1 Management Delete Component', () => {
    let comp: CHVMalariaReportVersion1DeleteDialogComponent;
    let fixture: ComponentFixture<CHVMalariaReportVersion1DeleteDialogComponent>;
    let service: CHVMalariaReportVersion1Service;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CHVMalariaReportVersion1DeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(CHVMalariaReportVersion1DeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CHVMalariaReportVersion1DeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CHVMalariaReportVersion1Service);
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
