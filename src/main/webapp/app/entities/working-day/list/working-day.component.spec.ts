import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { WorkingDayService } from '../service/working-day.service';

import { WorkingDayComponent } from './working-day.component';

describe('Component Tests', () => {
  describe('WorkingDay Management Component', () => {
    let comp: WorkingDayComponent;
    let fixture: ComponentFixture<WorkingDayComponent>;
    let service: WorkingDayService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [WorkingDayComponent],
      })
        .overrideTemplate(WorkingDayComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WorkingDayComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(WorkingDayService);

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
      expect(comp.workingDays?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
