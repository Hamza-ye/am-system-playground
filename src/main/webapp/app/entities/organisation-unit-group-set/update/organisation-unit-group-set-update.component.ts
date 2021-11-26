import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IOrganisationUnitGroupSet, OrganisationUnitGroupSet } from '../organisation-unit-group-set.model';
import { OrganisationUnitGroupSetService } from '../service/organisation-unit-group-set.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IOrganisationUnitGroup } from 'app/entities/organisation-unit-group/organisation-unit-group.model';
import { OrganisationUnitGroupService } from 'app/entities/organisation-unit-group/service/organisation-unit-group.service';

@Component({
  selector: 'app-organisation-unit-group-set-update',
  templateUrl: './organisation-unit-group-set-update.component.html',
})
export class OrganisationUnitGroupSetUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  organisationUnitGroupsSharedCollection: IOrganisationUnitGroup[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    code: [null, []],
    name: [],
    created: [],
    lastUpdated: [],
    compulsory: [],
    includeSubhierarchyInAnalytics: [],
    createdBy: [],
    lastUpdatedBy: [],
    organisationUnitGroups: [],
  });

  constructor(
    protected organisationUnitGroupSetService: OrganisationUnitGroupSetService,
    protected userService: UserService,
    protected organisationUnitGroupService: OrganisationUnitGroupService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ organisationUnitGroupSet }) => {
      if (organisationUnitGroupSet.id === undefined) {
        const today = dayjs().startOf('day');
        organisationUnitGroupSet.created = today;
        organisationUnitGroupSet.lastUpdated = today;
      }

      this.updateForm(organisationUnitGroupSet);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const organisationUnitGroupSet = this.createFromForm();
    if (organisationUnitGroupSet.id !== undefined) {
      this.subscribeToSaveResponse(this.organisationUnitGroupSetService.update(organisationUnitGroupSet));
    } else {
      this.subscribeToSaveResponse(this.organisationUnitGroupSetService.create(organisationUnitGroupSet));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackOrganisationUnitGroupById(index: number, item: IOrganisationUnitGroup): number {
    return item.id!;
  }

  getSelectedOrganisationUnitGroup(option: IOrganisationUnitGroup, selectedVals?: IOrganisationUnitGroup[]): IOrganisationUnitGroup {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrganisationUnitGroupSet>>): void {
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

  protected updateForm(organisationUnitGroupSet: IOrganisationUnitGroupSet): void {
    this.editForm.patchValue({
      id: organisationUnitGroupSet.id,
      uid: organisationUnitGroupSet.uid,
      code: organisationUnitGroupSet.code,
      name: organisationUnitGroupSet.name,
      created: organisationUnitGroupSet.created ? organisationUnitGroupSet.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: organisationUnitGroupSet.lastUpdated ? organisationUnitGroupSet.lastUpdated.format(DATE_TIME_FORMAT) : null,
      compulsory: organisationUnitGroupSet.compulsory,
      includeSubhierarchyInAnalytics: organisationUnitGroupSet.includeSubhierarchyInAnalytics,
      createdBy: organisationUnitGroupSet.createdBy,
      lastUpdatedBy: organisationUnitGroupSet.lastUpdatedBy,
      organisationUnitGroups: organisationUnitGroupSet.organisationUnitGroups,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      organisationUnitGroupSet.createdBy,
      organisationUnitGroupSet.lastUpdatedBy
    );
    this.organisationUnitGroupsSharedCollection = this.organisationUnitGroupService.addOrganisationUnitGroupToCollectionIfMissing(
      this.organisationUnitGroupsSharedCollection,
      ...(organisationUnitGroupSet.organisationUnitGroups ?? [])
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

    this.organisationUnitGroupService
      .query()
      .pipe(map((res: HttpResponse<IOrganisationUnitGroup[]>) => res.body ?? []))
      .pipe(
        map((organisationUnitGroups: IOrganisationUnitGroup[]) =>
          this.organisationUnitGroupService.addOrganisationUnitGroupToCollectionIfMissing(
            organisationUnitGroups,
            ...(this.editForm.get('organisationUnitGroups')!.value ?? [])
          )
        )
      )
      .subscribe(
        (organisationUnitGroups: IOrganisationUnitGroup[]) => (this.organisationUnitGroupsSharedCollection = organisationUnitGroups)
      );
  }

  protected createFromForm(): IOrganisationUnitGroupSet {
    return {
      ...new OrganisationUnitGroupSet(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      code: this.editForm.get(['code'])!.value,
      name: this.editForm.get(['name'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      compulsory: this.editForm.get(['compulsory'])!.value,
      includeSubhierarchyInAnalytics: this.editForm.get(['includeSubhierarchyInAnalytics'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      organisationUnitGroups: this.editForm.get(['organisationUnitGroups'])!.value,
    };
  }
}
