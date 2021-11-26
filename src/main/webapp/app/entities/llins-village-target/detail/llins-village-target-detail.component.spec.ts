import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LlinsVillageTargetDetailComponent } from './llins-village-target-detail.component';

describe('Component Tests', () => {
  describe('LlinsVillageTarget Management Detail Component', () => {
    let comp: LlinsVillageTargetDetailComponent;
    let fixture: ComponentFixture<LlinsVillageTargetDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LlinsVillageTargetDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ llinsVillageTarget: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LlinsVillageTargetDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LlinsVillageTargetDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load llinsVillageTarget on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.llinsVillageTarget).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
