import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChvMalariaReportVersion1 } from '../chv-malaria-report-version-1.model';

@Component({
  selector: 'app-chv-malaria-report-version-1-detail',
  templateUrl: './chv-malaria-report-version-1-detail.component.html',
})
export class ChvMalariaReportVersion1DetailComponent implements OnInit {
  chvMalariaReportVersion1: IChvMalariaReportVersion1 | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chvMalariaReportVersion1 }) => {
      this.chvMalariaReportVersion1 = chvMalariaReportVersion1;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
