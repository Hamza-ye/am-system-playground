import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWHMovement } from '../wh-movement.model';

@Component({
  selector: 'app-wh-movement-detail',
  templateUrl: './wh-movement-detail.component.html',
})
export class WHMovementDetailComponent implements OnInit {
  wHMovement: IWHMovement | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wHMovement }) => {
      this.wHMovement = wHMovement;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
