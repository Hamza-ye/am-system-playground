import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DataProviderService } from '../service/data-provider.service';

import { DataProviderComponent } from './data-provider.component';

describe('Component Tests', () => {
  describe('DataProvider Management Component', () => {
    let comp: DataProviderComponent;
    let fixture: ComponentFixture<DataProviderComponent>;
    let service: DataProviderService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DataProviderComponent],
      })
        .overrideTemplate(DataProviderComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DataProviderComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DataProviderService);

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
      expect(comp.dataProviders?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
