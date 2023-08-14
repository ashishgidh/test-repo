import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ClusterFormService } from './cluster-form.service';
import { ClusterService } from '../service/cluster.service';
import { ICluster } from '../cluster.model';

import { ClusterUpdateComponent } from './cluster-update.component';

describe('Cluster Management Update Component', () => {
  let comp: ClusterUpdateComponent;
  let fixture: ComponentFixture<ClusterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let clusterFormService: ClusterFormService;
  let clusterService: ClusterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ClusterUpdateComponent],
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
      .overrideTemplate(ClusterUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClusterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    clusterFormService = TestBed.inject(ClusterFormService);
    clusterService = TestBed.inject(ClusterService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cluster: ICluster = { id: 456 };

      activatedRoute.data = of({ cluster });
      comp.ngOnInit();

      expect(comp.cluster).toEqual(cluster);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICluster>>();
      const cluster = { id: 123 };
      jest.spyOn(clusterFormService, 'getCluster').mockReturnValue(cluster);
      jest.spyOn(clusterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cluster });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cluster }));
      saveSubject.complete();

      // THEN
      expect(clusterFormService.getCluster).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(clusterService.update).toHaveBeenCalledWith(expect.objectContaining(cluster));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICluster>>();
      const cluster = { id: 123 };
      jest.spyOn(clusterFormService, 'getCluster').mockReturnValue({ id: null });
      jest.spyOn(clusterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cluster: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cluster }));
      saveSubject.complete();

      // THEN
      expect(clusterFormService.getCluster).toHaveBeenCalled();
      expect(clusterService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICluster>>();
      const cluster = { id: 123 };
      jest.spyOn(clusterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cluster });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(clusterService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
