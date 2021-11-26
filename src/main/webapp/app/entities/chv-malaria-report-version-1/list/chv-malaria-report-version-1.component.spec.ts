import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ChvMalariaReportVersion1Service } from '../service/chv-malaria-report-version-1.service';

import { ChvMalariaReportVersion1Component } from './chv-malaria-report-version-1.component';

describe('Component Tests', () => {
  describe('ChvMalariaReportVersion1 Management Component', () => {
    let comp: ChvMalariaReportVersion1Component;
    let fixture: ComponentFixture<ChvMalariaReportVersion1Component>;
    let service: ChvMalariaReportVersion1Service;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ChvMalariaReportVersion1Component],
      })
        .overrideTemplate(ChvMalariaReportVersion1Component, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChvMalariaReportVersion1Component);
      comp = fixture.componentInstance;
      service = TestBed.inject(ChvMalariaReportVersion1Service);

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
      expect(comp.chvMalariaReportVersion1s?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
