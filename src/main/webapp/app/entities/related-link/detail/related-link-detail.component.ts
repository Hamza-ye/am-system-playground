import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRelatedLink } from '../related-link.model';

@Component({
  selector: 'app-related-link-detail',
  templateUrl: './related-link-detail.component.html',
})
export class RelatedLinkDetailComponent implements OnInit {
  relatedLink: IRelatedLink | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ relatedLink }) => {
      this.relatedLink = relatedLink;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
