import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CasesReportClassDetailComponent } from './cases-report-class-detail.component';

describe('Component Tests', () => {
  describe('CasesReportClass Management Detail Component', () => {
    let comp: CasesReportClassDetailComponent;
    let fixture: ComponentFixture<CasesReportClassDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CasesReportClassDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ casesReportClass: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CasesReportClassDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CasesReportClassDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load casesReportClass on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.casesReportClass).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
