import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { OrganisationUnitLevelService } from '../service/organisation-unit-level.service';

import { OrganisationUnitLevelComponent } from './organisation-unit-level.component';

describe('Component Tests', () => {
  describe('OrganisationUnitLevel Management Component', () => {
    let comp: OrganisationUnitLevelComponent;
    let fixture: ComponentFixture<OrganisationUnitLevelComponent>;
    let service: OrganisationUnitLevelService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OrganisationUnitLevelComponent],
      })
        .overrideTemplate(OrganisationUnitLevelComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrganisationUnitLevelComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(OrganisationUnitLevelService);

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
      expect(comp.organisationUnitLevels?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
