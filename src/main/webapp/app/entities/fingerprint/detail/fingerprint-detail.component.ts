import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFingerprint } from '../fingerprint.model';

@Component({
  selector: 'app-fingerprint-detail',
  templateUrl: './fingerprint-detail.component.html',
})
export class FingerprintDetailComponent implements OnInit {
  fingerprint: IFingerprint | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fingerprint }) => {
      this.fingerprint = fingerprint;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
