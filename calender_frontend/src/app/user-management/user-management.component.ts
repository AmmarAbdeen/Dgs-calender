import { Component, OnInit } from '@angular/core';
import { NavigationExtras, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { GeneralService } from '../service/general.service';

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss']
})
export class UserManagementComponent implements OnInit {

  cols: any[];
  errormsg = false;
  users: any[];
  constructor(private generalService: GeneralService,private messageService: MessageService, private router: Router) { }

  ngOnInit(): void {
    this.cols = [
      {field: 'username', header: 'User Name'},
      {field: 'email', header: 'Email'},
      {field: 'fullName', header: 'Full Name'},
      {field: 'admin', header: 'Admin'},
      {field: 'Action', header: 'Action'}
     ];

    this.getAllUsers();
  }

  getAllUsers(){
    this.users =[];
    this.generalService.getAllUsers().subscribe(
      (responseData: any) => {
              this.users = responseData;
      },
      (error: any) => {
        document.documentElement.scrollTop = 0;
        this.messageService.clear();
        console.log(error);
        this.messageService.add({severity: 'error', detail: error.error.description});
      }
    );
  
  }

  editUser(username){
    this.router.navigate(['/user'], { queryParams: { username: username } });
  }

}
