import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILLINSFamilyReportHistory } from '../llins-family-report-history.model';

@Component({
  selector: 'app-llins-family-report-history-detail',
  templateUrl: './llins-family-report-history-detail.component.html',
})
export class LLINSFamilyReportHistoryDetailComponent implements OnInit {
  lLINSFamilyReportHistory: ILLINSFamilyReportHistory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lLINSFamilyReportHistory }) => {
      this.lLINSFamilyReportHistory = lLINSFamilyReportHistory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
