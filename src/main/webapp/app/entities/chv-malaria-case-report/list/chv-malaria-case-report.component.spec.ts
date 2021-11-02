import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CHVMalariaCaseReportService } from '../service/chv-malaria-case-report.service';

import { CHVMalariaCaseReportComponent } from './chv-malaria-case-report.component';

describe('Component Tests', () => {
  describe('CHVMalariaCaseReport Management Component', () => {
    let comp: CHVMalariaCaseReportComponent;
    let fixture: ComponentFixture<CHVMalariaCaseReportComponent>;
    let service: CHVMalariaCaseReportService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CHVMalariaCaseReportComponent],
      })
        .overrideTemplate(CHVMalariaCaseReportComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CHVMalariaCaseReportComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CHVMalariaCaseReportService);

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
      expect(comp.cHVMalariaCaseReports?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
