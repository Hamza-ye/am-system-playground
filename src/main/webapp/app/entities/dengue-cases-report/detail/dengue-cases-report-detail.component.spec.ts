import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DengueCasesReportDetailComponent } from './dengue-cases-report-detail.component';

describe('Component Tests', () => {
  describe('DengueCasesReport Management Detail Component', () => {
    let comp: DengueCasesReportDetailComponent;
    let fixture: ComponentFixture<DengueCasesReportDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DengueCasesReportDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ dengueCasesReport: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DengueCasesReportDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DengueCasesReportDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load dengueCasesReport on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.dengueCasesReport).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
