import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILlinsVillageReportHistory } from '../llins-village-report-history.model';

@Component({
  selector: 'app-llins-village-report-history-detail',
  templateUrl: './llins-village-report-history-detail.component.html',
})
export class LlinsVillageReportHistoryDetailComponent implements OnInit {
  llinsVillageReportHistory: ILlinsVillageReportHistory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ llinsVillageReportHistory }) => {
      this.llinsVillageReportHistory = llinsVillageReportHistory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
