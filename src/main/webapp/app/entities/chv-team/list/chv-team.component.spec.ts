import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CHVTeamService } from '../service/chv-team.service';

import { CHVTeamComponent } from './chv-team.component';

describe('Component Tests', () => {
  describe('CHVTeam Management Component', () => {
    let comp: CHVTeamComponent;
    let fixture: ComponentFixture<CHVTeamComponent>;
    let service: CHVTeamService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CHVTeamComponent],
      })
        .overrideTemplate(CHVTeamComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CHVTeamComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CHVTeamService);

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
      expect(comp.cHVTeams?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
