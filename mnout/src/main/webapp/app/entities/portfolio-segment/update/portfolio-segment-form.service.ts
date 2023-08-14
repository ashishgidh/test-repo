import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPortfolioSegment, NewPortfolioSegment } from '../portfolio-segment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPortfolioSegment for edit and NewPortfolioSegmentFormGroupInput for create.
 */
type PortfolioSegmentFormGroupInput = IPortfolioSegment | PartialWithRequiredKeyOf<NewPortfolioSegment>;

type PortfolioSegmentFormDefaults = Pick<NewPortfolioSegment, 'id'>;

type PortfolioSegmentFormGroupContent = {
  id: FormControl<IPortfolioSegment['id'] | NewPortfolioSegment['id']>;
  portfolioSigment: FormControl<IPortfolioSegment['portfolioSigment']>;
  name: FormControl<IPortfolioSegment['name']>;
  segment: FormControl<IPortfolioSegment['segment']>;
};

export type PortfolioSegmentFormGroup = FormGroup<PortfolioSegmentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PortfolioSegmentFormService {
  createPortfolioSegmentFormGroup(portfolioSegment: PortfolioSegmentFormGroupInput = { id: null }): PortfolioSegmentFormGroup {
    const portfolioSegmentRawValue = {
      ...this.getFormDefaults(),
      ...portfolioSegment,
    };
    return new FormGroup<PortfolioSegmentFormGroupContent>({
      id: new FormControl(
        { value: portfolioSegmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      portfolioSigment: new FormControl(portfolioSegmentRawValue.portfolioSigment),
      name: new FormControl(portfolioSegmentRawValue.name),
      segment: new FormControl(portfolioSegmentRawValue.segment),
    });
  }

  getPortfolioSegment(form: PortfolioSegmentFormGroup): IPortfolioSegment | NewPortfolioSegment {
    return form.getRawValue() as IPortfolioSegment | NewPortfolioSegment;
  }

  resetForm(form: PortfolioSegmentFormGroup, portfolioSegment: PortfolioSegmentFormGroupInput): void {
    const portfolioSegmentRawValue = { ...this.getFormDefaults(), ...portfolioSegment };
    form.reset(
      {
        ...portfolioSegmentRawValue,
        id: { value: portfolioSegmentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PortfolioSegmentFormDefaults {
    return {
      id: null,
    };
  }
}
