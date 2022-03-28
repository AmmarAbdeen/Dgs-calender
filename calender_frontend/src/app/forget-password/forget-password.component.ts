import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, NgForm, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { GeneralService } from '../service/general.service';
import { User } from '../vo/User';

@Component({
  selector: 'app-forget-password',
  templateUrl: './forget-password.component.html',
  styleUrls: ['./forget-password.component.scss']
})
export class ForgetPasswordComponent implements OnInit {
  form = new FormGroup({
    username: new FormControl('', [Validators.required]),
    email:new FormControl('', [Validators.required,Validators.pattern("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")])
});
user : User;
isSaveBtnDisabled = false;
validateAllForm = false;
isLoginBtnDisabled = false;
errormsg = false;
errorText :String;
@ViewChild('forgetPassword') forgetPassword: NgForm;
constructor(private router: Router,
  private route: ActivatedRoute,
  private generalService: GeneralService,
  private messageService: MessageService) {
  this.user = new User();
 }

  ngOnInit(): void {
  }
  
  onSubmit(){
    if (this.form.invalid) {
      this.validateAllForm = true;
      this.form.setErrors({ ...this.form.errors, yourErrorName: true });
      document.documentElement.scrollTop = 0;
      this.errorText =  "Please Enter your required fields";
      this.errormsg = true;
      return;
    }else  {
      this.isSaveBtnDisabled = true;

        const model = {
          username: this.user.username,
          email: this.user.email,
        }

        this.generalService.verifyToSendEmail(model).subscribe(
          (responseData: any) => {
            this.isSaveBtnDisabled = false;
            this.router.navigate([''], { queryParams: { message: "!" } }); 
          },
          (error: any) => {
              document.documentElement.scrollTop = 0;
                console.log(error);
                this.errorText =  error.error.description;
                this.errormsg = true; 
                this.isSaveBtnDisabled = false;
              
          }
      );
    }
   
  }

}
