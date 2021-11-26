jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { WhMovementService } from '../service/wh-movement.service';
import { IWhMovement, WhMovement } from '../wh-movement.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { WorkingDayService } from 'app/entities/working-day/service/working-day.service';
import { IWarehouse } from 'app/entities/warehouse/warehouse.model';
import { WarehouseService } from 'app/entities/warehouse/service/warehouse.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';

import { WhMovementUpdateComponent } from './wh-movement-update.component';

describe('Component Tests', () => {
  describe('WhMovement Management Update Component', () => {
    let comp: WhMovementUpdateComponent;
    let fixture: ComponentFixture<WhMovementUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let whMovementService: WhMovementService;
    let userService: UserService;
    let workingDayService: WorkingDayService;
    let warehouseService: WarehouseService;
    let teamService: TeamService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [WhMovementUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(WhMovementUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WhMovementUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      whMovementService = TestBed.inject(WhMovementService);
      userService = TestBed.inject(UserService);
      workingDayService = TestBed.inject(WorkingDayService);
      warehouseService = TestBed.inject(WarehouseService);
      teamService = TestBed.inject(TeamService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const whMovement: IWhMovement = { id: 456 };
        const createdBy: IUser = { id: 99427 };
        whMovement.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 93412 };
        whMovement.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 67046 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ whMovement });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call WorkingDay query and add missing value', () => {
        const whMovement: IWhMovement = { id: 456 };
        const day: IWorkingDay = { id: 89551 };
        whMovement.day = day;

        const workingDayCollection: IWorkingDay[] = [{ id: 12922 }];
        jest.spyOn(workingDayService, 'query').mockReturnValue(of(new HttpResponse({ body: workingDayCollection })));
        const additionalWorkingDays = [day];
        const expectedCollection: IWorkingDay[] = [...additionalWorkingDays, ...workingDayCollection];
        jest.spyOn(workingDayService, 'addWorkingDayToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ whMovement });
        comp.ngOnInit();

        expect(workingDayService.query).toHaveBeenCalled();
        expect(workingDayService.addWorkingDayToCollectionIfMissing).toHaveBeenCalledWith(workingDayCollection, ...additionalWorkingDays);
        expect(comp.workingDaysSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Warehouse query and add missing value', () => {
        const whMovement: IWhMovement = { id: 456 };
        const initiatedWh: IWarehouse = { id: 15625 };
        whMovement.initiatedWh = initiatedWh;
        const theOtherSideWh: IWarehouse = { id: 85564 };
        whMovement.theOtherSideWh = theOtherSideWh;

        const warehouseCollection: IWarehouse[] = [{ id: 76894 }];
        jest.spyOn(warehouseService, 'query').mockReturnValue(of(new HttpResponse({ body: warehouseCollection })));
        const additionalWarehouses = [initiatedWh, theOtherSideWh];
        const expectedCollection: IWarehouse[] = [...additionalWarehouses, ...warehouseCollection];
        jest.spyOn(warehouseService, 'addWarehouseToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ whMovement });
        comp.ngOnInit();

        expect(warehouseService.query).toHaveBeenCalled();
        expect(warehouseService.addWarehouseToCollectionIfMissing).toHaveBeenCalledWith(warehouseCollection, ...additionalWarehouses);
        expect(comp.warehousesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Team query and add missing value', () => {
        const whMovement: IWhMovement = { id: 456 };
        const team: ITeam = { id: 83979 };
        whMovement.team = team;

        const teamCollection: ITeam[] = [{ id: 86606 }];
        jest.spyOn(teamService, 'query').mockReturnValue(of(new HttpResponse({ body: teamCollection })));
        const additionalTeams = [team];
        const expectedCollection: ITeam[] = [...additionalTeams, ...teamCollection];
        jest.spyOn(teamService, 'addTeamToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ whMovement });
        comp.ngOnInit();

        expect(teamService.query).toHaveBeenCalled();
        expect(teamService.addTeamToCollectionIfMissing).toHaveBeenCalledWith(teamCollection, ...additionalTeams);
        expect(comp.teamsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const whMovement: IWhMovement = { id: 456 };
        const createdBy: IUser = { id: 24298 };
        whMovement.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 50076 };
        whMovement.lastUpdatedBy = lastUpdatedBy;
        const day: IWorkingDay = { id: 42271 };
        whMovement.day = day;
        const initiatedWh: IWarehouse = { id: 32532 };
        whMovement.initiatedWh = initiatedWh;
        const theOtherSideWh: IWarehouse = { id: 97076 };
        whMovement.theOtherSideWh = theOtherSideWh;
        const team: ITeam = { id: 45372 };
        whMovement.team = team;

        activatedRoute.data = of({ whMovement });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(whMovement));
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.workingDaysSharedCollection).toContain(day);
        expect(comp.warehousesSharedCollection).toContain(initiatedWh);
        expect(comp.warehousesSharedCollection).toContain(theOtherSideWh);
        expect(comp.teamsSharedCollection).toContain(team);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<WhMovement>>();
        const whMovement = { id: 123 };
        jest.spyOn(whMovementService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ whMovement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: whMovement }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(whMovementService.update).toHaveBeenCalledWith(whMovement);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<WhMovement>>();
        const whMovement = new WhMovement();
        jest.spyOn(whMovementService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ whMovement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: whMovement }));
        saveSubject.complete();

        // THEN
        expect(whMovementService.create).toHaveBeenCalledWith(whMovement);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<WhMovement>>();
        const whMovement = { id: 123 };
        jest.spyOn(whMovementService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ whMovement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(whMovementService.update).toHaveBeenCalledWith(whMovement);
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

      describe('trackWorkingDayById', () => {
        it('Should return tracked WorkingDay primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackWorkingDayById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackWarehouseById', () => {
        it('Should return tracked Warehouse primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackWarehouseById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackTeamById', () => {
        it('Should return tracked Team primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTeamById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
