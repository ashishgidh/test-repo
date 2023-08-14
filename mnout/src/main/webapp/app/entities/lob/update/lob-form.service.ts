import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ILOB, NewLOB } from '../lob.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILOB for edit and NewLOBFormGroupInput for create.
 */
type LOBFormGroupInput = ILOB | PartialWithRequiredKeyOf<NewLOB>;

type LOBFormDefaults = Pick<NewLOB, 'id'>;

type LOBFormGroupContent = {
  id: FormControl<ILOB['id'] | NewLOB['id']>;
  wvLobCode: FormControl<ILOB['wvLobCode']>;
  wvLobName: FormControl<ILOB['wvLobName']>;
  maxLobCode: FormControl<ILOB['maxLobCode']>;
  maxLobName: FormControl<ILOB['maxLobName']>;
};

export type LOBFormGroup = FormGroup<LOBFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LOBFormService {
  createLOBFormGroup(lOB: LOBFormGroupInput = { id: null }): LOBFormGroup {
    const lOBRawValue = {
      ...this.getFormDefaults(),
      ...lOB,
    };
    return new FormGroup<LOBFormGroupContent>({
      id: new FormControl(
        { value: lOBRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      wvLobCode: new FormControl(lOBRawValue.wvLobCode),
      wvLobName: new FormControl(lOBRawValue.wvLobName),
      maxLobCode: new FormControl(lOBRawValue.maxLobCode),
      maxLobName: new FormControl(lOBRawValue.maxLobName),
    });
  }

  getLOB(form: LOBFormGroup): ILOB | NewLOB {
    return form.getRawValue() as ILOB | NewLOB;
  }

  resetForm(form: LOBFormGroup, lOB: LOBFormGroupInput): void {
    const lOBRawValue = { ...this.getFormDefaults(), ...lOB };
    form.reset(
      {
        ...lOBRawValue,
        id: { value: lOBRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): LOBFormDefaults {
    return {
      id: null,
    };
  }
}
