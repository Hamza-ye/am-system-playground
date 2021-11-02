jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { StatusOfCoverageService } from '../service/status-of-coverage.service';

import { StatusOfCoverageDeleteDialogComponent } from './status-of-coverage-delete-dialog.component';

describe('Component Tests', () => {
  describe('StatusOfCoverage Management Delete Component', () => {
    let comp: StatusOfCoverageDeleteDialogComponent;
    let fixture: ComponentFixture<StatusOfCoverageDeleteDialogComponent>;
    let service: StatusOfCoverageService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [StatusOfCoverageDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(StatusOfCoverageDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StatusOfCoverageDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(StatusOfCoverageService);
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
