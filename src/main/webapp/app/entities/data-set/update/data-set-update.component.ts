import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDataSet, DataSet } from '../data-set.model';
import { DataSetService } from '../service/data-set.service';
import { IPeriodType } from 'app/entities/period-type/period-type.model';
import { PeriodTypeService } from 'app/entities/period-type/service/period-type.service';
import { IPeopleGroup } from 'app/entities/people-group/people-group.model';
import { PeopleGroupService } from 'app/entities/people-group/service/people-group.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';
import { OrganisationUnitService } from 'app/entities/organisation-unit/service/organisation-unit.service';

@Component({
  selector: 'app-data-set-update',
  templateUrl: './data-set-update.component.html',
})
export class DataSetUpdateComponent implements OnInit {
  isSaving = false;

  periodTypesSharedCollection: IPeriodType[] = [];
  peopleGroupsSharedCollection: IPeopleGroup[] = [];
  usersSharedCollection: IUser[] = [];
  organisationUnitsSharedCollection: IOrganisationUnit[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    code: [null, []],
    name: [],
    shortName: [null, [Validators.maxLength(50)]],
    description: [],
    created: [],
    lastUpdated: [],
    expiryDays: [],
    timelyDays: [],
    periodType: [],
    notificationRecipients: [],
    user: [],
    lastUpdatedBy: [],
    sources: [],
  });

  constructor(
    protected dataSetService: DataSetService,
    protected periodTypeService: PeriodTypeService,
    protected peopleGroupService: PeopleGroupService,
    protected userService: UserService,
    protected organisationUnitService: OrganisationUnitService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dataSet }) => {
      if (dataSet.id === undefined) {
        const today = dayjs().startOf('day');
        dataSet.created = today;
        dataSet.lastUpdated = today;
      }

      this.updateForm(dataSet);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dataSet = this.createFromForm();
    if (dataSet.id !== undefined) {
      this.subscribeToSaveResponse(this.dataSetService.update(dataSet));
    } else {
      this.subscribeToSaveResponse(this.dataSetService.create(dataSet));
    }
  }

  trackPeriodTypeById(index: number, item: IPeriodType): number {
    return item.id!;
  }

  trackPeopleGroupById(index: number, item: IPeopleGroup): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackOrganisationUnitById(index: number, item: IOrganisationUnit): number {
    return item.id!;
  }

  getSelectedOrganisationUnit(option: IOrganisationUnit, selectedVals?: IOrganisationUnit[]): IOrganisationUnit {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDataSet>>): void {
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

  protected updateForm(dataSet: IDataSet): void {
    this.editForm.patchValue({
      id: dataSet.id,
      uid: dataSet.uid,
      code: dataSet.code,
      name: dataSet.name,
      shortName: dataSet.shortName,
      description: dataSet.description,
      created: dataSet.created ? dataSet.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: dataSet.lastUpdated ? dataSet.lastUpdated.format(DATE_TIME_FORMAT) : null,
      expiryDays: dataSet.expiryDays,
      timelyDays: dataSet.timelyDays,
      periodType: dataSet.periodType,
      notificationRecipients: dataSet.notificationRecipients,
      user: dataSet.user,
      lastUpdatedBy: dataSet.lastUpdatedBy,
      sources: dataSet.sources,
    });

    this.periodTypesSharedCollection = this.periodTypeService.addPeriodTypeToCollectionIfMissing(
      this.periodTypesSharedCollection,
      dataSet.periodType
    );
    this.peopleGroupsSharedCollection = this.peopleGroupService.addPeopleGroupToCollectionIfMissing(
      this.peopleGroupsSharedCollection,
      dataSet.notificationRecipients
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      dataSet.user,
      dataSet.lastUpdatedBy
    );
    this.organisationUnitsSharedCollection = this.organisationUnitService.addOrganisationUnitToCollectionIfMissing(
      this.organisationUnitsSharedCollection,
      ...(dataSet.sources ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.periodTypeService
      .query()
      .pipe(map((res: HttpResponse<IPeriodType[]>) => res.body ?? []))
      .pipe(
        map((periodTypes: IPeriodType[]) =>
          this.periodTypeService.addPeriodTypeToCollectionIfMissing(periodTypes, this.editForm.get('periodType')!.value)
        )
      )
      .subscribe((periodTypes: IPeriodType[]) => (this.periodTypesSharedCollection = periodTypes));

    this.peopleGroupService
      .query()
      .pipe(map((res: HttpResponse<IPeopleGroup[]>) => res.body ?? []))
      .pipe(
        map((peopleGroups: IPeopleGroup[]) =>
          this.peopleGroupService.addPeopleGroupToCollectionIfMissing(peopleGroups, this.editForm.get('notificationRecipients')!.value)
        )
      )
      .subscribe((peopleGroups: IPeopleGroup[]) => (this.peopleGroupsSharedCollection = peopleGroups));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) =>
          this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value, this.editForm.get('lastUpdatedBy')!.value)
        )
      )
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.organisationUnitService
      .query()
      .pipe(map((res: HttpResponse<IOrganisationUnit[]>) => res.body ?? []))
      .pipe(
        map((organisationUnits: IOrganisationUnit[]) =>
          this.organisationUnitService.addOrganisationUnitToCollectionIfMissing(
            organisationUnits,
            ...(this.editForm.get('sources')!.value ?? [])
          )
        )
      )
      .subscribe((organisationUnits: IOrganisationUnit[]) => (this.organisationUnitsSharedCollection = organisationUnits));
  }

  protected createFromForm(): IDataSet {
    return {
      ...new DataSet(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      code: this.editForm.get(['code'])!.value,
      name: this.editForm.get(['name'])!.value,
      shortName: this.editForm.get(['shortName'])!.value,
      description: this.editForm.get(['description'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      expiryDays: this.editForm.get(['expiryDays'])!.value,
      timelyDays: this.editForm.get(['timelyDays'])!.value,
      periodType: this.editForm.get(['periodType'])!.value,
      notificationRecipients: this.editForm.get(['notificationRecipients'])!.value,
      user: this.editForm.get(['user'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      sources: this.editForm.get(['sources'])!.value,
    };
  }
}
