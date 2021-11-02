import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { LLINSVillageReportService } from '../service/llins-village-report.service';

import { LLINSVillageReportComponent } from './llins-village-report.component';

describe('Component Tests', () => {
  describe('LLINSVillageReport Management Component', () => {
    let comp: LLINSVillageReportComponent;
    let fixture: ComponentFixture<LLINSVillageReportComponent>;
    let service: LLINSVillageReportService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LLINSVillageReportComponent],
      })
        .overrideTemplate(LLINSVillageReportComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LLINSVillageReportComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(LLINSVillageReportService);

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
      expect(comp.lLINSVillageReports?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
