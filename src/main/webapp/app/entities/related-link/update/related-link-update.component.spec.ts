jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RelatedLinkService } from '../service/related-link.service';
import { IRelatedLink, RelatedLink } from '../related-link.model';

import { RelatedLinkUpdateComponent } from './related-link-update.component';

describe('Component Tests', () => {
  describe('RelatedLink Management Update Component', () => {
    let comp: RelatedLinkUpdateComponent;
    let fixture: ComponentFixture<RelatedLinkUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let relatedLinkService: RelatedLinkService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RelatedLinkUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RelatedLinkUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RelatedLinkUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      relatedLinkService = TestBed.inject(RelatedLinkService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const relatedLink: IRelatedLink = { id: 456 };

        activatedRoute.data = of({ relatedLink });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(relatedLink));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<RelatedLink>>();
        const relatedLink = { id: 123 };
        jest.spyOn(relatedLinkService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ relatedLink });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: relatedLink }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(relatedLinkService.update).toHaveBeenCalledWith(relatedLink);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<RelatedLink>>();
        const relatedLink = new RelatedLink();
        jest.spyOn(relatedLinkService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ relatedLink });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: relatedLink }));
        saveSubject.complete();

        // THEN
        expect(relatedLinkService.create).toHaveBeenCalledWith(relatedLink);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<RelatedLink>>();
        const relatedLink = { id: 123 };
        jest.spyOn(relatedLinkService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ relatedLink });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(relatedLinkService.update).toHaveBeenCalledWith(relatedLink);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
