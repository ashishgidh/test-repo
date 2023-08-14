import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { CSEUserFormService, CSEUserFormGroup } from './cse-user-form.service';
import { ICSEUser } from '../cse-user.model';
import { CSEUserService } from '../service/cse-user.service';

@Component({
  standalone: true,
  selector: 'jhi-cse-user-update',
  templateUrl: './cse-user-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CSEUserUpdateComponent implements OnInit {
  isSaving = false;
  cSEUser: ICSEUser | null = null;

  editForm: CSEUserFormGroup = this.cSEUserFormService.createCSEUserFormGroup();

  constructor(
    protected cSEUserService: CSEUserService,
    protected cSEUserFormService: CSEUserFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cSEUser }) => {
      this.cSEUser = cSEUser;
      if (cSEUser) {
        this.updateForm(cSEUser);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cSEUser = this.cSEUserFormService.getCSEUser(this.editForm);
    if (cSEUser.id !== null) {
      this.subscribeToSaveResponse(this.cSEUserService.update(cSEUser));
    } else {
      this.subscribeToSaveResponse(this.cSEUserService.create(cSEUser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICSEUser>>): void {
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

  protected updateForm(cSEUser: ICSEUser): void {
    this.cSEUser = cSEUser;
    this.cSEUserFormService.resetForm(this.editForm, cSEUser);
  }
}
