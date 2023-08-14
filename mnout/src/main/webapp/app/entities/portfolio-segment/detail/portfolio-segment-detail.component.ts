import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IPortfolioSegment } from '../portfolio-segment.model';

@Component({
  standalone: true,
  selector: 'jhi-portfolio-segment-detail',
  templateUrl: './portfolio-segment-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PortfolioSegmentDetailComponent {
  @Input() portfolioSegment: IPortfolioSegment | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
