import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPeopleGroup, PeopleGroup } from '../people-group.model';
import { PeopleGroupService } from '../service/people-group.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';

@Component({
  selector: 'app-people-group-update',
  templateUrl: './people-group-update.component.html',
})
export class PeopleGroupUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  peopleSharedCollection: IPerson[] = [];
  peopleGroupsSharedCollection: IPeopleGroup[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    code: [null, []],
    name: [],
    created: [],
    lastUpdated: [],
    uuid: [],
    createdBy: [],
    lastUpdatedBy: [],
    members: [],
    managedGroups: [],
  });

  constructor(
    protected peopleGroupService: PeopleGroupService,
    protected userService: UserService,
    protected personService: PersonService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ peopleGroup }) => {
      if (peopleGroup.id === undefined) {
        const today = dayjs().startOf('day');
        peopleGroup.created = today;
        peopleGroup.lastUpdated = today;
      }

      this.updateForm(peopleGroup);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const peopleGroup = this.createFromForm();
    if (peopleGroup.id !== undefined) {
      this.subscribeToSaveResponse(this.peopleGroupService.update(peopleGroup));
    } else {
      this.subscribeToSaveResponse(this.peopleGroupService.create(peopleGroup));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackPersonById(index: number, item: IPerson): number {
    return item.id!;
  }

  trackPeopleGroupById(index: number, item: IPeopleGroup): number {
    return item.id!;
  }

  getSelectedPerson(option: IPerson, selectedVals?: IPerson[]): IPerson {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedPeopleGroup(option: IPeopleGroup, selectedVals?: IPeopleGroup[]): IPeopleGroup {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPeopleGroup>>): void {
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

  protected updateForm(peopleGroup: IPeopleGroup): void {
    this.editForm.patchValue({
      id: peopleGroup.id,
      uid: peopleGroup.uid,
      code: peopleGroup.code,
      name: peopleGroup.name,
      created: peopleGroup.created ? peopleGroup.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: peopleGroup.lastUpdated ? peopleGroup.lastUpdated.format(DATE_TIME_FORMAT) : null,
      uuid: peopleGroup.uuid,
      createdBy: peopleGroup.createdBy,
      lastUpdatedBy: peopleGroup.lastUpdatedBy,
      members: peopleGroup.members,
      managedGroups: peopleGroup.managedGroups,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      peopleGroup.createdBy,
      peopleGroup.lastUpdatedBy
    );
    this.peopleSharedCollection = this.personService.addPersonToCollectionIfMissing(
      this.peopleSharedCollection,
      ...(peopleGroup.members ?? [])
    );
    this.peopleGroupsSharedCollection = this.peopleGroupService.addPeopleGroupToCollectionIfMissing(
      this.peopleGroupsSharedCollection,
      ...(peopleGroup.managedGroups ?? [])
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

    this.personService
      .query()
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .pipe(
        map((people: IPerson[]) =>
          this.personService.addPersonToCollectionIfMissing(people, ...(this.editForm.get('members')!.value ?? []))
        )
      )
      .subscribe((people: IPerson[]) => (this.peopleSharedCollection = people));

    this.peopleGroupService
      .query()
      .pipe(map((res: HttpResponse<IPeopleGroup[]>) => res.body ?? []))
      .pipe(
        map((peopleGroups: IPeopleGroup[]) =>
          this.peopleGroupService.addPeopleGroupToCollectionIfMissing(peopleGroups, ...(this.editForm.get('managedGroups')!.value ?? []))
        )
      )
      .subscribe((peopleGroups: IPeopleGroup[]) => (this.peopleGroupsSharedCollection = peopleGroups));
  }

  protected createFromForm(): IPeopleGroup {
    return {
      ...new PeopleGroup(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      code: this.editForm.get(['code'])!.value,
      name: this.editForm.get(['name'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      uuid: this.editForm.get(['uuid'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      members: this.editForm.get(['members'])!.value,
      managedGroups: this.editForm.get(['managedGroups'])!.value,
    };
  }
}
