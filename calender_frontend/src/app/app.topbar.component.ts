import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AppMainComponent} from './app.main.component';
import { GeneralService } from './service/general.service';

@Component({
  selector: 'app-topbar',
  templateUrl: './app.topbar.component.html'
})
export class AppTopBarComponent {
  fullName : any;
    constructor(public app: AppMainComponent,public generalService:GeneralService,private router: Router) {
    }

    ngOnInit() {
      this.fullName = JSON.parse(localStorage.getItem('user')).fullName;

    }

    navigateToMyProfile() {
      this.router.navigate(['/myprofile']);
    }

    navigateToChangePassword() {
      this.router.navigate(['/changepassword']);
    }
  
}

