import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PeriodTypeService } from '../service/period-type.service';

import { PeriodTypeComponent } from './period-type.component';

describe('Component Tests', () => {
  describe('PeriodType Management Component', () => {
    let comp: PeriodTypeComponent;
    let fixture: ComponentFixture<PeriodTypeComponent>;
    let service: PeriodTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PeriodTypeComponent],
      })
        .overrideTemplate(PeriodTypeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PeriodTypeComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PeriodTypeService);

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
      expect(comp.periodTypes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
