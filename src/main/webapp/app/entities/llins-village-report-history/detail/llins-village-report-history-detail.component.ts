import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILLINSVillageReportHistory } from '../llins-village-report-history.model';

@Component({
  selector: 'app-llins-village-report-history-detail',
  templateUrl: './llins-village-report-history-detail.component.html',
})
export class LLINSVillageReportHistoryDetailComponent implements OnInit {
  lLINSVillageReportHistory: ILLINSVillageReportHistory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lLINSVillageReportHistory }) => {
      this.lLINSVillageReportHistory = lLINSVillageReportHistory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
