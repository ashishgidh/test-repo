import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { PortfolioSegmentFormService, PortfolioSegmentFormGroup } from './portfolio-segment-form.service';
import { IPortfolioSegment } from '../portfolio-segment.model';
import { PortfolioSegmentService } from '../service/portfolio-segment.service';

@Component({
  standalone: true,
  selector: 'jhi-portfolio-segment-update',
  templateUrl: './portfolio-segment-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PortfolioSegmentUpdateComponent implements OnInit {
  isSaving = false;
  portfolioSegment: IPortfolioSegment | null = null;

  editForm: PortfolioSegmentFormGroup = this.portfolioSegmentFormService.createPortfolioSegmentFormGroup();

  constructor(
    protected portfolioSegmentService: PortfolioSegmentService,
    protected portfolioSegmentFormService: PortfolioSegmentFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ portfolioSegment }) => {
      this.portfolioSegment = portfolioSegment;
      if (portfolioSegment) {
        this.updateForm(portfolioSegment);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const portfolioSegment = this.portfolioSegmentFormService.getPortfolioSegment(this.editForm);
    if (portfolioSegment.id !== null) {
      this.subscribeToSaveResponse(this.portfolioSegmentService.update(portfolioSegment));
    } else {
      this.subscribeToSaveResponse(this.portfolioSegmentService.create(portfolioSegment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPortfolioSegment>>): void {
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

  protected updateForm(portfolioSegment: IPortfolioSegment): void {
    this.portfolioSegment = portfolioSegment;
    this.portfolioSegmentFormService.resetForm(this.editForm, portfolioSegment);
  }
}
