import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChvMalariaReportVersion1DetailComponent } from './chv-malaria-report-version-1-detail.component';

describe('Component Tests', () => {
  describe('ChvMalariaReportVersion1 Management Detail Component', () => {
    let comp: ChvMalariaReportVersion1DetailComponent;
    let fixture: ComponentFixture<ChvMalariaReportVersion1DetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ChvMalariaReportVersion1DetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ chvMalariaReportVersion1: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ChvMalariaReportVersion1DetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChvMalariaReportVersion1DetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load chvMalariaReportVersion1 on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.chvMalariaReportVersion1).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
