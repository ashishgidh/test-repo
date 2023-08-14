import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LOBFormService } from './lob-form.service';
import { LOBService } from '../service/lob.service';
import { ILOB } from '../lob.model';

import { LOBUpdateComponent } from './lob-update.component';

describe('LOB Management Update Component', () => {
  let comp: LOBUpdateComponent;
  let fixture: ComponentFixture<LOBUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let lOBFormService: LOBFormService;
  let lOBService: LOBService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), LOBUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(LOBUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LOBUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    lOBFormService = TestBed.inject(LOBFormService);
    lOBService = TestBed.inject(LOBService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const lOB: ILOB = { id: 456 };

      activatedRoute.data = of({ lOB });
      comp.ngOnInit();

      expect(comp.lOB).toEqual(lOB);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILOB>>();
      const lOB = { id: 123 };
      jest.spyOn(lOBFormService, 'getLOB').mockReturnValue(lOB);
      jest.spyOn(lOBService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lOB });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: lOB }));
      saveSubject.complete();

      // THEN
      expect(lOBFormService.getLOB).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(lOBService.update).toHaveBeenCalledWith(expect.objectContaining(lOB));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILOB>>();
      const lOB = { id: 123 };
      jest.spyOn(lOBFormService, 'getLOB').mockReturnValue({ id: null });
      jest.spyOn(lOBService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lOB: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: lOB }));
      saveSubject.complete();

      // THEN
      expect(lOBFormService.getLOB).toHaveBeenCalled();
      expect(lOBService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILOB>>();
      const lOB = { id: 123 };
      jest.spyOn(lOBService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lOB });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(lOBService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
