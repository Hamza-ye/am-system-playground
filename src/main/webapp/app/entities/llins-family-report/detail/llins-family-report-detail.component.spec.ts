import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LlinsFamilyReportDetailComponent } from './llins-family-report-detail.component';

describe('Component Tests', () => {
  describe('LlinsFamilyReport Management Detail Component', () => {
    let comp: LlinsFamilyReportDetailComponent;
    let fixture: ComponentFixture<LlinsFamilyReportDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LlinsFamilyReportDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ llinsFamilyReport: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LlinsFamilyReportDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LlinsFamilyReportDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load llinsFamilyReport on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.llinsFamilyReport).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
