import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LLINSFamilyReportHistoryDetailComponent } from './llins-family-report-history-detail.component';

describe('Component Tests', () => {
  describe('LLINSFamilyReportHistory Management Detail Component', () => {
    let comp: LLINSFamilyReportHistoryDetailComponent;
    let fixture: ComponentFixture<LLINSFamilyReportHistoryDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LLINSFamilyReportHistoryDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ lLINSFamilyReportHistory: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LLINSFamilyReportHistoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LLINSFamilyReportHistoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load lLINSFamilyReportHistory on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.lLINSFamilyReportHistory).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
