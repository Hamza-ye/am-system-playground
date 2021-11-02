import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IOrganisationUnitLevel, OrganisationUnitLevel } from '../organisation-unit-level.model';
import { OrganisationUnitLevelService } from '../service/organisation-unit-level.service';

@Component({
  selector: 'app-organisation-unit-level-update',
  templateUrl: './organisation-unit-level-update.component.html',
})
export class OrganisationUnitLevelUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    code: [null, []],
    name: [],
    created: [],
    lastUpdated: [],
    level: [],
    offlineLevels: [],
  });

  constructor(
    protected organisationUnitLevelService: OrganisationUnitLevelService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ organisationUnitLevel }) => {
      if (organisationUnitLevel.id === undefined) {
        const today = dayjs().startOf('day');
        organisationUnitLevel.created = today;
        organisationUnitLevel.lastUpdated = today;
      }

      this.updateForm(organisationUnitLevel);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const organisationUnitLevel = this.createFromForm();
    if (organisationUnitLevel.id !== undefined) {
      this.subscribeToSaveResponse(this.organisationUnitLevelService.update(organisationUnitLevel));
    } else {
      this.subscribeToSaveResponse(this.organisationUnitLevelService.create(organisationUnitLevel));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrganisationUnitLevel>>): void {
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

  protected updateForm(organisationUnitLevel: IOrganisationUnitLevel): void {
    this.editForm.patchValue({
      id: organisationUnitLevel.id,
      uid: organisationUnitLevel.uid,
      code: organisationUnitLevel.code,
      name: organisationUnitLevel.name,
      created: organisationUnitLevel.created ? organisationUnitLevel.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: organisationUnitLevel.lastUpdated ? organisationUnitLevel.lastUpdated.format(DATE_TIME_FORMAT) : null,
      level: organisationUnitLevel.level,
      offlineLevels: organisationUnitLevel.offlineLevels,
    });
  }

  protected createFromForm(): IOrganisationUnitLevel {
    return {
      ...new OrganisationUnitLevel(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      code: this.editForm.get(['code'])!.value,
      name: this.editForm.get(['name'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      level: this.editForm.get(['level'])!.value,
      offlineLevels: this.editForm.get(['offlineLevels'])!.value,
    };
  }
}
