import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, NgForm, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { GeneralService } from '../service/general.service';
import { User } from '../vo/User';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  form = new FormGroup({
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required)
});
user : User;
errormsg = false;
validateAllForm = false;
isLoginBtnDisabled = false;
sentMessageAppear = false;
@ViewChild('login') loginForm: NgForm;
constructor(private router: Router,
  private route: ActivatedRoute,
  private generalService: GeneralService,
  private messageService: MessageService) {
  this.user = new User();
 }

  ngOnInit(): void {
    const message = this.route.snapshot.queryParams.message;
    if(message){
      this.sentMessageAppear = true;
    }

 
  }

  onSubmit(){
    this.isLoginBtnDisabled = true;
    if (this.form.invalid) {
      this.validateAllForm = true;
      this.form.setErrors({ ...this.form.errors, yourErrorName: true });
      document.documentElement.scrollTop = 0;
      return;
    }
    const requestbody = {
        username: this.loginForm.value.username,
        password: this.loginForm.value.password
    };

    const encodedRequest: string = btoa(JSON.stringify(requestbody));
    const request = {
        encryptedData: encodedRequest
    };
    this.generalService.login(request).subscribe(
        (responseData: any) => {console.log(responseData)
           localStorage.setItem('session-token', responseData.sessionToken);
           localStorage.setItem('user', JSON.stringify(responseData));
            this.isLoginBtnDisabled = false;
            this.router.navigate(['/dashboard']);
        },
        (error: any) => {
            this.isLoginBtnDisabled = false;
            document.documentElement.scrollTop = 0;
               this.errormsg = true; 
        }
    );


  }

}
