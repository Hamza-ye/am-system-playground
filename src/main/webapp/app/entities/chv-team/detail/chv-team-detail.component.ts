import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChvTeam } from '../chv-team.model';

@Component({
  selector: 'app-chv-team-detail',
  templateUrl: './chv-team-detail.component.html',
})
export class ChvTeamDetailComponent implements OnInit {
  chvTeam: IChvTeam | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chvTeam }) => {
      this.chvTeam = chvTeam;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
