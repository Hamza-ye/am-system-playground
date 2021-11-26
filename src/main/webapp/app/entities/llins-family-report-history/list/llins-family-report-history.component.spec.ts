import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { LlinsFamilyReportHistoryService } from '../service/llins-family-report-history.service';

import { LlinsFamilyReportHistoryComponent } from './llins-family-report-history.component';

describe('Component Tests', () => {
  describe('LlinsFamilyReportHistory Management Component', () => {
    let comp: LlinsFamilyReportHistoryComponent;
    let fixture: ComponentFixture<LlinsFamilyReportHistoryComponent>;
    let service: LlinsFamilyReportHistoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LlinsFamilyReportHistoryComponent],
      })
        .overrideTemplate(LlinsFamilyReportHistoryComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LlinsFamilyReportHistoryComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(LlinsFamilyReportHistoryService);

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
      expect(comp.llinsFamilyReportHistories?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
