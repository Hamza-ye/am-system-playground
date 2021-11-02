import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITeam, Team } from '../team.model';
import { TeamService } from '../service/team.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { IWarehouse } from 'app/entities/warehouse/warehouse.model';
import { WarehouseService } from 'app/entities/warehouse/service/warehouse.service';

@Component({
  selector: 'app-team-update',
  templateUrl: './team-update.component.html',
})
export class TeamUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  peopleSharedCollection: IPerson[] = [];
  warehousesSharedCollection: IWarehouse[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    code: [null, []],
    name: [],
    created: [],
    lastUpdated: [],
    teamNo: [null, [Validators.required, Validators.min(1)]],
    teamType: [null, [Validators.required]],
    user: [],
    lastUpdatedBy: [],
    person: [],
    assignedToWarehouses: [],
  });

  constructor(
    protected teamService: TeamService,
    protected userService: UserService,
    protected personService: PersonService,
    protected warehouseService: WarehouseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ team }) => {
      if (team.id === undefined) {
        const today = dayjs().startOf('day');
        team.created = today;
        team.lastUpdated = today;
      }

      this.updateForm(team);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const team = this.createFromForm();
    if (team.id !== undefined) {
      this.subscribeToSaveResponse(this.teamService.update(team));
    } else {
      this.subscribeToSaveResponse(this.teamService.create(team));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackPersonById(index: number, item: IPerson): number {
    return item.id!;
  }

  trackWarehouseById(index: number, item: IWarehouse): number {
    return item.id!;
  }

  getSelectedWarehouse(option: IWarehouse, selectedVals?: IWarehouse[]): IWarehouse {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITeam>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(team: ITeam): void {
    this.editForm.patchValue({
      id: team.id,
      uid: team.uid,
      code: team.code,
      name: team.name,
      created: team.created ? team.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: team.lastUpdated ? team.lastUpdated.format(DATE_TIME_FORMAT) : null,
      teamNo: team.teamNo,
      teamType: team.teamType,
      user: team.user,
      lastUpdatedBy: team.lastUpdatedBy,
      person: team.person,
      assignedToWarehouses: team.assignedToWarehouses,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, team.user, team.lastUpdatedBy);
    this.peopleSharedCollection = this.personService.addPersonToCollectionIfMissing(this.peopleSharedCollection, team.person);
    this.warehousesSharedCollection = this.warehouseService.addWarehouseToCollectionIfMissing(
      this.warehousesSharedCollection,
      ...(team.assignedToWarehouses ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) =>
          this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value, this.editForm.get('lastUpdatedBy')!.value)
        )
      )
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.personService
      .query()
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .pipe(map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing(people, this.editForm.get('person')!.value)))
      .subscribe((people: IPerson[]) => (this.peopleSharedCollection = people));

    this.warehouseService
      .query()
      .pipe(map((res: HttpResponse<IWarehouse[]>) => res.body ?? []))
      .pipe(
        map((warehouses: IWarehouse[]) =>
          this.warehouseService.addWarehouseToCollectionIfMissing(warehouses, ...(this.editForm.get('assignedToWarehouses')!.value ?? []))
        )
      )
      .subscribe((warehouses: IWarehouse[]) => (this.warehousesSharedCollection = warehouses));
  }

  protected createFromForm(): ITeam {
    return {
      ...new Team(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      code: this.editForm.get(['code'])!.value,
      name: this.editForm.get(['name'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      teamNo: this.editForm.get(['teamNo'])!.value,
      teamType: this.editForm.get(['teamType'])!.value,
      user: this.editForm.get(['user'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      person: this.editForm.get(['person'])!.value,
      assignedToWarehouses: this.editForm.get(['assignedToWarehouses'])!.value,
    };
  }
}
