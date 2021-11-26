import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ILLINSVillageReport, LLINSVillageReport } from '../llins-village-report.model';
import { LLINSVillageReportService } from '../service/llins-village-report.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { WorkingDayService } from 'app/entities/working-day/service/working-day.service';
import { ILLINSVillageTarget } from 'app/entities/llins-village-target/llins-village-target.model';
import { LLINSVillageTargetService } from 'app/entities/llins-village-target/service/llins-village-target.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';

@Component({
  selector: 'app-llins-village-report-update',
  templateUrl: './llins-village-report-update.component.html',
})
export class LLINSVillageReportUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  workingDaysSharedCollection: IWorkingDay[] = [];
  lLINSVillageTargetsSharedCollection: ILLINSVillageTarget[] = [];
  teamsSharedCollection: ITeam[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    created: [],
    lastUpdated: [],
    houses: [null, [Validators.required, Validators.min(0)]],
    residentHousehold: [null, [Validators.required, Validators.min(0)]],
    idpsHousehold: [null, [Validators.required, Validators.min(0)]],
    maleIndividuals: [null, [Validators.min(0)]],
    femaleIndividuals: [null, [Validators.min(0)]],
    lessThan5Males: [null, [Validators.min(0)]],
    lessThan5Females: [null, [Validators.min(0)]],
    pregnantWomen: [null, [Validators.min(0)]],
    quantityReceived: [null, [Validators.required, Validators.min(0)]],
    comment: [],
    createdBy: [],
    lastUpdatedBy: [],
    dayReached: [null, Validators.required],
    targetDetails: [],
    executingTeam: [null, Validators.required],
  });

  constructor(
    protected lLINSVillageReportService: LLINSVillageReportService,
    protected userService: UserService,
    protected workingDayService: WorkingDayService,
    protected lLINSVillageTargetService: LLINSVillageTargetService,
    protected teamService: TeamService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lLINSVillageReport }) => {
      if (lLINSVillageReport.id === undefined) {
        const today = dayjs().startOf('day');
        lLINSVillageReport.created = today;
        lLINSVillageReport.lastUpdated = today;
      }

      this.updateForm(lLINSVillageReport);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lLINSVillageReport = this.createFromForm();
    if (lLINSVillageReport.id !== undefined) {
      this.subscribeToSaveResponse(this.lLINSVillageReportService.update(lLINSVillageReport));
    } else {
      this.subscribeToSaveResponse(this.lLINSVillageReportService.create(lLINSVillageReport));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackWorkingDayById(index: number, item: IWorkingDay): number {
    return item.id!;
  }

  trackLLINSVillageTargetById(index: number, item: ILLINSVillageTarget): number {
    return item.id!;
  }

  trackTeamById(index: number, item: ITeam): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILLINSVillageReport>>): void {
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

  protected updateForm(lLINSVillageReport: ILLINSVillageReport): void {
    this.editForm.patchValue({
      id: lLINSVillageReport.id,
      uid: lLINSVillageReport.uid,
      created: lLINSVillageReport.created ? lLINSVillageReport.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: lLINSVillageReport.lastUpdated ? lLINSVillageReport.lastUpdated.format(DATE_TIME_FORMAT) : null,
      houses: lLINSVillageReport.houses,
      residentHousehold: lLINSVillageReport.residentHousehold,
      idpsHousehold: lLINSVillageReport.idpsHousehold,
      maleIndividuals: lLINSVillageReport.maleIndividuals,
      femaleIndividuals: lLINSVillageReport.femaleIndividuals,
      lessThan5Males: lLINSVillageReport.lessThan5Males,
      lessThan5Females: lLINSVillageReport.lessThan5Females,
      pregnantWomen: lLINSVillageReport.pregnantWomen,
      quantityReceived: lLINSVillageReport.quantityReceived,
      comment: lLINSVillageReport.comment,
      createdBy: lLINSVillageReport.createdBy,
      lastUpdatedBy: lLINSVillageReport.lastUpdatedBy,
      dayReached: lLINSVillageReport.dayReached,
      targetDetails: lLINSVillageReport.targetDetails,
      executingTeam: lLINSVillageReport.executingTeam,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      lLINSVillageReport.createdBy,
      lLINSVillageReport.lastUpdatedBy
    );
    this.workingDaysSharedCollection = this.workingDayService.addWorkingDayToCollectionIfMissing(
      this.workingDaysSharedCollection,
      lLINSVillageReport.dayReached
    );
    this.lLINSVillageTargetsSharedCollection = this.lLINSVillageTargetService.addLLINSVillageTargetToCollectionIfMissing(
      this.lLINSVillageTargetsSharedCollection,
      lLINSVillageReport.targetDetails
    );
    this.teamsSharedCollection = this.teamService.addTeamToCollectionIfMissing(
      this.teamsSharedCollection,
      lLINSVillageReport.executingTeam
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

    this.workingDayService
      .query()
      .pipe(map((res: HttpResponse<IWorkingDay[]>) => res.body ?? []))
      .pipe(
        map((workingDays: IWorkingDay[]) =>
          this.workingDayService.addWorkingDayToCollectionIfMissing(workingDays, this.editForm.get('dayReached')!.value)
        )
      )
      .subscribe((workingDays: IWorkingDay[]) => (this.workingDaysSharedCollection = workingDays));

    this.lLINSVillageTargetService
      .query()
      .pipe(map((res: HttpResponse<ILLINSVillageTarget[]>) => res.body ?? []))
      .pipe(
        map((lLINSVillageTargets: ILLINSVillageTarget[]) =>
          this.lLINSVillageTargetService.addLLINSVillageTargetToCollectionIfMissing(
            lLINSVillageTargets,
            this.editForm.get('targetDetails')!.value
          )
        )
      )
      .subscribe((lLINSVillageTargets: ILLINSVillageTarget[]) => (this.lLINSVillageTargetsSharedCollection = lLINSVillageTargets));

    this.teamService
      .query()
      .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
      .pipe(map((teams: ITeam[]) => this.teamService.addTeamToCollectionIfMissing(teams, this.editForm.get('executingTeam')!.value)))
      .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));
  }

  protected createFromForm(): ILLINSVillageReport {
    return {
      ...new LLINSVillageReport(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      houses: this.editForm.get(['houses'])!.value,
      residentHousehold: this.editForm.get(['residentHousehold'])!.value,
      idpsHousehold: this.editForm.get(['idpsHousehold'])!.value,
      maleIndividuals: this.editForm.get(['maleIndividuals'])!.value,
      femaleIndividuals: this.editForm.get(['femaleIndividuals'])!.value,
      lessThan5Males: this.editForm.get(['lessThan5Males'])!.value,
      lessThan5Females: this.editForm.get(['lessThan5Females'])!.value,
      pregnantWomen: this.editForm.get(['pregnantWomen'])!.value,
      quantityReceived: this.editForm.get(['quantityReceived'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      dayReached: this.editForm.get(['dayReached'])!.value,
      targetDetails: this.editForm.get(['targetDetails'])!.value,
      executingTeam: this.editForm.get(['executingTeam'])!.value,
    };
  }
}
