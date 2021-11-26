import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { LlinsVillageReportService } from '../service/llins-village-report.service';

import { LlinsVillageReportComponent } from './llins-village-report.component';

describe('Component Tests', () => {
  describe('LlinsVillageReport Management Component', () => {
    let comp: LlinsVillageReportComponent;
    let fixture: ComponentFixture<LlinsVillageReportComponent>;
    let service: LlinsVillageReportService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LlinsVillageReportComponent],
      })
        .overrideTemplate(LlinsVillageReportComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LlinsVillageReportComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(LlinsVillageReportService);

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
      expect(comp.llinsVillageReports?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
