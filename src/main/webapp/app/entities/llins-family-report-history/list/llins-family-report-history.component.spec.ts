import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { LLINSFamilyReportHistoryService } from '../service/llins-family-report-history.service';

import { LLINSFamilyReportHistoryComponent } from './llins-family-report-history.component';

describe('Component Tests', () => {
  describe('LlinsFamilyReportHistory Management Component', () => {
    let comp: LLINSFamilyReportHistoryComponent;
    let fixture: ComponentFixture<LLINSFamilyReportHistoryComponent>;
    let service: LLINSFamilyReportHistoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LLINSFamilyReportHistoryComponent],
      })
        .overrideTemplate(LLINSFamilyReportHistoryComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LLINSFamilyReportHistoryComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(LLINSFamilyReportHistoryService);

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
      expect(comp.lLINSFamilyReportHistories?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
