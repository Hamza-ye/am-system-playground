import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFamilyHead } from '../family-head.model';

@Component({
  selector: 'app-family-head-detail',
  templateUrl: './family-head-detail.component.html',
})
export class FamilyHeadDetailComponent implements OnInit {
  familyHead: IFamilyHead | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ familyHead }) => {
      this.familyHead = familyHead;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
