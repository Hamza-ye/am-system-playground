import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILLINSFamilyReport } from '../llins-family-report.model';

@Component({
  selector: 'app-llins-family-report-detail',
  templateUrl: './llins-family-report-detail.component.html',
})
export class LLINSFamilyReportDetailComponent implements OnInit {
  lLINSFamilyReport: ILLINSFamilyReport | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lLINSFamilyReport }) => {
      this.lLINSFamilyReport = lLINSFamilyReport;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
