jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ProjectService } from '../service/project.service';
import { IProject, Project } from '../project.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { ProjectUpdateComponent } from './project-update.component';

describe('Component Tests', () => {
  describe('Project Management Update Component', () => {
    let comp: ProjectUpdateComponent;
    let fixture: ComponentFixture<ProjectUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let projectService: ProjectService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProjectUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ProjectUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProjectUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      projectService = TestBed.inject(ProjectService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const project: IProject = { id: 456 };
        const createdBy: IUser = { id: 56342 };
        project.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 98841 };
        project.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 75694 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ project });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const project: IProject = { id: 456 };
        const createdBy: IUser = { id: 65199 };
        project.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 4608 };
        project.lastUpdatedBy = lastUpdatedBy;

        activatedRoute.data = of({ project });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(project));
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Project>>();
        const project = { id: 123 };
        jest.spyOn(projectService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ project });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: project }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(projectService.update).toHaveBeenCalledWith(project);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Project>>();
        const project = new Project();
        jest.spyOn(projectService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ project });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: project }));
        saveSubject.complete();

        // THEN
        expect(projectService.create).toHaveBeenCalledWith(project);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Project>>();
        const project = { id: 123 };
        jest.spyOn(projectService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ project });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(projectService.update).toHaveBeenCalledWith(project);
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
