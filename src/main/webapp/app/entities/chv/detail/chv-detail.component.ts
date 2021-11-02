import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICHV } from '../chv.model';

@Component({
  selector: 'app-chv-detail',
  templateUrl: './chv-detail.component.html',
})
export class CHVDetailComponent implements OnInit {
  cHV: ICHV | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cHV }) => {
      this.cHV = cHV;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
