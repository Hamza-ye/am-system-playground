import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CHVMalariaCaseReportDetailComponent } from './chv-malaria-case-report-detail.component';

describe('Component Tests', () => {
  describe('CHVMalariaCaseReport Management Detail Component', () => {
    let comp: CHVMalariaCaseReportDetailComponent;
    let fixture: ComponentFixture<CHVMalariaCaseReportDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CHVMalariaCaseReportDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ cHVMalariaCaseReport: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CHVMalariaCaseReportDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CHVMalariaCaseReportDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load cHVMalariaCaseReport on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cHVMalariaCaseReport).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
