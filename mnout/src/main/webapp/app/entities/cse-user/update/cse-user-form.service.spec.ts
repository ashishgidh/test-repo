import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../cse-user.test-samples';

import { CSEUserFormService } from './cse-user-form.service';

describe('CSEUser Form Service', () => {
  let service: CSEUserFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CSEUserFormService);
  });

  describe('Service methods', () => {
    describe('createCSEUserFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCSEUserFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            email: expect.any(Object),
            phone: expect.any(Object),
          })
        );
      });

      it('passing ICSEUser should create a new form with FormGroup', () => {
        const formGroup = service.createCSEUserFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            email: expect.any(Object),
            phone: expect.any(Object),
          })
        );
      });
    });

    describe('getCSEUser', () => {
      it('should return NewCSEUser for default CSEUser initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCSEUserFormGroup(sampleWithNewData);

        const cSEUser = service.getCSEUser(formGroup) as any;

        expect(cSEUser).toMatchObject(sampleWithNewData);
      });

      it('should return NewCSEUser for empty CSEUser initial value', () => {
        const formGroup = service.createCSEUserFormGroup();

        const cSEUser = service.getCSEUser(formGroup) as any;

        expect(cSEUser).toMatchObject({});
      });

      it('should return ICSEUser', () => {
        const formGroup = service.createCSEUserFormGroup(sampleWithRequiredData);

        const cSEUser = service.getCSEUser(formGroup) as any;

        expect(cSEUser).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICSEUser should not enable id FormControl', () => {
        const formGroup = service.createCSEUserFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCSEUser should disable id FormControl', () => {
        const formGroup = service.createCSEUserFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
