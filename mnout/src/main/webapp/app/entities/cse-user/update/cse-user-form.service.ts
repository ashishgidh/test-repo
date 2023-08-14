import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICSEUser, NewCSEUser } from '../cse-user.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICSEUser for edit and NewCSEUserFormGroupInput for create.
 */
type CSEUserFormGroupInput = ICSEUser | PartialWithRequiredKeyOf<NewCSEUser>;

type CSEUserFormDefaults = Pick<NewCSEUser, 'id'>;

type CSEUserFormGroupContent = {
  id: FormControl<ICSEUser['id'] | NewCSEUser['id']>;
  name: FormControl<ICSEUser['name']>;
  email: FormControl<ICSEUser['email']>;
  phone: FormControl<ICSEUser['phone']>;
};

export type CSEUserFormGroup = FormGroup<CSEUserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CSEUserFormService {
  createCSEUserFormGroup(cSEUser: CSEUserFormGroupInput = { id: null }): CSEUserFormGroup {
    const cSEUserRawValue = {
      ...this.getFormDefaults(),
      ...cSEUser,
    };
    return new FormGroup<CSEUserFormGroupContent>({
      id: new FormControl(
        { value: cSEUserRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(cSEUserRawValue.name),
      email: new FormControl(cSEUserRawValue.email),
      phone: new FormControl(cSEUserRawValue.phone),
    });
  }

  getCSEUser(form: CSEUserFormGroup): ICSEUser | NewCSEUser {
    return form.getRawValue() as ICSEUser | NewCSEUser;
  }

  resetForm(form: CSEUserFormGroup, cSEUser: CSEUserFormGroupInput): void {
    const cSEUserRawValue = { ...this.getFormDefaults(), ...cSEUser };
    form.reset(
      {
        ...cSEUserRawValue,
        id: { value: cSEUserRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CSEUserFormDefaults {
    return {
      id: null,
    };
  }
}
