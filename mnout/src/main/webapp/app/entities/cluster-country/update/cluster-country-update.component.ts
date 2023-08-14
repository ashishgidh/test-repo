import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ClusterCountryFormService, ClusterCountryFormGroup } from './cluster-country-form.service';
import { IClusterCountry } from '../cluster-country.model';
import { ClusterCountryService } from '../service/cluster-country.service';

@Component({
  standalone: true,
  selector: 'jhi-cluster-country-update',
  templateUrl: './cluster-country-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ClusterCountryUpdateComponent implements OnInit {
  isSaving = false;
  clusterCountry: IClusterCountry | null = null;

  editForm: ClusterCountryFormGroup = this.clusterCountryFormService.createClusterCountryFormGroup();

  constructor(
    protected clusterCountryService: ClusterCountryService,
    protected clusterCountryFormService: ClusterCountryFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ clusterCountry }) => {
      this.clusterCountry = clusterCountry;
      if (clusterCountry) {
        this.updateForm(clusterCountry);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const clusterCountry = this.clusterCountryFormService.getClusterCountry(this.editForm);
    if (clusterCountry.id !== null) {
      this.subscribeToSaveResponse(this.clusterCountryService.update(clusterCountry));
    } else {
      this.subscribeToSaveResponse(this.clusterCountryService.create(clusterCountry));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClusterCountry>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(clusterCountry: IClusterCountry): void {
    this.clusterCountry = clusterCountry;
    this.clusterCountryFormService.resetForm(this.editForm, clusterCountry);
  }
}
