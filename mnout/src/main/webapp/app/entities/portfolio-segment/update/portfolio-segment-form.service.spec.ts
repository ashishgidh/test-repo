import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../portfolio-segment.test-samples';

import { PortfolioSegmentFormService } from './portfolio-segment-form.service';

describe('PortfolioSegment Form Service', () => {
  let service: PortfolioSegmentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PortfolioSegmentFormService);
  });

  describe('Service methods', () => {
    describe('createPortfolioSegmentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPortfolioSegmentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            portfolioSigment: expect.any(Object),
            name: expect.any(Object),
            segment: expect.any(Object),
          })
        );
      });

      it('passing IPortfolioSegment should create a new form with FormGroup', () => {
        const formGroup = service.createPortfolioSegmentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            portfolioSigment: expect.any(Object),
            name: expect.any(Object),
            segment: expect.any(Object),
          })
        );
      });
    });

    describe('getPortfolioSegment', () => {
      it('should return NewPortfolioSegment for default PortfolioSegment initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPortfolioSegmentFormGroup(sampleWithNewData);

        const portfolioSegment = service.getPortfolioSegment(formGroup) as any;

        expect(portfolioSegment).toMatchObject(sampleWithNewData);
      });

      it('should return NewPortfolioSegment for empty PortfolioSegment initial value', () => {
        const formGroup = service.createPortfolioSegmentFormGroup();

        const portfolioSegment = service.getPortfolioSegment(formGroup) as any;

        expect(portfolioSegment).toMatchObject({});
      });

      it('should return IPortfolioSegment', () => {
        const formGroup = service.createPortfolioSegmentFormGroup(sampleWithRequiredData);

        const portfolioSegment = service.getPortfolioSegment(formGroup) as any;

        expect(portfolioSegment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPortfolioSegment should not enable id FormControl', () => {
        const formGroup = service.createPortfolioSegmentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPortfolioSegment should disable id FormControl', () => {
        const formGroup = service.createPortfolioSegmentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
