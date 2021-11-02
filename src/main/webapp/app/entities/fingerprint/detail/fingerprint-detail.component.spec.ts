import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FingerprintDetailComponent } from './fingerprint-detail.component';

describe('Component Tests', () => {
  describe('Fingerprint Management Detail Component', () => {
    let comp: FingerprintDetailComponent;
    let fixture: ComponentFixture<FingerprintDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FingerprintDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ fingerprint: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FingerprintDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FingerprintDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load fingerprint on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.fingerprint).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
