import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDataProvider } from '../data-provider.model';

@Component({
  selector: 'app-data-provider-detail',
  templateUrl: './data-provider-detail.component.html',
})
export class DataProviderDetailComponent implements OnInit {
  dataProvider: IDataProvider | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dataProvider }) => {
      this.dataProvider = dataProvider;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
