import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILlinsFamilyReport } from '../llins-family-report.model';

@Component({
  selector: 'app-llins-family-report-detail',
  templateUrl: './llins-family-report-detail.component.html',
})
export class LlinsFamilyReportDetailComponent implements OnInit {
  llinsFamilyReport: ILlinsFamilyReport | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ llinsFamilyReport }) => {
      this.llinsFamilyReport = llinsFamilyReport;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
