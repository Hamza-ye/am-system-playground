import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILlinsFamilyTarget } from '../llins-family-target.model';

@Component({
  selector: 'app-llins-family-target-detail',
  templateUrl: './llins-family-target-detail.component.html',
})
export class LlinsFamilyTargetDetailComponent implements OnInit {
  llinsFamilyTarget: ILlinsFamilyTarget | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ llinsFamilyTarget }) => {
      this.llinsFamilyTarget = llinsFamilyTarget;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
