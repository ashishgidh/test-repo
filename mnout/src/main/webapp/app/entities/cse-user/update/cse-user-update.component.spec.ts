import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CSEUserFormService } from './cse-user-form.service';
import { CSEUserService } from '../service/cse-user.service';
import { ICSEUser } from '../cse-user.model';

import { CSEUserUpdateComponent } from './cse-user-update.component';

describe('CSEUser Management Update Component', () => {
  let comp: CSEUserUpdateComponent;
  let fixture: ComponentFixture<CSEUserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cSEUserFormService: CSEUserFormService;
  let cSEUserService: CSEUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CSEUserUpdateComponent],
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
      .overrideTemplate(CSEUserUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CSEUserUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cSEUserFormService = TestBed.inject(CSEUserFormService);
    cSEUserService = TestBed.inject(CSEUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cSEUser: ICSEUser = { id: 456 };

      activatedRoute.data = of({ cSEUser });
      comp.ngOnInit();

      expect(comp.cSEUser).toEqual(cSEUser);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICSEUser>>();
      const cSEUser = { id: 123 };
      jest.spyOn(cSEUserFormService, 'getCSEUser').mockReturnValue(cSEUser);
      jest.spyOn(cSEUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cSEUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cSEUser }));
      saveSubject.complete();

      // THEN
      expect(cSEUserFormService.getCSEUser).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cSEUserService.update).toHaveBeenCalledWith(expect.objectContaining(cSEUser));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICSEUser>>();
      const cSEUser = { id: 123 };
      jest.spyOn(cSEUserFormService, 'getCSEUser').mockReturnValue({ id: null });
      jest.spyOn(cSEUserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cSEUser: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cSEUser }));
      saveSubject.complete();

      // THEN
      expect(cSEUserFormService.getCSEUser).toHaveBeenCalled();
      expect(cSEUserService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICSEUser>>();
      const cSEUser = { id: 123 };
      jest.spyOn(cSEUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cSEUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cSEUserService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
