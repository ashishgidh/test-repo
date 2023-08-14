import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ClusterFormService, ClusterFormGroup } from './cluster-form.service';
import { ICluster } from '../cluster.model';
import { ClusterService } from '../service/cluster.service';

@Component({
  standalone: true,
  selector: 'jhi-cluster-update',
  templateUrl: './cluster-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ClusterUpdateComponent implements OnInit {
  isSaving = false;
  cluster: ICluster | null = null;

  editForm: ClusterFormGroup = this.clusterFormService.createClusterFormGroup();

  constructor(
    protected clusterService: ClusterService,
    protected clusterFormService: ClusterFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cluster }) => {
      this.cluster = cluster;
      if (cluster) {
        this.updateForm(cluster);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cluster = this.clusterFormService.getCluster(this.editForm);
    if (cluster.id !== null) {
      this.subscribeToSaveResponse(this.clusterService.update(cluster));
    } else {
      this.subscribeToSaveResponse(this.clusterService.create(cluster));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICluster>>): void {
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

  protected updateForm(cluster: ICluster): void {
    this.cluster = cluster;
    this.clusterFormService.resetForm(this.editForm, cluster);
  }
}
