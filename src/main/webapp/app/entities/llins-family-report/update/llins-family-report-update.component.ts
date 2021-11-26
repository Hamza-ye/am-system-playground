import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ILLINSFamilyReport, LLINSFamilyReport } from '../llins-family-report.model';
import { LLINSFamilyReportService } from '../service/llins-family-report.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { WorkingDayService } from 'app/entities/working-day/service/working-day.service';
import { ILLINSFamilyTarget } from 'app/entities/llins-family-target/llins-family-target.model';
import { LLINSFamilyTargetService } from 'app/entities/llins-family-target/service/llins-family-target.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';

@Component({
  selector: 'app-llins-family-report-update',
  templateUrl: './llins-family-report-update.component.html',
})
export class LLINSFamilyReportUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  workingDaysSharedCollection: IWorkingDay[] = [];
  lLINSFamilyTargetsSharedCollection: ILLINSFamilyTarget[] = [];
  teamsSharedCollection: ITeam[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    created: [],
    lastUpdated: [],
    checkNo: [],
    maleIndividuals: [null, [Validators.required, Validators.min(0)]],
    femaleIndividuals: [null, [Validators.required, Validators.min(0)]],
    lessThan5Males: [null, [Validators.required, Validators.min(0)]],
    lessThan5Females: [null, [Validators.required, Validators.min(0)]],
    pregnantWomen: [null, [Validators.required, Validators.min(0)]],
    quantityReceived: [null, [Validators.required, Validators.min(0)]],
    familyType: [null, [Validators.required]],
    comment: [],
    createdBy: [],
    lastUpdatedBy: [],
    dayReached: [null, Validators.required],
    targetDetails: [],
    executingTeam: [null, Validators.required],
  });

  constructor(
    protected lLINSFamilyReportService: LLINSFamilyReportService,
    protected userService: UserService,
    protected workingDayService: WorkingDayService,
    protected lLINSFamilyTargetService: LLINSFamilyTargetService,
    protected teamService: TeamService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lLINSFamilyReport }) => {
      if (lLINSFamilyReport.id === undefined) {
        const today = dayjs().startOf('day');
        lLINSFamilyReport.created = today;
        lLINSFamilyReport.lastUpdated = today;
      }

      this.updateForm(lLINSFamilyReport);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lLINSFamilyReport = this.createFromForm();
    if (lLINSFamilyReport.id !== undefined) {
      this.subscribeToSaveResponse(this.lLINSFamilyReportService.update(lLINSFamilyReport));
    } else {
      this.subscribeToSaveResponse(this.lLINSFamilyReportService.create(lLINSFamilyReport));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackWorkingDayById(index: number, item: IWorkingDay): number {
    return item.id!;
  }

  trackLLINSFamilyTargetById(index: number, item: ILLINSFamilyTarget): number {
    return item.id!;
  }

  trackTeamById(index: number, item: ITeam): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILLINSFamilyReport>>): void {
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

  protected updateForm(lLINSFamilyReport: ILLINSFamilyReport): void {
    this.editForm.patchValue({
      id: lLINSFamilyReport.id,
      uid: lLINSFamilyReport.uid,
      created: lLINSFamilyReport.created ? lLINSFamilyReport.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: lLINSFamilyReport.lastUpdated ? lLINSFamilyReport.lastUpdated.format(DATE_TIME_FORMAT) : null,
      checkNo: lLINSFamilyReport.checkNo,
      maleIndividuals: lLINSFamilyReport.maleIndividuals,
      femaleIndividuals: lLINSFamilyReport.femaleIndividuals,
      lessThan5Males: lLINSFamilyReport.lessThan5Males,
      lessThan5Females: lLINSFamilyReport.lessThan5Females,
      pregnantWomen: lLINSFamilyReport.pregnantWomen,
      quantityReceived: lLINSFamilyReport.quantityReceived,
      familyType: lLINSFamilyReport.familyType,
      comment: lLINSFamilyReport.comment,
      createdBy: lLINSFamilyReport.createdBy,
      lastUpdatedBy: lLINSFamilyReport.lastUpdatedBy,
      dayReached: lLINSFamilyReport.dayReached,
      targetDetails: lLINSFamilyReport.targetDetails,
      executingTeam: lLINSFamilyReport.executingTeam,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      lLINSFamilyReport.createdBy,
      lLINSFamilyReport.lastUpdatedBy
    );
    this.workingDaysSharedCollection = this.workingDayService.addWorkingDayToCollectionIfMissing(
      this.workingDaysSharedCollection,
      lLINSFamilyReport.dayReached
    );
    this.lLINSFamilyTargetsSharedCollection = this.lLINSFamilyTargetService.addLLINSFamilyTargetToCollectionIfMissing(
      this.lLINSFamilyTargetsSharedCollection,
      lLINSFamilyReport.targetDetails
    );
    this.teamsSharedCollection = this.teamService.addTeamToCollectionIfMissing(this.teamsSharedCollection, lLINSFamilyReport.executingTeam);
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

    this.lLINSFamilyTargetService
      .query()
      .pipe(map((res: HttpResponse<ILLINSFamilyTarget[]>) => res.body ?? []))
      .pipe(
        map((lLINSFamilyTargets: ILLINSFamilyTarget[]) =>
          this.lLINSFamilyTargetService.addLLINSFamilyTargetToCollectionIfMissing(
            lLINSFamilyTargets,
            this.editForm.get('targetDetails')!.value
          )
        )
      )
      .subscribe((lLINSFamilyTargets: ILLINSFamilyTarget[]) => (this.lLINSFamilyTargetsSharedCollection = lLINSFamilyTargets));

    this.teamService
      .query()
      .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
      .pipe(map((teams: ITeam[]) => this.teamService.addTeamToCollectionIfMissing(teams, this.editForm.get('executingTeam')!.value)))
      .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));
  }

  protected createFromForm(): ILLINSFamilyReport {
    return {
      ...new LLINSFamilyReport(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      checkNo: this.editForm.get(['checkNo'])!.value,
      maleIndividuals: this.editForm.get(['maleIndividuals'])!.value,
      femaleIndividuals: this.editForm.get(['femaleIndividuals'])!.value,
      lessThan5Males: this.editForm.get(['lessThan5Males'])!.value,
      lessThan5Females: this.editForm.get(['lessThan5Females'])!.value,
      pregnantWomen: this.editForm.get(['pregnantWomen'])!.value,
      quantityReceived: this.editForm.get(['quantityReceived'])!.value,
      familyType: this.editForm.get(['familyType'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      dayReached: this.editForm.get(['dayReached'])!.value,
      targetDetails: this.editForm.get(['targetDetails'])!.value,
      executingTeam: this.editForm.get(['executingTeam'])!.value,
    };
  }
}
