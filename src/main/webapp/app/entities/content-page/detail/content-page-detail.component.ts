import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContentPage } from '../content-page.model';

@Component({
  selector: 'app-content-page-detail',
  templateUrl: './content-page-detail.component.html',
})
export class ContentPageDetailComponent implements OnInit {
  contentPage: IContentPage | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contentPage }) => {
      this.contentPage = contentPage;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
