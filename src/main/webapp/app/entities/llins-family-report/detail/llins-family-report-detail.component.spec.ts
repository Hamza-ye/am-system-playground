import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LLINSFamilyReportDetailComponent } from './llins-family-report-detail.component';

describe('Component Tests', () => {
  describe('LLINSFamilyReport Management Detail Component', () => {
    let comp: LLINSFamilyReportDetailComponent;
    let fixture: ComponentFixture<LLINSFamilyReportDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LLINSFamilyReportDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ lLINSFamilyReport: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LLINSFamilyReportDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LLINSFamilyReportDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load lLINSFamilyReport on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.lLINSFamilyReport).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
