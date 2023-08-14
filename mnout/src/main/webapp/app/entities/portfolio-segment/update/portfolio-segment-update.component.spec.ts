import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PortfolioSegmentFormService } from './portfolio-segment-form.service';
import { PortfolioSegmentService } from '../service/portfolio-segment.service';
import { IPortfolioSegment } from '../portfolio-segment.model';

import { PortfolioSegmentUpdateComponent } from './portfolio-segment-update.component';

describe('PortfolioSegment Management Update Component', () => {
  let comp: PortfolioSegmentUpdateComponent;
  let fixture: ComponentFixture<PortfolioSegmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let portfolioSegmentFormService: PortfolioSegmentFormService;
  let portfolioSegmentService: PortfolioSegmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), PortfolioSegmentUpdateComponent],
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
      .overrideTemplate(PortfolioSegmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PortfolioSegmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    portfolioSegmentFormService = TestBed.inject(PortfolioSegmentFormService);
    portfolioSegmentService = TestBed.inject(PortfolioSegmentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const portfolioSegment: IPortfolioSegment = { id: 'CBA' };

      activatedRoute.data = of({ portfolioSegment });
      comp.ngOnInit();

      expect(comp.portfolioSegment).toEqual(portfolioSegment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPortfolioSegment>>();
      const portfolioSegment = { id: 'ABC' };
      jest.spyOn(portfolioSegmentFormService, 'getPortfolioSegment').mockReturnValue(portfolioSegment);
      jest.spyOn(portfolioSegmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ portfolioSegment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: portfolioSegment }));
      saveSubject.complete();

      // THEN
      expect(portfolioSegmentFormService.getPortfolioSegment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(portfolioSegmentService.update).toHaveBeenCalledWith(expect.objectContaining(portfolioSegment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPortfolioSegment>>();
      const portfolioSegment = { id: 'ABC' };
      jest.spyOn(portfolioSegmentFormService, 'getPortfolioSegment').mockReturnValue({ id: null });
      jest.spyOn(portfolioSegmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ portfolioSegment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: portfolioSegment }));
      saveSubject.complete();

      // THEN
      expect(portfolioSegmentFormService.getPortfolioSegment).toHaveBeenCalled();
      expect(portfolioSegmentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPortfolioSegment>>();
      const portfolioSegment = { id: 'ABC' };
      jest.spyOn(portfolioSegmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ portfolioSegment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(portfolioSegmentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
