import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILlinsFamilyReportHistory } from '../llins-family-report-history.model';

@Component({
  selector: 'app-llins-family-report-history-detail',
  templateUrl: './llins-family-report-history-detail.component.html',
})
export class LlinsFamilyReportHistoryDetailComponent implements OnInit {
  llinsFamilyReportHistory: ILlinsFamilyReportHistory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ llinsFamilyReportHistory }) => {
      this.llinsFamilyReportHistory = llinsFamilyReportHistory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
