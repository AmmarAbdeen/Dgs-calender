import {Injectable, Injector} from '@angular/core';
import {HttpInterceptor} from '@angular/common/http';
import { GeneralService } from './service/general.service';

@Injectable({
  providedIn: 'root'
})
export class TokenInterceptorService implements HttpInterceptor {

  constructor(private injector: Injector) {
  }

  intercept(req, next) {
    let authService = this.injector.get(GeneralService);
    let tokenizedReq = req.clone({
        setHeaders: {
            'session-token': `${authService.getToken()}`
        }
    });
    return next.handle(tokenizedReq);

}
}
