import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LLINSFamilyTargetDetailComponent } from './llins-family-target-detail.component';

describe('Component Tests', () => {
  describe('LLINSFamilyTarget Management Detail Component', () => {
    let comp: LLINSFamilyTargetDetailComponent;
    let fixture: ComponentFixture<LLINSFamilyTargetDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LLINSFamilyTargetDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ lLINSFamilyTarget: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LLINSFamilyTargetDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LLINSFamilyTargetDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load lLINSFamilyTarget on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.lLINSFamilyTarget).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
