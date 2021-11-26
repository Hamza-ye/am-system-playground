import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LlinsVillageReportHistoryDetailComponent } from './llins-village-report-history-detail.component';

describe('Component Tests', () => {
  describe('LlinsVillageReportHistory Management Detail Component', () => {
    let comp: LlinsVillageReportHistoryDetailComponent;
    let fixture: ComponentFixture<LlinsVillageReportHistoryDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LlinsVillageReportHistoryDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ llinsVillageReportHistory: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LlinsVillageReportHistoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LlinsVillageReportHistoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load llinsVillageReportHistory on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.llinsVillageReportHistory).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
