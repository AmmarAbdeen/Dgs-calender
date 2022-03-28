import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { GeneralService } from './service/general.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private generalService: GeneralService, private router: Router) {
  }

  canActivate() {
    if (this.generalService.checkExpiredToken()) {
        return true;
    } else {
        this.router.navigate(['']);
        localStorage.removeItem('session-token');
        return false;
    }

}
}
