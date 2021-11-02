import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CasesReportClassService } from '../service/cases-report-class.service';

import { CasesReportClassComponent } from './cases-report-class.component';

describe('Component Tests', () => {
  describe('CasesReportClass Management Component', () => {
    let comp: CasesReportClassComponent;
    let fixture: ComponentFixture<CasesReportClassComponent>;
    let service: CasesReportClassService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CasesReportClassComponent],
      })
        .overrideTemplate(CasesReportClassComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CasesReportClassComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CasesReportClassService);

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
      expect(comp.casesReportClasses?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
