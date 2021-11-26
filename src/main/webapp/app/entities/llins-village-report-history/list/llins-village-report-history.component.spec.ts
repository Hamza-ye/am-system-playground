import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { LLINSVillageReportHistoryService } from '../service/llins-village-report-history.service';

import { LLINSVillageReportHistoryComponent } from './llins-village-report-history.component';

describe('Component Tests', () => {
  describe('LlinsVillageReportHistory Management Component', () => {
    let comp: LLINSVillageReportHistoryComponent;
    let fixture: ComponentFixture<LLINSVillageReportHistoryComponent>;
    let service: LLINSVillageReportHistoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LLINSVillageReportHistoryComponent],
      })
        .overrideTemplate(LLINSVillageReportHistoryComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LLINSVillageReportHistoryComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(LLINSVillageReportHistoryService);

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
      expect(comp.lLINSVillageReportHistories?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
