import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LLINSVillageTargetDetailComponent } from './llins-village-target-detail.component';

describe('Component Tests', () => {
  describe('LlinsVillageTarget Management Detail Component', () => {
    let comp: LLINSVillageTargetDetailComponent;
    let fixture: ComponentFixture<LLINSVillageTargetDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LLINSVillageTargetDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ lLINSVillageTarget: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LLINSVillageTargetDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LLINSVillageTargetDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load lLINSVillageTarget on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.lLINSVillageTarget).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
