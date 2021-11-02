import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DemographicDataService } from '../service/demographic-data.service';

import { DemographicDataComponent } from './demographic-data.component';

describe('Component Tests', () => {
  describe('DemographicData Management Component', () => {
    let comp: DemographicDataComponent;
    let fixture: ComponentFixture<DemographicDataComponent>;
    let service: DemographicDataService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DemographicDataComponent],
      })
        .overrideTemplate(DemographicDataComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DemographicDataComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DemographicDataService);

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
      expect(comp.demographicData?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
