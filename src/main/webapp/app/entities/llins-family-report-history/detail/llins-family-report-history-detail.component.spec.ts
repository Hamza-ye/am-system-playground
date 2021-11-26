import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LlinsFamilyReportHistoryDetailComponent } from './llins-family-report-history-detail.component';

describe('Component Tests', () => {
  describe('LlinsFamilyReportHistory Management Detail Component', () => {
    let comp: LlinsFamilyReportHistoryDetailComponent;
    let fixture: ComponentFixture<LlinsFamilyReportHistoryDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LlinsFamilyReportHistoryDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ llinsFamilyReportHistory: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LlinsFamilyReportHistoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LlinsFamilyReportHistoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load llinsFamilyReportHistory on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.llinsFamilyReportHistory).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
