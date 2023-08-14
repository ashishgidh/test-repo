import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ClusterCountryFormService } from './cluster-country-form.service';
import { ClusterCountryService } from '../service/cluster-country.service';
import { IClusterCountry } from '../cluster-country.model';

import { ClusterCountryUpdateComponent } from './cluster-country-update.component';

describe('ClusterCountry Management Update Component', () => {
  let comp: ClusterCountryUpdateComponent;
  let fixture: ComponentFixture<ClusterCountryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let clusterCountryFormService: ClusterCountryFormService;
  let clusterCountryService: ClusterCountryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ClusterCountryUpdateComponent],
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
      .overrideTemplate(ClusterCountryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClusterCountryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    clusterCountryFormService = TestBed.inject(ClusterCountryFormService);
    clusterCountryService = TestBed.inject(ClusterCountryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const clusterCountry: IClusterCountry = { id: 456 };

      activatedRoute.data = of({ clusterCountry });
      comp.ngOnInit();

      expect(comp.clusterCountry).toEqual(clusterCountry);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClusterCountry>>();
      const clusterCountry = { id: 123 };
      jest.spyOn(clusterCountryFormService, 'getClusterCountry').mockReturnValue(clusterCountry);
      jest.spyOn(clusterCountryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ clusterCountry });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: clusterCountry }));
      saveSubject.complete();

      // THEN
      expect(clusterCountryFormService.getClusterCountry).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(clusterCountryService.update).toHaveBeenCalledWith(expect.objectContaining(clusterCountry));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClusterCountry>>();
      const clusterCountry = { id: 123 };
      jest.spyOn(clusterCountryFormService, 'getClusterCountry').mockReturnValue({ id: null });
      jest.spyOn(clusterCountryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ clusterCountry: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: clusterCountry }));
      saveSubject.complete();

      // THEN
      expect(clusterCountryFormService.getClusterCountry).toHaveBeenCalled();
      expect(clusterCountryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClusterCountry>>();
      const clusterCountry = { id: 123 };
      jest.spyOn(clusterCountryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ clusterCountry });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(clusterCountryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
