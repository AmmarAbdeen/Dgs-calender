import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, NgForm, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ConfirmationService, MessageService } from 'primeng/api';
import { GeneralService } from '../service/general.service';
import { User } from '../vo/User';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss'],
  providers: [ConfirmationService]
})
export class ChangePasswordComponent implements OnInit {
  form = new FormGroup({
    password: new FormControl('', [Validators.required,Validators.minLength(6), Validators.maxLength(10)]),
    newPassword: new FormControl('', [Validators.required,Validators.minLength(6), Validators.maxLength(10)]),
    confirmPassword: new FormControl('', [Validators.required,Validators.minLength(6), Validators.maxLength(10)]),
});
validateAllForm = false;
@ViewChild('changePassword') changePassword: NgForm;
constructor(private router: Router,
  private route: ActivatedRoute,
  private generalService: GeneralService,
  private messageService: MessageService,
  private confirmationService: ConfirmationService) {
 }

  ngOnInit(): void {
  }

  onSubmit(){
    if (this.form.invalid) {
      this.validateAllForm = true;
      this.form.setErrors({ ...this.form.errors, yourErrorName: true });
      document.documentElement.scrollTop = 0;
      this.messageService.clear();
      this.messageService.add({ severity: 'error', detail: "Please Enter your required fields" });
      return;
    }else  {
        const model = {
          password: this.form.get('password').value,
          newPassword: this.form.get('newPassword').value,
          confirmPassword: this.form.get('confirmPassword').value
        }

        const encodedRequest: string = btoa(JSON.stringify(model));
        const request = {
            encryptedData: encodedRequest
        };
       
        this.confirmationService.confirm({
            message: 'If you change your password, you will be logged out of all sessions',
            header: 'Warning',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.generalService.changePassword(request).subscribe(
                        (response) => {
                          document.documentElement.scrollTop = 0;
                          this.messageService.clear();
                          this.messageService.add({severity: 'success', detail: "Password changed successfully"});
                          this.changePassword.reset();          
                          localStorage.removeItem('session-token');
                          localStorage.removeItem('user');
                          this.router.navigate(['']);
                        },
                        (error) => {
                          document.documentElement.scrollTop = 0;
                          this.messageService.clear();
                          this.messageService.add({severity: 'error', detail: error.error.description});
                        }
                    );

            },
            reject: () => {

            }
        });

    }
  }

  cancel(){
      this.router.navigate(['/dashboard']);
  }


}
