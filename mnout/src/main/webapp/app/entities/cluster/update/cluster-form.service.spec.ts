import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../cluster.test-samples';

import { ClusterFormService } from './cluster-form.service';

describe('Cluster Form Service', () => {
  let service: ClusterFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClusterFormService);
  });

  describe('Service methods', () => {
    describe('createClusterFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createClusterFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });

      it('passing ICluster should create a new form with FormGroup', () => {
        const formGroup = service.createClusterFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });
    });

    describe('getCluster', () => {
      it('should return NewCluster for default Cluster initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createClusterFormGroup(sampleWithNewData);

        const cluster = service.getCluster(formGroup) as any;

        expect(cluster).toMatchObject(sampleWithNewData);
      });

      it('should return NewCluster for empty Cluster initial value', () => {
        const formGroup = service.createClusterFormGroup();

        const cluster = service.getCluster(formGroup) as any;

        expect(cluster).toMatchObject({});
      });

      it('should return ICluster', () => {
        const formGroup = service.createClusterFormGroup(sampleWithRequiredData);

        const cluster = service.getCluster(formGroup) as any;

        expect(cluster).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICluster should not enable id FormControl', () => {
        const formGroup = service.createClusterFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCluster should disable id FormControl', () => {
        const formGroup = service.createClusterFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
