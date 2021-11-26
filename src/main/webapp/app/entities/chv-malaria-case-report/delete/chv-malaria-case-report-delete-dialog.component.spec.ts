jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { CHVMalariaCaseReportService } from '../service/chv-malaria-case-report.service';

import { CHVMalariaCaseReportDeleteDialogComponent } from './chv-malaria-case-report-delete-dialog.component';

describe('Component Tests', () => {
  describe('ChvMalariaCaseReport Management Delete Component', () => {
    let comp: CHVMalariaCaseReportDeleteDialogComponent;
    let fixture: ComponentFixture<CHVMalariaCaseReportDeleteDialogComponent>;
    let service: CHVMalariaCaseReportService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CHVMalariaCaseReportDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(CHVMalariaCaseReportDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CHVMalariaCaseReportDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CHVMalariaCaseReportService);
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
