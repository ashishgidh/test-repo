import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../branch.test-samples';

import { BranchFormService } from './branch-form.service';

describe('Branch Form Service', () => {
  let service: BranchFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BranchFormService);
  });

  describe('Service methods', () => {
    describe('createBranchFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBranchFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            branchId: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });

      it('passing IBranch should create a new form with FormGroup', () => {
        const formGroup = service.createBranchFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            branchId: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });
    });

    describe('getBranch', () => {
      it('should return NewBranch for default Branch initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createBranchFormGroup(sampleWithNewData);

        const branch = service.getBranch(formGroup) as any;

        expect(branch).toMatchObject(sampleWithNewData);
      });

      it('should return NewBranch for empty Branch initial value', () => {
        const formGroup = service.createBranchFormGroup();

        const branch = service.getBranch(formGroup) as any;

        expect(branch).toMatchObject({});
      });

      it('should return IBranch', () => {
        const formGroup = service.createBranchFormGroup(sampleWithRequiredData);

        const branch = service.getBranch(formGroup) as any;

        expect(branch).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBranch should not enable id FormControl', () => {
        const formGroup = service.createBranchFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBranch should disable id FormControl', () => {
        const formGroup = service.createBranchFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
