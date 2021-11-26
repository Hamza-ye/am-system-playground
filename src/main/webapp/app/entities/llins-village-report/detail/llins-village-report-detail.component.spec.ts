import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LlinsVillageReportDetailComponent } from './llins-village-report-detail.component';

describe('Component Tests', () => {
  describe('LlinsVillageReport Management Detail Component', () => {
    let comp: LlinsVillageReportDetailComponent;
    let fixture: ComponentFixture<LlinsVillageReportDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LlinsVillageReportDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ llinsVillageReport: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LlinsVillageReportDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LlinsVillageReportDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load llinsVillageReport on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.llinsVillageReport).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
