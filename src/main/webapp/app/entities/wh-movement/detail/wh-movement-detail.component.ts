import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWhMovement } from '../wh-movement.model';

@Component({
  selector: 'app-wh-movement-detail',
  templateUrl: './wh-movement-detail.component.html',
})
export class WhMovementDetailComponent implements OnInit {
  whMovement: IWhMovement | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ whMovement }) => {
      this.whMovement = whMovement;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
