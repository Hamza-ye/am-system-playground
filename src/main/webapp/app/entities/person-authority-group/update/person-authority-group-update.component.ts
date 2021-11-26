import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPersonAuthorityGroup, PersonAuthorityGroup } from '../person-authority-group.model';
import { PersonAuthorityGroupService } from '../service/person-authority-group.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'app-person-authority-group-update',
  templateUrl: './person-authority-group-update.component.html',
})
export class PersonAuthorityGroupUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    code: [null, []],
    name: [],
    description: [],
    created: [],
    lastUpdated: [],
    createdBy: [],
    lastUpdatedBy: [],
  });

  constructor(
    protected personAuthorityGroupService: PersonAuthorityGroupService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personAuthorityGroup }) => {
      if (personAuthorityGroup.id === undefined) {
        const today = dayjs().startOf('day');
        personAuthorityGroup.created = today;
        personAuthorityGroup.lastUpdated = today;
      }

      this.updateForm(personAuthorityGroup);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const personAuthorityGroup = this.createFromForm();
    if (personAuthorityGroup.id !== undefined) {
      this.subscribeToSaveResponse(this.personAuthorityGroupService.update(personAuthorityGroup));
    } else {
      this.subscribeToSaveResponse(this.personAuthorityGroupService.create(personAuthorityGroup));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersonAuthorityGroup>>): void {
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

  protected updateForm(personAuthorityGroup: IPersonAuthorityGroup): void {
    this.editForm.patchValue({
      id: personAuthorityGroup.id,
      uid: personAuthorityGroup.uid,
      code: personAuthorityGroup.code,
      name: personAuthorityGroup.name,
      description: personAuthorityGroup.description,
      created: personAuthorityGroup.created ? personAuthorityGroup.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: personAuthorityGroup.lastUpdated ? personAuthorityGroup.lastUpdated.format(DATE_TIME_FORMAT) : null,
      createdBy: personAuthorityGroup.createdBy,
      lastUpdatedBy: personAuthorityGroup.lastUpdatedBy,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      personAuthorityGroup.createdBy,
      personAuthorityGroup.lastUpdatedBy
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
  }

  protected createFromForm(): IPersonAuthorityGroup {
    return {
      ...new PersonAuthorityGroup(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      code: this.editForm.get(['code'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
    };
  }
}
