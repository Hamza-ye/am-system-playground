import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILLINSVillageTarget } from '../llins-village-target.model';

@Component({
  selector: 'app-llins-village-target-detail',
  templateUrl: './llins-village-target-detail.component.html',
})
export class LLINSVillageTargetDetailComponent implements OnInit {
  lLINSVillageTarget: ILLINSVillageTarget | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lLINSVillageTarget }) => {
      this.lLINSVillageTarget = lLINSVillageTarget;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
