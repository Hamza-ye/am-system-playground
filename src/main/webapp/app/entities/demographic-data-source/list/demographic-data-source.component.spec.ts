import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DemographicDataSourceService } from '../service/demographic-data-source.service';

import { DemographicDataSourceComponent } from './demographic-data-source.component';

describe('Component Tests', () => {
  describe('DemographicDataSource Management Component', () => {
    let comp: DemographicDataSourceComponent;
    let fixture: ComponentFixture<DemographicDataSourceComponent>;
    let service: DemographicDataSourceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DemographicDataSourceComponent],
      })
        .overrideTemplate(DemographicDataSourceComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DemographicDataSourceComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DemographicDataSourceService);

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
      expect(comp.demographicDataSources?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
