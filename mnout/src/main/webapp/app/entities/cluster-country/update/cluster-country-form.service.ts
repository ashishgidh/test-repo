import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IClusterCountry, NewClusterCountry } from '../cluster-country.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IClusterCountry for edit and NewClusterCountryFormGroupInput for create.
 */
type ClusterCountryFormGroupInput = IClusterCountry | PartialWithRequiredKeyOf<NewClusterCountry>;

type ClusterCountryFormDefaults = Pick<NewClusterCountry, 'id'>;

type ClusterCountryFormGroupContent = {
  id: FormControl<IClusterCountry['id'] | NewClusterCountry['id']>;
  name: FormControl<IClusterCountry['name']>;
};

export type ClusterCountryFormGroup = FormGroup<ClusterCountryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ClusterCountryFormService {
  createClusterCountryFormGroup(clusterCountry: ClusterCountryFormGroupInput = { id: null }): ClusterCountryFormGroup {
    const clusterCountryRawValue = {
      ...this.getFormDefaults(),
      ...clusterCountry,
    };
    return new FormGroup<ClusterCountryFormGroupContent>({
      id: new FormControl(
        { value: clusterCountryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(clusterCountryRawValue.name),
    });
  }

  getClusterCountry(form: ClusterCountryFormGroup): IClusterCountry | NewClusterCountry {
    return form.getRawValue() as IClusterCountry | NewClusterCountry;
  }

  resetForm(form: ClusterCountryFormGroup, clusterCountry: ClusterCountryFormGroupInput): void {
    const clusterCountryRawValue = { ...this.getFormDefaults(), ...clusterCountry };
    form.reset(
      {
        ...clusterCountryRawValue,
        id: { value: clusterCountryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ClusterCountryFormDefaults {
    return {
      id: null,
    };
  }
}
