import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { OrganisationUnitGroupService } from '../service/organisation-unit-group.service';

import { OrganisationUnitGroupComponent } from './organisation-unit-group.component';

describe('Component Tests', () => {
  describe('OrganisationUnitGroup Management Component', () => {
    let comp: OrganisationUnitGroupComponent;
    let fixture: ComponentFixture<OrganisationUnitGroupComponent>;
    let service: OrganisationUnitGroupService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OrganisationUnitGroupComponent],
      })
        .overrideTemplate(OrganisationUnitGroupComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrganisationUnitGroupComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(OrganisationUnitGroupService);

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
      expect(comp.organisationUnitGroups?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
