import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICluster, NewCluster } from '../cluster.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICluster for edit and NewClusterFormGroupInput for create.
 */
type ClusterFormGroupInput = ICluster | PartialWithRequiredKeyOf<NewCluster>;

type ClusterFormDefaults = Pick<NewCluster, 'id'>;

type ClusterFormGroupContent = {
  id: FormControl<ICluster['id'] | NewCluster['id']>;
  name: FormControl<ICluster['name']>;
};

export type ClusterFormGroup = FormGroup<ClusterFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ClusterFormService {
  createClusterFormGroup(cluster: ClusterFormGroupInput = { id: null }): ClusterFormGroup {
    const clusterRawValue = {
      ...this.getFormDefaults(),
      ...cluster,
    };
    return new FormGroup<ClusterFormGroupContent>({
      id: new FormControl(
        { value: clusterRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(clusterRawValue.name),
    });
  }

  getCluster(form: ClusterFormGroup): ICluster | NewCluster {
    return form.getRawValue() as ICluster | NewCluster;
  }

  resetForm(form: ClusterFormGroup, cluster: ClusterFormGroupInput): void {
    const clusterRawValue = { ...this.getFormDefaults(), ...cluster };
    form.reset(
      {
        ...clusterRawValue,
        id: { value: clusterRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ClusterFormDefaults {
    return {
      id: null,
    };
  }
}
