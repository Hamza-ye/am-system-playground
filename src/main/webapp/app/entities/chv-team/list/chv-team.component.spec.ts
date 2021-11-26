import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ChvTeamService } from '../service/chv-team.service';

import { ChvTeamComponent } from './chv-team.component';

describe('Component Tests', () => {
  describe('ChvTeam Management Component', () => {
    let comp: ChvTeamComponent;
    let fixture: ComponentFixture<ChvTeamComponent>;
    let service: ChvTeamService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ChvTeamComponent],
      })
        .overrideTemplate(ChvTeamComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChvTeamComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ChvTeamService);

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
      expect(comp.chvTeams?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
