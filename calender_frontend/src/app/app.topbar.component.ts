import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AppMainComponent} from './app.main.component';
import { GeneralService } from './service/general.service';
import { WebSocketService } from './web-socket.service';

@Component({
  selector: 'app-topbar',
  templateUrl: './app.topbar.component.html'
})
export class AppTopBarComponent {
  notifications = 0;
  fullName : any;
    constructor(private webSocketService: WebSocketService,public app: AppMainComponent,public generalService:GeneralService,private router: Router) {
        // Open connection with server socket
        let stompClient = this.webSocketService.connect();
        stompClient.connect({}, frame => {

      // Subscribe to notification topic
            stompClient.subscribe('/topic/notification', notifications => {

        // Update notifications attribute with the recent messsage sent from the server
                this.notifications = JSON.parse(notifications.body).count;
            })
        });
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

