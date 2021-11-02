import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DengueCasesReportService } from '../service/dengue-cases-report.service';

import { DengueCasesReportComponent } from './dengue-cases-report.component';

describe('Component Tests', () => {
  describe('DengueCasesReport Management Component', () => {
    let comp: DengueCasesReportComponent;
    let fixture: ComponentFixture<DengueCasesReportComponent>;
    let service: DengueCasesReportService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DengueCasesReportComponent],
      })
        .overrideTemplate(DengueCasesReportComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DengueCasesReportComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DengueCasesReportService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.dengueCasesReports?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
