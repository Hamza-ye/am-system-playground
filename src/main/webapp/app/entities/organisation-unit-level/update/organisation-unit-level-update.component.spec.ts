jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OrganisationUnitLevelService } from '../service/organisation-unit-level.service';
import { IOrganisationUnitLevel, OrganisationUnitLevel } from '../organisation-unit-level.model';

import { OrganisationUnitLevelUpdateComponent } from './organisation-unit-level-update.component';

describe('Component Tests', () => {
  describe('OrganisationUnitLevel Management Update Component', () => {
    let comp: OrganisationUnitLevelUpdateComponent;
    let fixture: ComponentFixture<OrganisationUnitLevelUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let organisationUnitLevelService: OrganisationUnitLevelService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OrganisationUnitLevelUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(OrganisationUnitLevelUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrganisationUnitLevelUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      organisationUnitLevelService = TestBed.inject(OrganisationUnitLevelService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const organisationUnitLevel: IOrganisationUnitLevel = { id: 456 };

        activatedRoute.data = of({ organisationUnitLevel });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(organisationUnitLevel));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OrganisationUnitLevel>>();
        const organisationUnitLevel = { id: 123 };
        jest.spyOn(organisationUnitLevelService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ organisationUnitLevel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: organisationUnitLevel }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(organisationUnitLevelService.update).toHaveBeenCalledWith(organisationUnitLevel);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OrganisationUnitLevel>>();
        const organisationUnitLevel = new OrganisationUnitLevel();
        jest.spyOn(organisationUnitLevelService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ organisationUnitLevel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: organisationUnitLevel }));
        saveSubject.complete();

        // THEN
        expect(organisationUnitLevelService.create).toHaveBeenCalledWith(organisationUnitLevel);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OrganisationUnitLevel>>();
        const organisationUnitLevel = { id: 123 };
        jest.spyOn(organisationUnitLevelService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ organisationUnitLevel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(organisationUnitLevelService.update).toHaveBeenCalledWith(organisationUnitLevel);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
