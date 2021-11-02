import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPersonAuthorityGroup } from '../person-authority-group.model';

@Component({
  selector: 'app-person-authority-group-detail',
  templateUrl: './person-authority-group-detail.component.html',
})
export class PersonAuthorityGroupDetailComponent implements OnInit {
  personAuthorityGroup: IPersonAuthorityGroup | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personAuthorityGroup }) => {
      this.personAuthorityGroup = personAuthorityGroup;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
