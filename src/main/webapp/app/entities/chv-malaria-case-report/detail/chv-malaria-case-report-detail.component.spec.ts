import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChvMalariaCaseReportDetailComponent } from './chv-malaria-case-report-detail.component';

describe('Component Tests', () => {
  describe('ChvMalariaCaseReport Management Detail Component', () => {
    let comp: ChvMalariaCaseReportDetailComponent;
    let fixture: ComponentFixture<ChvMalariaCaseReportDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ChvMalariaCaseReportDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ chvMalariaCaseReport: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ChvMalariaCaseReportDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChvMalariaCaseReportDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load chvMalariaCaseReport on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.chvMalariaCaseReport).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
