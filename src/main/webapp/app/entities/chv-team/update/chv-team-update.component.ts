import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IChvTeam, ChvTeam } from '../chv-team.model';
import { ChvTeamService } from '../service/chv-team.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { IChv } from 'app/entities/chv/chv.model';
import { ChvService } from 'app/entities/chv/service/chv.service';

@Component({
  selector: 'app-chv-team-update',
  templateUrl: './chv-team-update.component.html',
})
export class ChvTeamUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  peopleSharedCollection: IPerson[] = [];
  chvsSharedCollection: IChv[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    code: [null, []],
    name: [],
    description: [],
    created: [],
    lastUpdated: [],
    teamNo: [null, [Validators.required, Validators.pattern('^[0-9]{1,12}')]],
    teamType: [null, [Validators.required]],
    createdBy: [],
    lastUpdatedBy: [],
    person: [],
    responsibleForChvs: [],
  });

  constructor(
    protected chvTeamService: ChvTeamService,
    protected userService: UserService,
    protected personService: PersonService,
    protected chvService: ChvService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chvTeam }) => {
      if (chvTeam.id === undefined) {
        const today = dayjs().startOf('day');
        chvTeam.created = today;
        chvTeam.lastUpdated = today;
      }

      this.updateForm(chvTeam);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const chvTeam = this.createFromForm();
    if (chvTeam.id !== undefined) {
      this.subscribeToSaveResponse(this.chvTeamService.update(chvTeam));
    } else {
      this.subscribeToSaveResponse(this.chvTeamService.create(chvTeam));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackPersonById(index: number, item: IPerson): number {
    return item.id!;
  }

  trackChvById(index: number, item: IChv): number {
    return item.id!;
  }

  getSelectedChv(option: IChv, selectedVals?: IChv[]): IChv {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChvTeam>>): void {
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

  protected updateForm(chvTeam: IChvTeam): void {
    this.editForm.patchValue({
      id: chvTeam.id,
      uid: chvTeam.uid,
      code: chvTeam.code,
      name: chvTeam.name,
      description: chvTeam.description,
      created: chvTeam.created ? chvTeam.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: chvTeam.lastUpdated ? chvTeam.lastUpdated.format(DATE_TIME_FORMAT) : null,
      teamNo: chvTeam.teamNo,
      teamType: chvTeam.teamType,
      createdBy: chvTeam.createdBy,
      lastUpdatedBy: chvTeam.lastUpdatedBy,
      person: chvTeam.person,
      responsibleForChvs: chvTeam.responsibleForChvs,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      chvTeam.createdBy,
      chvTeam.lastUpdatedBy
    );
    this.peopleSharedCollection = this.personService.addPersonToCollectionIfMissing(this.peopleSharedCollection, chvTeam.person);
    this.chvsSharedCollection = this.chvService.addChvToCollectionIfMissing(
      this.chvsSharedCollection,
      ...(chvTeam.responsibleForChvs ?? [])
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
      .pipe(map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing(people, this.editForm.get('person')!.value)))
      .subscribe((people: IPerson[]) => (this.peopleSharedCollection = people));

    this.chvService
      .query()
      .pipe(map((res: HttpResponse<IChv[]>) => res.body ?? []))
      .pipe(
        map((chvs: IChv[]) => this.chvService.addChvToCollectionIfMissing(chvs, ...(this.editForm.get('responsibleForChvs')!.value ?? [])))
      )
      .subscribe((chvs: IChv[]) => (this.chvsSharedCollection = chvs));
  }

  protected createFromForm(): IChvTeam {
    return {
      ...new ChvTeam(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      code: this.editForm.get(['code'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      teamNo: this.editForm.get(['teamNo'])!.value,
      teamType: this.editForm.get(['teamType'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      person: this.editForm.get(['person'])!.value,
      responsibleForChvs: this.editForm.get(['responsibleForChvs'])!.value,
    };
  }
}
