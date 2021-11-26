import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICHVMalariaCaseReport, CHVMalariaCaseReport } from '../chv-malaria-case-report.model';
import { CHVMalariaCaseReportService } from '../service/chv-malaria-case-report.service';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';
import { OrganisationUnitService } from 'app/entities/organisation-unit/service/organisation-unit.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ICHV } from 'app/entities/chv/chv.model';
import { CHVService } from 'app/entities/chv/service/chv.service';
import { ICasesReportClass } from 'app/entities/cases-report-class/cases-report-class.model';
import { CasesReportClassService } from 'app/entities/cases-report-class/service/cases-report-class.service';

@Component({
  selector: 'app-chv-malaria-case-report-update',
  templateUrl: './chv-malaria-case-report-update.component.html',
})
export class CHVMalariaCaseReportUpdateComponent implements OnInit {
  isSaving = false;

  organisationUnitsSharedCollection: IOrganisationUnit[] = [];
  usersSharedCollection: IUser[] = [];
  cHVSSharedCollection: ICHV[] = [];
  casesReportClassesSharedCollection: ICasesReportClass[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    created: [],
    lastUpdated: [],
    date: [],
    individualName: [],
    gender: [],
    isPregnant: [],
    malariaTestResult: [],
    drugsGiven: [],
    suppsGiven: [],
    referral: [],
    barImageUrl: [],
    comment: [],
    subVillage: [],
    createdBy: [],
    lastUpdatedBy: [],
    chv: [null, Validators.required],
    reportClass: [null, Validators.required],
  });

  constructor(
    protected cHVMalariaCaseReportService: CHVMalariaCaseReportService,
    protected organisationUnitService: OrganisationUnitService,
    protected userService: UserService,
    protected cHVService: CHVService,
    protected casesReportClassService: CasesReportClassService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cHVMalariaCaseReport }) => {
      if (cHVMalariaCaseReport.id === undefined) {
        const today = dayjs().startOf('day');
        cHVMalariaCaseReport.created = today;
        cHVMalariaCaseReport.lastUpdated = today;
      }

      this.updateForm(cHVMalariaCaseReport);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cHVMalariaCaseReport = this.createFromForm();
    if (cHVMalariaCaseReport.id !== undefined) {
      this.subscribeToSaveResponse(this.cHVMalariaCaseReportService.update(cHVMalariaCaseReport));
    } else {
      this.subscribeToSaveResponse(this.cHVMalariaCaseReportService.create(cHVMalariaCaseReport));
    }
  }

  trackOrganisationUnitById(index: number, item: IOrganisationUnit): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackCHVById(index: number, item: ICHV): number {
    return item.id!;
  }

  trackCasesReportClassById(index: number, item: ICasesReportClass): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICHVMalariaCaseReport>>): void {
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

  protected updateForm(cHVMalariaCaseReport: ICHVMalariaCaseReport): void {
    this.editForm.patchValue({
      id: cHVMalariaCaseReport.id,
      uid: cHVMalariaCaseReport.uid,
      created: cHVMalariaCaseReport.created ? cHVMalariaCaseReport.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: cHVMalariaCaseReport.lastUpdated ? cHVMalariaCaseReport.lastUpdated.format(DATE_TIME_FORMAT) : null,
      date: cHVMalariaCaseReport.date,
      individualName: cHVMalariaCaseReport.individualName,
      gender: cHVMalariaCaseReport.gender,
      isPregnant: cHVMalariaCaseReport.isPregnant,
      malariaTestResult: cHVMalariaCaseReport.malariaTestResult,
      drugsGiven: cHVMalariaCaseReport.drugsGiven,
      suppsGiven: cHVMalariaCaseReport.suppsGiven,
      referral: cHVMalariaCaseReport.referral,
      barImageUrl: cHVMalariaCaseReport.barImageUrl,
      comment: cHVMalariaCaseReport.comment,
      subVillage: cHVMalariaCaseReport.subVillage,
      createdBy: cHVMalariaCaseReport.createdBy,
      lastUpdatedBy: cHVMalariaCaseReport.lastUpdatedBy,
      chv: cHVMalariaCaseReport.chv,
      reportClass: cHVMalariaCaseReport.reportClass,
    });

    this.organisationUnitsSharedCollection = this.organisationUnitService.addOrganisationUnitToCollectionIfMissing(
      this.organisationUnitsSharedCollection,
      cHVMalariaCaseReport.subVillage
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      cHVMalariaCaseReport.createdBy,
      cHVMalariaCaseReport.lastUpdatedBy
    );
    this.cHVSSharedCollection = this.cHVService.addCHVToCollectionIfMissing(this.cHVSSharedCollection, cHVMalariaCaseReport.chv);
    this.casesReportClassesSharedCollection = this.casesReportClassService.addCasesReportClassToCollectionIfMissing(
      this.casesReportClassesSharedCollection,
      cHVMalariaCaseReport.reportClass
    );
  }

  protected loadRelationshipsOptions(): void {
    this.organisationUnitService
      .query()
      .pipe(map((res: HttpResponse<IOrganisationUnit[]>) => res.body ?? []))
      .pipe(
        map((organisationUnits: IOrganisationUnit[]) =>
          this.organisationUnitService.addOrganisationUnitToCollectionIfMissing(organisationUnits, this.editForm.get('subVillage')!.value)
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

    this.cHVService
      .query()
      .pipe(map((res: HttpResponse<ICHV[]>) => res.body ?? []))
      .pipe(map((cHVS: ICHV[]) => this.cHVService.addCHVToCollectionIfMissing(cHVS, this.editForm.get('chv')!.value)))
      .subscribe((cHVS: ICHV[]) => (this.cHVSSharedCollection = cHVS));

    this.casesReportClassService
      .query()
      .pipe(map((res: HttpResponse<ICasesReportClass[]>) => res.body ?? []))
      .pipe(
        map((casesReportClasses: ICasesReportClass[]) =>
          this.casesReportClassService.addCasesReportClassToCollectionIfMissing(casesReportClasses, this.editForm.get('reportClass')!.value)
        )
      )
      .subscribe((casesReportClasses: ICasesReportClass[]) => (this.casesReportClassesSharedCollection = casesReportClasses));
  }

  protected createFromForm(): ICHVMalariaCaseReport {
    return {
      ...new CHVMalariaCaseReport(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      date: this.editForm.get(['date'])!.value,
      individualName: this.editForm.get(['individualName'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      isPregnant: this.editForm.get(['isPregnant'])!.value,
      malariaTestResult: this.editForm.get(['malariaTestResult'])!.value,
      drugsGiven: this.editForm.get(['drugsGiven'])!.value,
      suppsGiven: this.editForm.get(['suppsGiven'])!.value,
      referral: this.editForm.get(['referral'])!.value,
      barImageUrl: this.editForm.get(['barImageUrl'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      subVillage: this.editForm.get(['subVillage'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      chv: this.editForm.get(['chv'])!.value,
      reportClass: this.editForm.get(['reportClass'])!.value,
    };
  }
}
