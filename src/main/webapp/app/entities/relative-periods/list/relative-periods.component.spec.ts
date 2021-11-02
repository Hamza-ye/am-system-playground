import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { RelativePeriodsService } from '../service/relative-periods.service';

import { RelativePeriodsComponent } from './relative-periods.component';

describe('Component Tests', () => {
  describe('RelativePeriods Management Component', () => {
    let comp: RelativePeriodsComponent;
    let fixture: ComponentFixture<RelativePeriodsComponent>;
    let service: RelativePeriodsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RelativePeriodsComponent],
      })
        .overrideTemplate(RelativePeriodsComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RelativePeriodsComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(RelativePeriodsService);

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
      expect(comp.relativePeriods?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
