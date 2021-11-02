jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { PeopleGroupService } from '../service/people-group.service';

import { PeopleGroupDeleteDialogComponent } from './people-group-delete-dialog.component';

describe('Component Tests', () => {
  describe('PeopleGroup Management Delete Component', () => {
    let comp: PeopleGroupDeleteDialogComponent;
    let fixture: ComponentFixture<PeopleGroupDeleteDialogComponent>;
    let service: PeopleGroupService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PeopleGroupDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(PeopleGroupDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PeopleGroupDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PeopleGroupService);
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
