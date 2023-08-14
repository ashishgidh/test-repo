import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IClusterCountry } from '../cluster-country.model';
import { ClusterCountryService } from '../service/cluster-country.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './cluster-country-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ClusterCountryDeleteDialogComponent {
  clusterCountry?: IClusterCountry;

  constructor(protected clusterCountryService: ClusterCountryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.clusterCountryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
