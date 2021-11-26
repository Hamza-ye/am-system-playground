import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { LlinsVillageReportHistoryService } from '../service/llins-village-report-history.service';

import { LlinsVillageReportHistoryComponent } from './llins-village-report-history.component';

describe('Component Tests', () => {
  describe('LlinsVillageReportHistory Management Component', () => {
    let comp: LlinsVillageReportHistoryComponent;
    let fixture: ComponentFixture<LlinsVillageReportHistoryComponent>;
    let service: LlinsVillageReportHistoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LlinsVillageReportHistoryComponent],
      })
        .overrideTemplate(LlinsVillageReportHistoryComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LlinsVillageReportHistoryComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(LlinsVillageReportHistoryService);

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
      expect(comp.llinsVillageReportHistories?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
