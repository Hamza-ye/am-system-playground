import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IWorkingDay, WorkingDay } from '../working-day.model';
import { WorkingDayService } from '../service/working-day.service';

@Component({
  selector: 'app-working-day-update',
  templateUrl: './working-day-update.component.html',
})
export class WorkingDayUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    dayNo: [null, [Validators.required, Validators.min(0)]],
    dayLabel: [null, [Validators.required, Validators.maxLength(20)]],
  });

  constructor(protected workingDayService: WorkingDayService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workingDay }) => {
      this.updateForm(workingDay);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const workingDay = this.createFromForm();
    if (workingDay.id !== undefined) {
      this.subscribeToSaveResponse(this.workingDayService.update(workingDay));
    } else {
      this.subscribeToSaveResponse(this.workingDayService.create(workingDay));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkingDay>>): void {
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

  protected updateForm(workingDay: IWorkingDay): void {
    this.editForm.patchValue({
      id: workingDay.id,
      dayNo: workingDay.dayNo,
      dayLabel: workingDay.dayLabel,
    });
  }

  protected createFromForm(): IWorkingDay {
    return {
      ...new WorkingDay(),
      id: this.editForm.get(['id'])!.value,
      dayNo: this.editForm.get(['dayNo'])!.value,
      dayLabel: this.editForm.get(['dayLabel'])!.value,
    };
  }
}
