import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { BranchFormService, BranchFormGroup } from './branch-form.service';
import { IBranch } from '../branch.model';
import { BranchService } from '../service/branch.service';

@Component({
  standalone: true,
  selector: 'jhi-branch-update',
  templateUrl: './branch-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BranchUpdateComponent implements OnInit {
  isSaving = false;
  branch: IBranch | null = null;

  editForm: BranchFormGroup = this.branchFormService.createBranchFormGroup();

  constructor(
    protected branchService: BranchService,
    protected branchFormService: BranchFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ branch }) => {
      this.branch = branch;
      if (branch) {
        this.updateForm(branch);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const branch = this.branchFormService.getBranch(this.editForm);
    if (branch.id !== null) {
      this.subscribeToSaveResponse(this.branchService.update(branch));
    } else {
      this.subscribeToSaveResponse(this.branchService.create(branch));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBranch>>): void {
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

  protected updateForm(branch: IBranch): void {
    this.branch = branch;
    this.branchFormService.resetForm(this.editForm, branch);
  }
}
