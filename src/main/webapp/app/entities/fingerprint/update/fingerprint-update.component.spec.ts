jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FingerprintService } from '../service/fingerprint.service';
import { IFingerprint, Fingerprint } from '../fingerprint.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IFamily } from 'app/entities/family/family.model';
import { FamilyService } from 'app/entities/family/service/family.service';

import { FingerprintUpdateComponent } from './fingerprint-update.component';

describe('Component Tests', () => {
  describe('Fingerprint Management Update Component', () => {
    let comp: FingerprintUpdateComponent;
    let fixture: ComponentFixture<FingerprintUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let fingerprintService: FingerprintService;
    let userService: UserService;
    let familyService: FamilyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FingerprintUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FingerprintUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FingerprintUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      fingerprintService = TestBed.inject(FingerprintService);
      userService = TestBed.inject(UserService);
      familyService = TestBed.inject(FamilyService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const fingerprint: IFingerprint = { id: 456 };
        const user: IUser = { id: 24137 };
        fingerprint.user = user;
        const lastUpdatedBy: IUser = { id: 5155 };
        fingerprint.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 34307 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ fingerprint });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Family query and add missing value', () => {
        const fingerprint: IFingerprint = { id: 456 };
        const family: IFamily = { id: 67186 };
        fingerprint.family = family;

        const familyCollection: IFamily[] = [{ id: 12028 }];
        jest.spyOn(familyService, 'query').mockReturnValue(of(new HttpResponse({ body: familyCollection })));
        const additionalFamilies = [family];
        const expectedCollection: IFamily[] = [...additionalFamilies, ...familyCollection];
        jest.spyOn(familyService, 'addFamilyToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ fingerprint });
        comp.ngOnInit();

        expect(familyService.query).toHaveBeenCalled();
        expect(familyService.addFamilyToCollectionIfMissing).toHaveBeenCalledWith(familyCollection, ...additionalFamilies);
        expect(comp.familiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const fingerprint: IFingerprint = { id: 456 };
        const user: IUser = { id: 94790 };
        fingerprint.user = user;
        const lastUpdatedBy: IUser = { id: 18949 };
        fingerprint.lastUpdatedBy = lastUpdatedBy;
        const family: IFamily = { id: 8513 };
        fingerprint.family = family;

        activatedRoute.data = of({ fingerprint });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(fingerprint));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.familiesSharedCollection).toContain(family);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Fingerprint>>();
        const fingerprint = { id: 123 };
        jest.spyOn(fingerprintService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ fingerprint });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fingerprint }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(fingerprintService.update).toHaveBeenCalledWith(fingerprint);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Fingerprint>>();
        const fingerprint = new Fingerprint();
        jest.spyOn(fingerprintService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ fingerprint });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fingerprint }));
        saveSubject.complete();

        // THEN
        expect(fingerprintService.create).toHaveBeenCalledWith(fingerprint);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Fingerprint>>();
        const fingerprint = { id: 123 };
        jest.spyOn(fingerprintService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ fingerprint });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(fingerprintService.update).toHaveBeenCalledWith(fingerprint);
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

      describe('trackFamilyById', () => {
        it('Should return tracked Family primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFamilyById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
