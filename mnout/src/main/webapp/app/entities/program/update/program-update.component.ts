import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ProgramFormService, ProgramFormGroup } from './program-form.service';
import { IProgram } from '../program.model';
import { ProgramService } from '../service/program.service';

@Component({
  standalone: true,
  selector: 'jhi-program-update',
  templateUrl: './program-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProgramUpdateComponent implements OnInit {
  isSaving = false;
  program: IProgram | null = null;

  editForm: ProgramFormGroup = this.programFormService.createProgramFormGroup();

  constructor(
    protected programService: ProgramService,
    protected programFormService: ProgramFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ program }) => {
      this.program = program;
      if (program) {
        this.updateForm(program);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const program = this.programFormService.getProgram(this.editForm);
    if (program.id !== null) {
      this.subscribeToSaveResponse(this.programService.update(program));
    } else {
      this.subscribeToSaveResponse(this.programService.create(program));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProgram>>): void {
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

  protected updateForm(program: IProgram): void {
    this.program = program;
    this.programFormService.resetForm(this.editForm, program);
  }
}
