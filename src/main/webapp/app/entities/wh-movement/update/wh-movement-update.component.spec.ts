jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { WHMovementService } from '../service/wh-movement.service';
import { IWHMovement, WHMovement } from '../wh-movement.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { WorkingDayService } from 'app/entities/working-day/service/working-day.service';
import { IWarehouse } from 'app/entities/warehouse/warehouse.model';
import { WarehouseService } from 'app/entities/warehouse/service/warehouse.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';

import { WHMovementUpdateComponent } from './wh-movement-update.component';

describe('Component Tests', () => {
  describe('WhMovement Management Update Component', () => {
    let comp: WHMovementUpdateComponent;
    let fixture: ComponentFixture<WHMovementUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let wHMovementService: WHMovementService;
    let userService: UserService;
    let workingDayService: WorkingDayService;
    let warehouseService: WarehouseService;
    let teamService: TeamService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [WHMovementUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(WHMovementUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WHMovementUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      wHMovementService = TestBed.inject(WHMovementService);
      userService = TestBed.inject(UserService);
      workingDayService = TestBed.inject(WorkingDayService);
      warehouseService = TestBed.inject(WarehouseService);
      teamService = TestBed.inject(TeamService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const wHMovement: IWHMovement = { id: 456 };
        const createdBy: IUser = { id: 55578 };
        wHMovement.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 14210 };
        wHMovement.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 96808 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ wHMovement });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call WorkingDay query and add missing value', () => {
        const wHMovement: IWHMovement = { id: 456 };
        const day: IWorkingDay = { id: 27809 };
        wHMovement.day = day;

        const workingDayCollection: IWorkingDay[] = [{ id: 53376 }];
        jest.spyOn(workingDayService, 'query').mockReturnValue(of(new HttpResponse({ body: workingDayCollection })));
        const additionalWorkingDays = [day];
        const expectedCollection: IWorkingDay[] = [...additionalWorkingDays, ...workingDayCollection];
        jest.spyOn(workingDayService, 'addWorkingDayToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ wHMovement });
        comp.ngOnInit();

        expect(workingDayService.query).toHaveBeenCalled();
        expect(workingDayService.addWorkingDayToCollectionIfMissing).toHaveBeenCalledWith(workingDayCollection, ...additionalWorkingDays);
        expect(comp.workingDaysSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Warehouse query and add missing value', () => {
        const wHMovement: IWHMovement = { id: 456 };
        const initiatedWH: IWarehouse = { id: 99308 };
        wHMovement.initiatedWH = initiatedWH;
        const theOtherSideWH: IWarehouse = { id: 57131 };
        wHMovement.theOtherSideWH = theOtherSideWH;

        const warehouseCollection: IWarehouse[] = [{ id: 98814 }];
        jest.spyOn(warehouseService, 'query').mockReturnValue(of(new HttpResponse({ body: warehouseCollection })));
        const additionalWarehouses = [initiatedWH, theOtherSideWH];
        const expectedCollection: IWarehouse[] = [...additionalWarehouses, ...warehouseCollection];
        jest.spyOn(warehouseService, 'addWarehouseToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ wHMovement });
        comp.ngOnInit();

        expect(warehouseService.query).toHaveBeenCalled();
        expect(warehouseService.addWarehouseToCollectionIfMissing).toHaveBeenCalledWith(warehouseCollection, ...additionalWarehouses);
        expect(comp.warehousesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Team query and add missing value', () => {
        const wHMovement: IWHMovement = { id: 456 };
        const team: ITeam = { id: 30961 };
        wHMovement.team = team;

        const teamCollection: ITeam[] = [{ id: 71432 }];
        jest.spyOn(teamService, 'query').mockReturnValue(of(new HttpResponse({ body: teamCollection })));
        const additionalTeams = [team];
        const expectedCollection: ITeam[] = [...additionalTeams, ...teamCollection];
        jest.spyOn(teamService, 'addTeamToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ wHMovement });
        comp.ngOnInit();

        expect(teamService.query).toHaveBeenCalled();
        expect(teamService.addTeamToCollectionIfMissing).toHaveBeenCalledWith(teamCollection, ...additionalTeams);
        expect(comp.teamsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const wHMovement: IWHMovement = { id: 456 };
        const createdBy: IUser = { id: 74123 };
        wHMovement.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 1397 };
        wHMovement.lastUpdatedBy = lastUpdatedBy;
        const day: IWorkingDay = { id: 42326 };
        wHMovement.day = day;
        const initiatedWH: IWarehouse = { id: 53602 };
        wHMovement.initiatedWH = initiatedWH;
        const theOtherSideWH: IWarehouse = { id: 4877 };
        wHMovement.theOtherSideWH = theOtherSideWH;
        const team: ITeam = { id: 9463 };
        wHMovement.team = team;

        activatedRoute.data = of({ wHMovement });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(wHMovement));
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.workingDaysSharedCollection).toContain(day);
        expect(comp.warehousesSharedCollection).toContain(initiatedWH);
        expect(comp.warehousesSharedCollection).toContain(theOtherSideWH);
        expect(comp.teamsSharedCollection).toContain(team);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<WHMovement>>();
        const wHMovement = { id: 123 };
        jest.spyOn(wHMovementService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ wHMovement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: wHMovement }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(wHMovementService.update).toHaveBeenCalledWith(wHMovement);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<WHMovement>>();
        const wHMovement = new WHMovement();
        jest.spyOn(wHMovementService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ wHMovement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: wHMovement }));
        saveSubject.complete();

        // THEN
        expect(wHMovementService.create).toHaveBeenCalledWith(wHMovement);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<WHMovement>>();
        const wHMovement = { id: 123 };
        jest.spyOn(wHMovementService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ wHMovement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(wHMovementService.update).toHaveBeenCalledWith(wHMovement);
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
