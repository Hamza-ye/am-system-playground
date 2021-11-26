import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPerson, Person } from '../person.model';
import { PersonService } from '../service/person.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';
import { OrganisationUnitService } from 'app/entities/organisation-unit/service/organisation-unit.service';
import { IPersonAuthorityGroup } from 'app/entities/person-authority-group/person-authority-group.model';
import { PersonAuthorityGroupService } from 'app/entities/person-authority-group/service/person-authority-group.service';

@Component({
  selector: 'app-person-update',
  templateUrl: './person-update.component.html',
})
export class PersonUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  organisationUnitsSharedCollection: IOrganisationUnit[] = [];
  personAuthorityGroupsSharedCollection: IPersonAuthorityGroup[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    code: [null, []],
    name: [],
    created: [],
    lastUpdated: [],
    uuid: [null, []],
    gender: [],
    mobile: [],
    lastLogin: [],
    login: [],
    selfRegistered: [],
    disabled: [],
    userInfo: [],
    createdBy: [],
    lastUpdatedBy: [],
    organisationUnits: [],
    dataViewOrganisationUnits: [],
    personAuthorityGroups: [],
  });

  constructor(
    protected personService: PersonService,
    protected userService: UserService,
    protected organisationUnitService: OrganisationUnitService,
    protected personAuthorityGroupService: PersonAuthorityGroupService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ person }) => {
      if (person.id === undefined) {
        const today = dayjs().startOf('day');
        person.created = today;
        person.lastUpdated = today;
        person.lastLogin = today;
      }

      this.updateForm(person);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const person = this.createFromForm();
    if (person.id !== undefined) {
      this.subscribeToSaveResponse(this.personService.update(person));
    } else {
      this.subscribeToSaveResponse(this.personService.create(person));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackOrganisationUnitById(index: number, item: IOrganisationUnit): number {
    return item.id!;
  }

  trackPersonAuthorityGroupById(index: number, item: IPersonAuthorityGroup): number {
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

  getSelectedPersonAuthorityGroup(option: IPersonAuthorityGroup, selectedVals?: IPersonAuthorityGroup[]): IPersonAuthorityGroup {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPerson>>): void {
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

  protected updateForm(person: IPerson): void {
    this.editForm.patchValue({
      id: person.id,
      uid: person.uid,
      code: person.code,
      name: person.name,
      created: person.created ? person.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: person.lastUpdated ? person.lastUpdated.format(DATE_TIME_FORMAT) : null,
      uuid: person.uuid,
      gender: person.gender,
      mobile: person.mobile,
      lastLogin: person.lastLogin ? person.lastLogin.format(DATE_TIME_FORMAT) : null,
      login: person.login,
      selfRegistered: person.selfRegistered,
      disabled: person.disabled,
      userInfo: person.userInfo,
      createdBy: person.createdBy,
      lastUpdatedBy: person.lastUpdatedBy,
      organisationUnits: person.organisationUnits,
      dataViewOrganisationUnits: person.dataViewOrganisationUnits,
      personAuthorityGroups: person.personAuthorityGroups,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      person.userInfo,
      person.createdBy,
      person.lastUpdatedBy
    );
    this.organisationUnitsSharedCollection = this.organisationUnitService.addOrganisationUnitToCollectionIfMissing(
      this.organisationUnitsSharedCollection,
      ...(person.organisationUnits ?? []),
      ...(person.dataViewOrganisationUnits ?? [])
    );
    this.personAuthorityGroupsSharedCollection = this.personAuthorityGroupService.addPersonAuthorityGroupToCollectionIfMissing(
      this.personAuthorityGroupsSharedCollection,
      ...(person.personAuthorityGroups ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) =>
          this.userService.addUserToCollectionIfMissing(
            users,
            this.editForm.get('userInfo')!.value,
            this.editForm.get('createdBy')!.value,
            this.editForm.get('lastUpdatedBy')!.value
          )
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
            ...(this.editForm.get('organisationUnits')!.value ?? []),
            ...(this.editForm.get('dataViewOrganisationUnits')!.value ?? [])
          )
        )
      )
      .subscribe((organisationUnits: IOrganisationUnit[]) => (this.organisationUnitsSharedCollection = organisationUnits));

    this.personAuthorityGroupService
      .query()
      .pipe(map((res: HttpResponse<IPersonAuthorityGroup[]>) => res.body ?? []))
      .pipe(
        map((personAuthorityGroups: IPersonAuthorityGroup[]) =>
          this.personAuthorityGroupService.addPersonAuthorityGroupToCollectionIfMissing(
            personAuthorityGroups,
            ...(this.editForm.get('personAuthorityGroups')!.value ?? [])
          )
        )
      )
      .subscribe((personAuthorityGroups: IPersonAuthorityGroup[]) => (this.personAuthorityGroupsSharedCollection = personAuthorityGroups));
  }

  protected createFromForm(): IPerson {
    return {
      ...new Person(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      code: this.editForm.get(['code'])!.value,
      name: this.editForm.get(['name'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      uuid: this.editForm.get(['uuid'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      mobile: this.editForm.get(['mobile'])!.value,
      lastLogin: this.editForm.get(['lastLogin'])!.value ? dayjs(this.editForm.get(['lastLogin'])!.value, DATE_TIME_FORMAT) : undefined,
      login: this.editForm.get(['login'])!.value,
      selfRegistered: this.editForm.get(['selfRegistered'])!.value,
      disabled: this.editForm.get(['disabled'])!.value,
      userInfo: this.editForm.get(['userInfo'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      organisationUnits: this.editForm.get(['organisationUnits'])!.value,
      dataViewOrganisationUnits: this.editForm.get(['dataViewOrganisationUnits'])!.value,
      personAuthorityGroups: this.editForm.get(['personAuthorityGroups'])!.value,
    };
  }
}
