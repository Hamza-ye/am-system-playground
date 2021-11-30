import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProject } from '../../../../entities/project/project.model';

@Component({
  selector: 'app-project-page-detail',
  templateUrl: './project-page-detail.component.html',
})
export class ProjectPageDetailComponent implements OnInit {
  project: IProject | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ project }) => {
      this.project = project;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
