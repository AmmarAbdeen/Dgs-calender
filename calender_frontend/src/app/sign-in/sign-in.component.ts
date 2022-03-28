import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, NgForm, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { GeneralService } from '../service/general.service';
import { User } from '../vo/User';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.scss']
})
export class SignInComponent implements OnInit {

  form = new FormGroup({
    username: new FormControl('', [Validators.required]),
    fullName: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required,Validators.minLength(6), Validators.maxLength(10)]),
    email:new FormControl('', [Validators.required,Validators.pattern("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")])
});
user : User;
validateAllForm = false;
isLoginBtnDisabled = false;
errormsg = false;
errorText :String;
@ViewChild('signin') siginForm: NgForm;
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
        const model = {
          username: this.user.username,
          password:  this.user.password,
          fullName: this.user.fullName,
          email: this.user.email,
          admin:false
        }

        this.generalService.signUser(model).subscribe(
          (responseData: any) => {
            this.router.navigate(['']); 
          },
          (error: any) => {
              document.documentElement.scrollTop = 0;
                console.log(error);
                this.errorText =  error.error.description;
                this.errormsg = true; 
              
          }
      );
    }
   
  }


}
