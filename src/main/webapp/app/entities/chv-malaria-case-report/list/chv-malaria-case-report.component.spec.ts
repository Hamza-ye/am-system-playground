import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ChvMalariaCaseReportService } from '../service/chv-malaria-case-report.service';

import { ChvMalariaCaseReportComponent } from './chv-malaria-case-report.component';

describe('Component Tests', () => {
  describe('ChvMalariaCaseReport Management Component', () => {
    let comp: ChvMalariaCaseReportComponent;
    let fixture: ComponentFixture<ChvMalariaCaseReportComponent>;
    let service: ChvMalariaCaseReportService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ChvMalariaCaseReportComponent],
      })
        .overrideTemplate(ChvMalariaCaseReportComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChvMalariaCaseReportComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ChvMalariaCaseReportService);

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
      expect(comp.chvMalariaCaseReports?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
