jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ChvService } from '../service/chv.service';
import { IChv, Chv } from '../chv.model';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { IContentPage } from 'app/entities/content-page/content-page.model';
import { ContentPageService } from 'app/entities/content-page/service/content-page.service';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';
import { OrganisationUnitService } from 'app/entities/organisation-unit/service/organisation-unit.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { ChvUpdateComponent } from './chv-update.component';

describe('Component Tests', () => {
  describe('Chv Management Update Component', () => {
    let comp: ChvUpdateComponent;
    let fixture: ComponentFixture<ChvUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let chvService: ChvService;
    let personService: PersonService;
    let contentPageService: ContentPageService;
    let organisationUnitService: OrganisationUnitService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ChvUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ChvUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChvUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      chvService = TestBed.inject(ChvService);
      personService = TestBed.inject(PersonService);
      contentPageService = TestBed.inject(ContentPageService);
      organisationUnitService = TestBed.inject(OrganisationUnitService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call person query and add missing value', () => {
        const chv: IChv = { id: 456 };
        const person: IPerson = { id: 82132 };
        chv.person = person;

        const personCollection: IPerson[] = [{ id: 51281 }];
        jest.spyOn(personService, 'query').mockReturnValue(of(new HttpResponse({ body: personCollection })));
        const expectedCollection: IPerson[] = [person, ...personCollection];
        jest.spyOn(personService, 'addPersonToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ chv });
        comp.ngOnInit();

        expect(personService.query).toHaveBeenCalled();
        expect(personService.addPersonToCollectionIfMissing).toHaveBeenCalledWith(personCollection, person);
        expect(comp.peopleCollection).toEqual(expectedCollection);
      });

      it('Should call contentPage query and add missing value', () => {
        const chv: IChv = { id: 456 };
        const contentPage: IContentPage = { id: 29413 };
        chv.contentPage = contentPage;

        const contentPageCollection: IContentPage[] = [{ id: 84070 }];
        jest.spyOn(contentPageService, 'query').mockReturnValue(of(new HttpResponse({ body: contentPageCollection })));
        const expectedCollection: IContentPage[] = [contentPage, ...contentPageCollection];
        jest.spyOn(contentPageService, 'addContentPageToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ chv });
        comp.ngOnInit();

        expect(contentPageService.query).toHaveBeenCalled();
        expect(contentPageService.addContentPageToCollectionIfMissing).toHaveBeenCalledWith(contentPageCollection, contentPage);
        expect(comp.contentPagesCollection).toEqual(expectedCollection);
      });

      it('Should call OrganisationUnit query and add missing value', () => {
        const chv: IChv = { id: 456 };
        const district: IOrganisationUnit = { id: 59321 };
        chv.district = district;
        const homeSubvillage: IOrganisationUnit = { id: 35786 };
        chv.homeSubvillage = homeSubvillage;
        const managedByHf: IOrganisationUnit = { id: 27546 };
        chv.managedByHf = managedByHf;

        const organisationUnitCollection: IOrganisationUnit[] = [{ id: 27495 }];
        jest.spyOn(organisationUnitService, 'query').mockReturnValue(of(new HttpResponse({ body: organisationUnitCollection })));
        const additionalOrganisationUnits = [district, homeSubvillage, managedByHf];
        const expectedCollection: IOrganisationUnit[] = [...additionalOrganisationUnits, ...organisationUnitCollection];
        jest.spyOn(organisationUnitService, 'addOrganisationUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ chv });
        comp.ngOnInit();

        expect(organisationUnitService.query).toHaveBeenCalled();
        expect(organisationUnitService.addOrganisationUnitToCollectionIfMissing).toHaveBeenCalledWith(
          organisationUnitCollection,
          ...additionalOrganisationUnits
        );
        expect(comp.organisationUnitsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const chv: IChv = { id: 456 };
        const createdBy: IUser = { id: 44806 };
        chv.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 81668 };
        chv.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 22554 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ chv });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const chv: IChv = { id: 456 };
        const person: IPerson = { id: 54958 };
        chv.person = person;
        const contentPage: IContentPage = { id: 65936 };
        chv.contentPage = contentPage;
        const district: IOrganisationUnit = { id: 17822 };
        chv.district = district;
        const homeSubvillage: IOrganisationUnit = { id: 55557 };
        chv.homeSubvillage = homeSubvillage;
        const managedByHf: IOrganisationUnit = { id: 49632 };
        chv.managedByHf = managedByHf;
        const createdBy: IUser = { id: 7006 };
        chv.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 94159 };
        chv.lastUpdatedBy = lastUpdatedBy;

        activatedRoute.data = of({ chv });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(chv));
        expect(comp.peopleCollection).toContain(person);
        expect(comp.contentPagesCollection).toContain(contentPage);
        expect(comp.organisationUnitsSharedCollection).toContain(district);
        expect(comp.organisationUnitsSharedCollection).toContain(homeSubvillage);
        expect(comp.organisationUnitsSharedCollection).toContain(managedByHf);
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Chv>>();
        const chv = { id: 123 };
        jest.spyOn(chvService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ chv });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: chv }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(chvService.update).toHaveBeenCalledWith(chv);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Chv>>();
        const chv = new Chv();
        jest.spyOn(chvService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ chv });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: chv }));
        saveSubject.complete();

        // THEN
        expect(chvService.create).toHaveBeenCalledWith(chv);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Chv>>();
        const chv = { id: 123 };
        jest.spyOn(chvService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ chv });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(chvService.update).toHaveBeenCalledWith(chv);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPersonById', () => {
        it('Should return tracked Person primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPersonById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackContentPageById', () => {
        it('Should return tracked ContentPage primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackContentPageById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackOrganisationUnitById', () => {
        it('Should return tracked OrganisationUnit primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackOrganisationUnitById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

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
