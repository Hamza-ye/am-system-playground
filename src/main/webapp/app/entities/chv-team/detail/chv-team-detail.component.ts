import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICHVTeam } from '../chv-team.model';

@Component({
  selector: 'app-chv-team-detail',
  templateUrl: './chv-team-detail.component.html',
})
export class CHVTeamDetailComponent implements OnInit {
  cHVTeam: ICHVTeam | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cHVTeam }) => {
      this.cHVTeam = cHVTeam;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
