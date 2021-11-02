import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MalariaUnitService } from '../service/malaria-unit.service';

import { MalariaUnitComponent } from './malaria-unit.component';

describe('Component Tests', () => {
  describe('MalariaUnit Management Component', () => {
    let comp: MalariaUnitComponent;
    let fixture: ComponentFixture<MalariaUnitComponent>;
    let service: MalariaUnitService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MalariaUnitComponent],
      })
        .overrideTemplate(MalariaUnitComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MalariaUnitComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(MalariaUnitService);

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
      expect(comp.malariaUnits?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
