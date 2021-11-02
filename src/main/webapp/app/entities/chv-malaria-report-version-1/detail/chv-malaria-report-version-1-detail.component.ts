import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICHVMalariaReportVersion1 } from '../chv-malaria-report-version-1.model';

@Component({
  selector: 'app-chv-malaria-report-version-1-detail',
  templateUrl: './chv-malaria-report-version-1-detail.component.html',
})
export class CHVMalariaReportVersion1DetailComponent implements OnInit {
  cHVMalariaReportVersion1: ICHVMalariaReportVersion1 | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cHVMalariaReportVersion1 }) => {
      this.cHVMalariaReportVersion1 = cHVMalariaReportVersion1;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
