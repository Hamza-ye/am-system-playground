import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IRelativePeriods, RelativePeriods } from '../relative-periods.model';
import { RelativePeriodsService } from '../service/relative-periods.service';

@Component({
  selector: 'app-relative-periods-update',
  templateUrl: './relative-periods-update.component.html',
})
export class RelativePeriodsUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    thisDay: [],
    yesterday: [],
    last3Days: [],
    last7Days: [],
    last14Days: [],
    thisMonth: [],
    lastMonth: [],
    thisBimonth: [],
    lastBimonth: [],
    thisQuarter: [],
    lastQuarter: [],
    thisSixMonth: [],
    lastSixMonth: [],
    weeksThisYear: [],
    monthsThisYear: [],
    biMonthsThisYear: [],
    quartersThisYear: [],
    thisYear: [],
    monthsLastYear: [],
    quartersLastYear: [],
    lastYear: [],
    last5Years: [],
    last12Months: [],
    last6Months: [],
    last3Months: [],
    last6BiMonths: [],
    last4Quarters: [],
    last2SixMonths: [],
    thisFinancialYear: [],
    lastFinancialYear: [],
    last5FinancialYears: [],
    thisWeek: [],
    lastWeek: [],
    thisBiWeek: [],
    lastBiWeek: [],
    last4Weeks: [],
    last4BiWeeks: [],
    last12Weeks: [],
    last52Weeks: [],
  });

  constructor(
    protected relativePeriodsService: RelativePeriodsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ relativePeriods }) => {
      this.updateForm(relativePeriods);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const relativePeriods = this.createFromForm();
    if (relativePeriods.id !== undefined) {
      this.subscribeToSaveResponse(this.relativePeriodsService.update(relativePeriods));
    } else {
      this.subscribeToSaveResponse(this.relativePeriodsService.create(relativePeriods));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRelativePeriods>>): void {
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

  protected updateForm(relativePeriods: IRelativePeriods): void {
    this.editForm.patchValue({
      id: relativePeriods.id,
      thisDay: relativePeriods.thisDay,
      yesterday: relativePeriods.yesterday,
      last3Days: relativePeriods.last3Days,
      last7Days: relativePeriods.last7Days,
      last14Days: relativePeriods.last14Days,
      thisMonth: relativePeriods.thisMonth,
      lastMonth: relativePeriods.lastMonth,
      thisBimonth: relativePeriods.thisBimonth,
      lastBimonth: relativePeriods.lastBimonth,
      thisQuarter: relativePeriods.thisQuarter,
      lastQuarter: relativePeriods.lastQuarter,
      thisSixMonth: relativePeriods.thisSixMonth,
      lastSixMonth: relativePeriods.lastSixMonth,
      weeksThisYear: relativePeriods.weeksThisYear,
      monthsThisYear: relativePeriods.monthsThisYear,
      biMonthsThisYear: relativePeriods.biMonthsThisYear,
      quartersThisYear: relativePeriods.quartersThisYear,
      thisYear: relativePeriods.thisYear,
      monthsLastYear: relativePeriods.monthsLastYear,
      quartersLastYear: relativePeriods.quartersLastYear,
      lastYear: relativePeriods.lastYear,
      last5Years: relativePeriods.last5Years,
      last12Months: relativePeriods.last12Months,
      last6Months: relativePeriods.last6Months,
      last3Months: relativePeriods.last3Months,
      last6BiMonths: relativePeriods.last6BiMonths,
      last4Quarters: relativePeriods.last4Quarters,
      last2SixMonths: relativePeriods.last2SixMonths,
      thisFinancialYear: relativePeriods.thisFinancialYear,
      lastFinancialYear: relativePeriods.lastFinancialYear,
      last5FinancialYears: relativePeriods.last5FinancialYears,
      thisWeek: relativePeriods.thisWeek,
      lastWeek: relativePeriods.lastWeek,
      thisBiWeek: relativePeriods.thisBiWeek,
      lastBiWeek: relativePeriods.lastBiWeek,
      last4Weeks: relativePeriods.last4Weeks,
      last4BiWeeks: relativePeriods.last4BiWeeks,
      last12Weeks: relativePeriods.last12Weeks,
      last52Weeks: relativePeriods.last52Weeks,
    });
  }

  protected createFromForm(): IRelativePeriods {
    return {
      ...new RelativePeriods(),
      id: this.editForm.get(['id'])!.value,
      thisDay: this.editForm.get(['thisDay'])!.value,
      yesterday: this.editForm.get(['yesterday'])!.value,
      last3Days: this.editForm.get(['last3Days'])!.value,
      last7Days: this.editForm.get(['last7Days'])!.value,
      last14Days: this.editForm.get(['last14Days'])!.value,
      thisMonth: this.editForm.get(['thisMonth'])!.value,
      lastMonth: this.editForm.get(['lastMonth'])!.value,
      thisBimonth: this.editForm.get(['thisBimonth'])!.value,
      lastBimonth: this.editForm.get(['lastBimonth'])!.value,
      thisQuarter: this.editForm.get(['thisQuarter'])!.value,
      lastQuarter: this.editForm.get(['lastQuarter'])!.value,
      thisSixMonth: this.editForm.get(['thisSixMonth'])!.value,
      lastSixMonth: this.editForm.get(['lastSixMonth'])!.value,
      weeksThisYear: this.editForm.get(['weeksThisYear'])!.value,
      monthsThisYear: this.editForm.get(['monthsThisYear'])!.value,
      biMonthsThisYear: this.editForm.get(['biMonthsThisYear'])!.value,
      quartersThisYear: this.editForm.get(['quartersThisYear'])!.value,
      thisYear: this.editForm.get(['thisYear'])!.value,
      monthsLastYear: this.editForm.get(['monthsLastYear'])!.value,
      quartersLastYear: this.editForm.get(['quartersLastYear'])!.value,
      lastYear: this.editForm.get(['lastYear'])!.value,
      last5Years: this.editForm.get(['last5Years'])!.value,
      last12Months: this.editForm.get(['last12Months'])!.value,
      last6Months: this.editForm.get(['last6Months'])!.value,
      last3Months: this.editForm.get(['last3Months'])!.value,
      last6BiMonths: this.editForm.get(['last6BiMonths'])!.value,
      last4Quarters: this.editForm.get(['last4Quarters'])!.value,
      last2SixMonths: this.editForm.get(['last2SixMonths'])!.value,
      thisFinancialYear: this.editForm.get(['thisFinancialYear'])!.value,
      lastFinancialYear: this.editForm.get(['lastFinancialYear'])!.value,
      last5FinancialYears: this.editForm.get(['last5FinancialYears'])!.value,
      thisWeek: this.editForm.get(['thisWeek'])!.value,
      lastWeek: this.editForm.get(['lastWeek'])!.value,
      thisBiWeek: this.editForm.get(['thisBiWeek'])!.value,
      lastBiWeek: this.editForm.get(['lastBiWeek'])!.value,
      last4Weeks: this.editForm.get(['last4Weeks'])!.value,
      last4BiWeeks: this.editForm.get(['last4BiWeeks'])!.value,
      last12Weeks: this.editForm.get(['last12Weeks'])!.value,
      last52Weeks: this.editForm.get(['last52Weeks'])!.value,
    };
  }
}
