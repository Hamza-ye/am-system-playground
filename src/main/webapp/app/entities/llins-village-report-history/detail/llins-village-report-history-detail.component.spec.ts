import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LLINSVillageReportHistoryDetailComponent } from './llins-village-report-history-detail.component';

describe('Component Tests', () => {
  describe('LLINSVillageReportHistory Management Detail Component', () => {
    let comp: LLINSVillageReportHistoryDetailComponent;
    let fixture: ComponentFixture<LLINSVillageReportHistoryDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LLINSVillageReportHistoryDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ lLINSVillageReportHistory: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LLINSVillageReportHistoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LLINSVillageReportHistoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load lLINSVillageReportHistory on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.lLINSVillageReportHistory).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
