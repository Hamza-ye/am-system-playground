import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DataInputPeriodService } from '../service/data-input-period.service';

import { DataInputPeriodComponent } from './data-input-period.component';

describe('Component Tests', () => {
  describe('DataInputPeriod Management Component', () => {
    let comp: DataInputPeriodComponent;
    let fixture: ComponentFixture<DataInputPeriodComponent>;
    let service: DataInputPeriodService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DataInputPeriodComponent],
      })
        .overrideTemplate(DataInputPeriodComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DataInputPeriodComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DataInputPeriodService);

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
      expect(comp.dataInputPeriods?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
