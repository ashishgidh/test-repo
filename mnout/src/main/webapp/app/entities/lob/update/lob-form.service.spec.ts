import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../lob.test-samples';

import { LOBFormService } from './lob-form.service';

describe('LOB Form Service', () => {
  let service: LOBFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LOBFormService);
  });

  describe('Service methods', () => {
    describe('createLOBFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLOBFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            wvLobCode: expect.any(Object),
            wvLobName: expect.any(Object),
            maxLobCode: expect.any(Object),
            maxLobName: expect.any(Object),
          })
        );
      });

      it('passing ILOB should create a new form with FormGroup', () => {
        const formGroup = service.createLOBFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            wvLobCode: expect.any(Object),
            wvLobName: expect.any(Object),
            maxLobCode: expect.any(Object),
            maxLobName: expect.any(Object),
          })
        );
      });
    });

    describe('getLOB', () => {
      it('should return NewLOB for default LOB initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createLOBFormGroup(sampleWithNewData);

        const lOB = service.getLOB(formGroup) as any;

        expect(lOB).toMatchObject(sampleWithNewData);
      });

      it('should return NewLOB for empty LOB initial value', () => {
        const formGroup = service.createLOBFormGroup();

        const lOB = service.getLOB(formGroup) as any;

        expect(lOB).toMatchObject({});
      });

      it('should return ILOB', () => {
        const formGroup = service.createLOBFormGroup(sampleWithRequiredData);

        const lOB = service.getLOB(formGroup) as any;

        expect(lOB).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILOB should not enable id FormControl', () => {
        const formGroup = service.createLOBFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLOB should disable id FormControl', () => {
        const formGroup = service.createLOBFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
