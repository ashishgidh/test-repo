import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProgram, NewProgram } from '../program.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProgram for edit and NewProgramFormGroupInput for create.
 */
type ProgramFormGroupInput = IProgram | PartialWithRequiredKeyOf<NewProgram>;

type ProgramFormDefaults = Pick<NewProgram, 'id'>;

type ProgramFormGroupContent = {
  id: FormControl<IProgram['id'] | NewProgram['id']>;
  programId: FormControl<IProgram['programId']>;
  clusterId: FormControl<IProgram['clusterId']>;
  countryId: FormControl<IProgram['countryId']>;
  branchId: FormControl<IProgram['branchId']>;
  maxLOBId: FormControl<IProgram['maxLOBId']>;
  wvLOBId: FormControl<IProgram['wvLOBId']>;
  programEffectiveDate: FormControl<IProgram['programEffectiveDate']>;
  programExpiryDate: FormControl<IProgram['programExpiryDate']>;
};

export type ProgramFormGroup = FormGroup<ProgramFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProgramFormService {
  createProgramFormGroup(program: ProgramFormGroupInput = { id: null }): ProgramFormGroup {
    const programRawValue = {
      ...this.getFormDefaults(),
      ...program,
    };
    return new FormGroup<ProgramFormGroupContent>({
      id: new FormControl(
        { value: programRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      programId: new FormControl(programRawValue.programId),
      clusterId: new FormControl(programRawValue.clusterId),
      countryId: new FormControl(programRawValue.countryId),
      branchId: new FormControl(programRawValue.branchId),
      maxLOBId: new FormControl(programRawValue.maxLOBId),
      wvLOBId: new FormControl(programRawValue.wvLOBId),
      programEffectiveDate: new FormControl(programRawValue.programEffectiveDate),
      programExpiryDate: new FormControl(programRawValue.programExpiryDate),
    });
  }

  getProgram(form: ProgramFormGroup): IProgram | NewProgram {
    return form.getRawValue() as IProgram | NewProgram;
  }

  resetForm(form: ProgramFormGroup, program: ProgramFormGroupInput): void {
    const programRawValue = { ...this.getFormDefaults(), ...program };
    form.reset(
      {
        ...programRawValue,
        id: { value: programRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProgramFormDefaults {
    return {
      id: null,
    };
  }
}
