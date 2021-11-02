import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataSetDetailComponent } from './data-set-detail.component';

describe('Component Tests', () => {
  describe('DataSet Management Detail Component', () => {
    let comp: DataSetDetailComponent;
    let fixture: ComponentFixture<DataSetDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DataSetDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ dataSet: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DataSetDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DataSetDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load dataSet on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.dataSet).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
