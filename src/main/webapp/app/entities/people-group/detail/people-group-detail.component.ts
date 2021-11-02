import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPeopleGroup } from '../people-group.model';

@Component({
  selector: 'app-people-group-detail',
  templateUrl: './people-group-detail.component.html',
})
export class PeopleGroupDetailComponent implements OnInit {
  peopleGroup: IPeopleGroup | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ peopleGroup }) => {
      this.peopleGroup = peopleGroup;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
