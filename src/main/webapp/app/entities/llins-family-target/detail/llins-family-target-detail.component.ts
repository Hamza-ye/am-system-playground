import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILLINSFamilyTarget } from '../llins-family-target.model';

@Component({
  selector: 'app-llins-family-target-detail',
  templateUrl: './llins-family-target-detail.component.html',
})
export class LLINSFamilyTargetDetailComponent implements OnInit {
  lLINSFamilyTarget: ILLINSFamilyTarget | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lLINSFamilyTarget }) => {
      this.lLINSFamilyTarget = lLINSFamilyTarget;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
