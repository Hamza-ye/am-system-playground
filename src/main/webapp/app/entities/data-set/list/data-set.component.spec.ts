import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DataSetService } from '../service/data-set.service';

import { DataSetComponent } from './data-set.component';

describe('Component Tests', () => {
  describe('DataSet Management Component', () => {
    let comp: DataSetComponent;
    let fixture: ComponentFixture<DataSetComponent>;
    let service: DataSetService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DataSetComponent],
      })
        .overrideTemplate(DataSetComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DataSetComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DataSetService);

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
      expect(comp.dataSets?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
