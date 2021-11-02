import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDataSet } from '../data-set.model';

@Component({
  selector: 'app-data-set-detail',
  templateUrl: './data-set-detail.component.html',
})
export class DataSetDetailComponent implements OnInit {
  dataSet: IDataSet | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dataSet }) => {
      this.dataSet = dataSet;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
