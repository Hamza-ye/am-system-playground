import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LLINSVillageReportDetailComponent } from './llins-village-report-detail.component';

describe('Component Tests', () => {
  describe('LLINSVillageReport Management Detail Component', () => {
    let comp: LLINSVillageReportDetailComponent;
    let fixture: ComponentFixture<LLINSVillageReportDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LLINSVillageReportDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ lLINSVillageReport: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LLINSVillageReportDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LLINSVillageReportDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load lLINSVillageReport on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.lLINSVillageReport).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
