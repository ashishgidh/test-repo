import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../cluster-country.test-samples';

import { ClusterCountryFormService } from './cluster-country-form.service';

describe('ClusterCountry Form Service', () => {
  let service: ClusterCountryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClusterCountryFormService);
  });

  describe('Service methods', () => {
    describe('createClusterCountryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createClusterCountryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });

      it('passing IClusterCountry should create a new form with FormGroup', () => {
        const formGroup = service.createClusterCountryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });
    });

    describe('getClusterCountry', () => {
      it('should return NewClusterCountry for default ClusterCountry initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createClusterCountryFormGroup(sampleWithNewData);

        const clusterCountry = service.getClusterCountry(formGroup) as any;

        expect(clusterCountry).toMatchObject(sampleWithNewData);
      });

      it('should return NewClusterCountry for empty ClusterCountry initial value', () => {
        const formGroup = service.createClusterCountryFormGroup();

        const clusterCountry = service.getClusterCountry(formGroup) as any;

        expect(clusterCountry).toMatchObject({});
      });

      it('should return IClusterCountry', () => {
        const formGroup = service.createClusterCountryFormGroup(sampleWithRequiredData);

        const clusterCountry = service.getClusterCountry(formGroup) as any;

        expect(clusterCountry).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IClusterCountry should not enable id FormControl', () => {
        const formGroup = service.createClusterCountryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewClusterCountry should disable id FormControl', () => {
        const formGroup = service.createClusterCountryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
