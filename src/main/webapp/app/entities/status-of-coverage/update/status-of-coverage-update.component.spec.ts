jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { StatusOfCoverageService } from '../service/status-of-coverage.service';
import { IStatusOfCoverage, StatusOfCoverage } from '../status-of-coverage.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { StatusOfCoverageUpdateComponent } from './status-of-coverage-update.component';

describe('Component Tests', () => {
  describe('StatusOfCoverage Management Update Component', () => {
    let comp: StatusOfCoverageUpdateComponent;
    let fixture: ComponentFixture<StatusOfCoverageUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let statusOfCoverageService: StatusOfCoverageService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [StatusOfCoverageUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(StatusOfCoverageUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StatusOfCoverageUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      statusOfCoverageService = TestBed.inject(StatusOfCoverageService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const statusOfCoverage: IStatusOfCoverage = { id: 456 };
        const user: IUser = { id: 80154 };
        statusOfCoverage.user = user;
        const lastUpdatedBy: IUser = { id: 53708 };
        statusOfCoverage.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 76264 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ statusOfCoverage });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const statusOfCoverage: IStatusOfCoverage = { id: 456 };
        const user: IUser = { id: 13380 };
        statusOfCoverage.user = user;
        const lastUpdatedBy: IUser = { id: 5929 };
        statusOfCoverage.lastUpdatedBy = lastUpdatedBy;

        activatedRoute.data = of({ statusOfCoverage });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(statusOfCoverage));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<StatusOfCoverage>>();
        const statusOfCoverage = { id: 123 };
        jest.spyOn(statusOfCoverageService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ statusOfCoverage });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: statusOfCoverage }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(statusOfCoverageService.update).toHaveBeenCalledWith(statusOfCoverage);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<StatusOfCoverage>>();
        const statusOfCoverage = new StatusOfCoverage();
        jest.spyOn(statusOfCoverageService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ statusOfCoverage });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: statusOfCoverage }));
        saveSubject.complete();

        // THEN
        expect(statusOfCoverageService.create).toHaveBeenCalledWith(statusOfCoverage);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<StatusOfCoverage>>();
        const statusOfCoverage = { id: 123 };
        jest.spyOn(statusOfCoverageService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ statusOfCoverage });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(statusOfCoverageService.update).toHaveBeenCalledWith(statusOfCoverage);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
