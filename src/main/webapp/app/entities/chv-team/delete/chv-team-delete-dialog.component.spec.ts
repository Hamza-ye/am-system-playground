jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { CHVTeamService } from '../service/chv-team.service';

import { CHVTeamDeleteDialogComponent } from './chv-team-delete-dialog.component';

describe('Component Tests', () => {
  describe('ChvTeam Management Delete Component', () => {
    let comp: CHVTeamDeleteDialogComponent;
    let fixture: ComponentFixture<CHVTeamDeleteDialogComponent>;
    let service: CHVTeamService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CHVTeamDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(CHVTeamDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CHVTeamDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CHVTeamService);
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
