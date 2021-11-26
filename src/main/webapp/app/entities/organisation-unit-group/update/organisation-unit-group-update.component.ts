import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IOrganisationUnitGroup, OrganisationUnitGroup } from '../organisation-unit-group.model';
import { OrganisationUnitGroupService } from '../service/organisation-unit-group.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';
import { OrganisationUnitService } from 'app/entities/organisation-unit/service/organisation-unit.service';

@Component({
  selector: 'app-organisation-unit-group-update',
  templateUrl: './organisation-unit-group-update.component.html',
})
export class OrganisationUnitGroupUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  organisationUnitsSharedCollection: IOrganisationUnit[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    code: [null, []],
    name: [],
    shortName: [null, [Validators.maxLength(50)]],
    created: [],
    lastUpdated: [],
    symbol: [],
    color: [],
    createdBy: [],
    lastUpdatedBy: [],
    members: [],
  });

  constructor(
    protected organisationUnitGroupService: OrganisationUnitGroupService,
    protected userService: UserService,
    protected organisationUnitService: OrganisationUnitService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ organisationUnitGroup }) => {
      if (organisationUnitGroup.id === undefined) {
        const today = dayjs().startOf('day');
        organisationUnitGroup.created = today;
        organisationUnitGroup.lastUpdated = today;
      }

      this.updateForm(organisationUnitGroup);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const organisationUnitGroup = this.createFromForm();
    if (organisationUnitGroup.id !== undefined) {
      this.subscribeToSaveResponse(this.organisationUnitGroupService.update(organisationUnitGroup));
    } else {
      this.subscribeToSaveResponse(this.organisationUnitGroupService.create(organisationUnitGroup));
    }
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrganisationUnitGroup>>): void {
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

  protected updateForm(organisationUnitGroup: IOrganisationUnitGroup): void {
    this.editForm.patchValue({
      id: organisationUnitGroup.id,
      uid: organisationUnitGroup.uid,
      code: organisationUnitGroup.code,
      name: organisationUnitGroup.name,
      shortName: organisationUnitGroup.shortName,
      created: organisationUnitGroup.created ? organisationUnitGroup.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: organisationUnitGroup.lastUpdated ? organisationUnitGroup.lastUpdated.format(DATE_TIME_FORMAT) : null,
      symbol: organisationUnitGroup.symbol,
      color: organisationUnitGroup.color,
      createdBy: organisationUnitGroup.createdBy,
      lastUpdatedBy: organisationUnitGroup.lastUpdatedBy,
      members: organisationUnitGroup.members,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      organisationUnitGroup.createdBy,
      organisationUnitGroup.lastUpdatedBy
    );
    this.organisationUnitsSharedCollection = this.organisationUnitService.addOrganisationUnitToCollectionIfMissing(
      this.organisationUnitsSharedCollection,
      ...(organisationUnitGroup.members ?? [])
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
            ...(this.editForm.get('members')!.value ?? [])
          )
        )
      )
      .subscribe((organisationUnits: IOrganisationUnit[]) => (this.organisationUnitsSharedCollection = organisationUnits));
  }

  protected createFromForm(): IOrganisationUnitGroup {
    return {
      ...new OrganisationUnitGroup(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      code: this.editForm.get(['code'])!.value,
      name: this.editForm.get(['name'])!.value,
      shortName: this.editForm.get(['shortName'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      symbol: this.editForm.get(['symbol'])!.value,
      color: this.editForm.get(['color'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      members: this.editForm.get(['members'])!.value,
    };
  }
}
