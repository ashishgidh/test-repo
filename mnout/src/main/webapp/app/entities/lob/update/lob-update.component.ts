import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { LOBFormService, LOBFormGroup } from './lob-form.service';
import { ILOB } from '../lob.model';
import { LOBService } from '../service/lob.service';

@Component({
  standalone: true,
  selector: 'jhi-lob-update',
  templateUrl: './lob-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class LOBUpdateComponent implements OnInit {
  isSaving = false;
  lOB: ILOB | null = null;

  editForm: LOBFormGroup = this.lOBFormService.createLOBFormGroup();

  constructor(protected lOBService: LOBService, protected lOBFormService: LOBFormService, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lOB }) => {
      this.lOB = lOB;
      if (lOB) {
        this.updateForm(lOB);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lOB = this.lOBFormService.getLOB(this.editForm);
    if (lOB.id !== null) {
      this.subscribeToSaveResponse(this.lOBService.update(lOB));
    } else {
      this.subscribeToSaveResponse(this.lOBService.create(lOB));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILOB>>): void {
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

  protected updateForm(lOB: ILOB): void {
    this.lOB = lOB;
    this.lOBFormService.resetForm(this.editForm, lOB);
  }
}
