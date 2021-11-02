import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMalariaUnit } from '../malaria-unit.model';

@Component({
  selector: 'app-malaria-unit-detail',
  templateUrl: './malaria-unit-detail.component.html',
})
export class MalariaUnitDetailComponent implements OnInit {
  malariaUnit: IMalariaUnit | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ malariaUnit }) => {
      this.malariaUnit = malariaUnit;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
