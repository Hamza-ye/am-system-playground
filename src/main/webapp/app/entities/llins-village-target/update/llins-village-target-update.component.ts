import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ILLINSVillageTarget, LLINSVillageTarget } from '../llins-village-target.model';
import { LLINSVillageTargetService } from '../service/llins-village-target.service';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';
import { OrganisationUnitService } from 'app/entities/organisation-unit/service/organisation-unit.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { WorkingDayService } from 'app/entities/working-day/service/working-day.service';
import { IStatusOfCoverage } from 'app/entities/status-of-coverage/status-of-coverage.model';
import { StatusOfCoverageService } from 'app/entities/status-of-coverage/service/status-of-coverage.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';

@Component({
  selector: 'app-llins-village-target-update',
  templateUrl: './llins-village-target-update.component.html',
})
export class LLINSVillageTargetUpdateComponent implements OnInit {
  isSaving = false;

  organisationUnitsSharedCollection: IOrganisationUnit[] = [];
  usersSharedCollection: IUser[] = [];
  workingDaysSharedCollection: IWorkingDay[] = [];
  statusOfCoveragesSharedCollection: IStatusOfCoverage[] = [];
  teamsSharedCollection: ITeam[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    created: [],
    lastUpdated: [],
    residentsIndividuals: [null, [Validators.required, Validators.min(0)]],
    idpsIndividuals: [null, [Validators.required, Validators.min(0)]],
    residentsFamilies: [null, [Validators.required, Validators.min(0)]],
    idpsFamilies: [null, [Validators.required, Validators.min(0)]],
    noOfDaysNeeded: [null, [Validators.min(0)]],
    quantity: [null, [Validators.required, Validators.min(0)]],
    organisationUnit: [null, Validators.required],
    createdBy: [],
    lastUpdatedBy: [],
    dayPlanned: [null, Validators.required],
    statusOfCoverage: [],
    teamAssigned: [null, Validators.required],
  });

  constructor(
    protected lLINSVillageTargetService: LLINSVillageTargetService,
    protected organisationUnitService: OrganisationUnitService,
    protected userService: UserService,
    protected workingDayService: WorkingDayService,
    protected statusOfCoverageService: StatusOfCoverageService,
    protected teamService: TeamService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lLINSVillageTarget }) => {
      if (lLINSVillageTarget.id === undefined) {
        const today = dayjs().startOf('day');
        lLINSVillageTarget.created = today;
        lLINSVillageTarget.lastUpdated = today;
      }

      this.updateForm(lLINSVillageTarget);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lLINSVillageTarget = this.createFromForm();
    if (lLINSVillageTarget.id !== undefined) {
      this.subscribeToSaveResponse(this.lLINSVillageTargetService.update(lLINSVillageTarget));
    } else {
      this.subscribeToSaveResponse(this.lLINSVillageTargetService.create(lLINSVillageTarget));
    }
  }

  trackOrganisationUnitById(index: number, item: IOrganisationUnit): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackWorkingDayById(index: number, item: IWorkingDay): number {
    return item.id!;
  }

  trackStatusOfCoverageById(index: number, item: IStatusOfCoverage): number {
    return item.id!;
  }

  trackTeamById(index: number, item: ITeam): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILLINSVillageTarget>>): void {
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

  protected updateForm(lLINSVillageTarget: ILLINSVillageTarget): void {
    this.editForm.patchValue({
      id: lLINSVillageTarget.id,
      uid: lLINSVillageTarget.uid,
      created: lLINSVillageTarget.created ? lLINSVillageTarget.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: lLINSVillageTarget.lastUpdated ? lLINSVillageTarget.lastUpdated.format(DATE_TIME_FORMAT) : null,
      residentsIndividuals: lLINSVillageTarget.residentsIndividuals,
      idpsIndividuals: lLINSVillageTarget.idpsIndividuals,
      residentsFamilies: lLINSVillageTarget.residentsFamilies,
      idpsFamilies: lLINSVillageTarget.idpsFamilies,
      noOfDaysNeeded: lLINSVillageTarget.noOfDaysNeeded,
      quantity: lLINSVillageTarget.quantity,
      organisationUnit: lLINSVillageTarget.organisationUnit,
      createdBy: lLINSVillageTarget.createdBy,
      lastUpdatedBy: lLINSVillageTarget.lastUpdatedBy,
      dayPlanned: lLINSVillageTarget.dayPlanned,
      statusOfCoverage: lLINSVillageTarget.statusOfCoverage,
      teamAssigned: lLINSVillageTarget.teamAssigned,
    });

    this.organisationUnitsSharedCollection = this.organisationUnitService.addOrganisationUnitToCollectionIfMissing(
      this.organisationUnitsSharedCollection,
      lLINSVillageTarget.organisationUnit
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      lLINSVillageTarget.createdBy,
      lLINSVillageTarget.lastUpdatedBy
    );
    this.workingDaysSharedCollection = this.workingDayService.addWorkingDayToCollectionIfMissing(
      this.workingDaysSharedCollection,
      lLINSVillageTarget.dayPlanned
    );
    this.statusOfCoveragesSharedCollection = this.statusOfCoverageService.addStatusOfCoverageToCollectionIfMissing(
      this.statusOfCoveragesSharedCollection,
      lLINSVillageTarget.statusOfCoverage
    );
    this.teamsSharedCollection = this.teamService.addTeamToCollectionIfMissing(this.teamsSharedCollection, lLINSVillageTarget.teamAssigned);
  }

  protected loadRelationshipsOptions(): void {
    this.organisationUnitService
      .query()
      .pipe(map((res: HttpResponse<IOrganisationUnit[]>) => res.body ?? []))
      .pipe(
        map((organisationUnits: IOrganisationUnit[]) =>
          this.organisationUnitService.addOrganisationUnitToCollectionIfMissing(
            organisationUnits,
            this.editForm.get('organisationUnit')!.value
          )
        )
      )
      .subscribe((organisationUnits: IOrganisationUnit[]) => (this.organisationUnitsSharedCollection = organisationUnits));

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
          this.workingDayService.addWorkingDayToCollectionIfMissing(workingDays, this.editForm.get('dayPlanned')!.value)
        )
      )
      .subscribe((workingDays: IWorkingDay[]) => (this.workingDaysSharedCollection = workingDays));

    this.statusOfCoverageService
      .query()
      .pipe(map((res: HttpResponse<IStatusOfCoverage[]>) => res.body ?? []))
      .pipe(
        map((statusOfCoverages: IStatusOfCoverage[]) =>
          this.statusOfCoverageService.addStatusOfCoverageToCollectionIfMissing(
            statusOfCoverages,
            this.editForm.get('statusOfCoverage')!.value
          )
        )
      )
      .subscribe((statusOfCoverages: IStatusOfCoverage[]) => (this.statusOfCoveragesSharedCollection = statusOfCoverages));

    this.teamService
      .query()
      .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
      .pipe(map((teams: ITeam[]) => this.teamService.addTeamToCollectionIfMissing(teams, this.editForm.get('teamAssigned')!.value)))
      .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));
  }

  protected createFromForm(): ILLINSVillageTarget {
    return {
      ...new LLINSVillageTarget(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      residentsIndividuals: this.editForm.get(['residentsIndividuals'])!.value,
      idpsIndividuals: this.editForm.get(['idpsIndividuals'])!.value,
      residentsFamilies: this.editForm.get(['residentsFamilies'])!.value,
      idpsFamilies: this.editForm.get(['idpsFamilies'])!.value,
      noOfDaysNeeded: this.editForm.get(['noOfDaysNeeded'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      organisationUnit: this.editForm.get(['organisationUnit'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      dayPlanned: this.editForm.get(['dayPlanned'])!.value,
      statusOfCoverage: this.editForm.get(['statusOfCoverage'])!.value,
      teamAssigned: this.editForm.get(['teamAssigned'])!.value,
    };
  }
}
