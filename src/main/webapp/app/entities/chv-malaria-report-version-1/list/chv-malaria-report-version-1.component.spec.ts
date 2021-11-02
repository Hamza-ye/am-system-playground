import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CHVMalariaReportVersion1Service } from '../service/chv-malaria-report-version-1.service';

import { CHVMalariaReportVersion1Component } from './chv-malaria-report-version-1.component';

describe('Component Tests', () => {
  describe('CHVMalariaReportVersion1 Management Component', () => {
    let comp: CHVMalariaReportVersion1Component;
    let fixture: ComponentFixture<CHVMalariaReportVersion1Component>;
    let service: CHVMalariaReportVersion1Service;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CHVMalariaReportVersion1Component],
      })
        .overrideTemplate(CHVMalariaReportVersion1Component, '')
        .compileComponents();

      fixture = TestBed.createComponent(CHVMalariaReportVersion1Component);
      comp = fixture.componentInstance;
      service = TestBed.inject(CHVMalariaReportVersion1Service);

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
      expect(comp.cHVMalariaReportVersion1s?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
