import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MalariaCasesReportDetailComponent } from './malaria-cases-report-detail.component';

describe('Component Tests', () => {
  describe('MalariaCasesReport Management Detail Component', () => {
    let comp: MalariaCasesReportDetailComponent;
    let fixture: ComponentFixture<MalariaCasesReportDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [MalariaCasesReportDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ malariaCasesReport: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(MalariaCasesReportDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MalariaCasesReportDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load malariaCasesReport on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.malariaCasesReport).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
