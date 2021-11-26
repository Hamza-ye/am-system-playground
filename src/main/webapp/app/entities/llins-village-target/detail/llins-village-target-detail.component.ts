import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILlinsVillageTarget } from '../llins-village-target.model';

@Component({
  selector: 'app-llins-village-target-detail',
  templateUrl: './llins-village-target-detail.component.html',
})
export class LlinsVillageTargetDetailComponent implements OnInit {
  llinsVillageTarget: ILlinsVillageTarget | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ llinsVillageTarget }) => {
      this.llinsVillageTarget = llinsVillageTarget;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
