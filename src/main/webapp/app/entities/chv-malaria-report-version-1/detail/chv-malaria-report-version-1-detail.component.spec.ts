import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CHVMalariaReportVersion1DetailComponent } from './chv-malaria-report-version-1-detail.component';

describe('Component Tests', () => {
  describe('CHVMalariaReportVersion1 Management Detail Component', () => {
    let comp: CHVMalariaReportVersion1DetailComponent;
    let fixture: ComponentFixture<CHVMalariaReportVersion1DetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CHVMalariaReportVersion1DetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ cHVMalariaReportVersion1: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CHVMalariaReportVersion1DetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CHVMalariaReportVersion1DetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load cHVMalariaReportVersion1 on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cHVMalariaReportVersion1).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
