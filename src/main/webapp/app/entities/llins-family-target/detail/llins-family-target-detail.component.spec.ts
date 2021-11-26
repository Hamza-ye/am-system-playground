import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LlinsFamilyTargetDetailComponent } from './llins-family-target-detail.component';

describe('Component Tests', () => {
  describe('LlinsFamilyTarget Management Detail Component', () => {
    let comp: LlinsFamilyTargetDetailComponent;
    let fixture: ComponentFixture<LlinsFamilyTargetDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LlinsFamilyTargetDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ llinsFamilyTarget: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LlinsFamilyTargetDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LlinsFamilyTargetDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load llinsFamilyTarget on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.llinsFamilyTarget).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
