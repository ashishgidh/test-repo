import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ICSEUser } from '../cse-user.model';
import { CSEUserService } from '../service/cse-user.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './cse-user-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CSEUserDeleteDialogComponent {
  cSEUser?: ICSEUser;

  constructor(protected cSEUserService: CSEUserService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cSEUserService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
