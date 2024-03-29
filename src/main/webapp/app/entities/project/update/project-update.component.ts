import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IProject, Project } from '../project.model';
import { ProjectService } from '../service/project.service';
import { IContentPage } from 'app/entities/content-page/content-page.model';
import { ContentPageService } from 'app/entities/content-page/service/content-page.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'app-project-update',
  templateUrl: './project-update.component.html',
})
export class ProjectUpdateComponent implements OnInit {
  isSaving = false;

  contentPagesCollection: IContentPage[] = [];
  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    code: [null, []],
    name: [],
    created: [],
    lastUpdated: [],
    displayed: [],
    contentPage: [],
    createdBy: [],
    lastUpdatedBy: [],
  });

  constructor(
    protected projectService: ProjectService,
    protected contentPageService: ContentPageService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ project }) => {
      if (project.id === undefined) {
        const today = dayjs().startOf('day');
        project.created = today;
        project.lastUpdated = today;
      }

      this.updateForm(project);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const project = this.createFromForm();
    if (project.id !== undefined) {
      this.subscribeToSaveResponse(this.projectService.update(project));
    } else {
      this.subscribeToSaveResponse(this.projectService.create(project));
    }
  }

  trackContentPageById(index: number, item: IContentPage): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProject>>): void {
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

  protected updateForm(project: IProject): void {
    this.editForm.patchValue({
      id: project.id,
      uid: project.uid,
      code: project.code,
      name: project.name,
      created: project.created ? project.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: project.lastUpdated ? project.lastUpdated.format(DATE_TIME_FORMAT) : null,
      displayed: project.displayed,
      contentPage: project.contentPage,
      createdBy: project.createdBy,
      lastUpdatedBy: project.lastUpdatedBy,
    });

    this.contentPagesCollection = this.contentPageService.addContentPageToCollectionIfMissing(
      this.contentPagesCollection,
      project.contentPage
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      project.createdBy,
      project.lastUpdatedBy
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contentPageService
      .query({ filter: 'project-is-null' })
      .pipe(map((res: HttpResponse<IContentPage[]>) => res.body ?? []))
      .pipe(
        map((contentPages: IContentPage[]) =>
          this.contentPageService.addContentPageToCollectionIfMissing(contentPages, this.editForm.get('contentPage')!.value)
        )
      )
      .subscribe((contentPages: IContentPage[]) => (this.contentPagesCollection = contentPages));

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

  protected createFromForm(): IProject {
    return {
      ...new Project(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      code: this.editForm.get(['code'])!.value,
      name: this.editForm.get(['name'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      displayed: this.editForm.get(['displayed'])!.value,
      contentPage: this.editForm.get(['contentPage'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
    };
  }
}
