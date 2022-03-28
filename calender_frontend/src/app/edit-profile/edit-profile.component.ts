import { DatePipe } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, NgForm, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService, SelectItem } from 'primeng/api';
import { GeneralService } from '../service/general.service';
import { User } from '../vo/User';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.scss']
})
export class EditProfileComponent implements OnInit {
  form = new FormGroup({
    username: new FormControl('', [Validators.required]),
    fullName: new FormControl('', [Validators.required]),
    email:new FormControl('', [Validators.required,Validators.pattern("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")])
});
colors: SelectItem[];
user : User;
validateAllForm = false;
userInfo:any;
@ViewChild('myProfile') myProfile: NgForm;
constructor(private router: Router,
  private route: ActivatedRoute,
  private generalService: GeneralService,
  private messageService: MessageService,
  private datePipe:DatePipe) {
  this.user = new User();
 }

  ngOnInit(): void {
    this.userInfo = JSON.parse(localStorage.getItem('user'));
    this.generalService.getUserByUsername(this.userInfo.username).subscribe(
      (data) => {
          this.user = data;
      },
      (error) => {
              document.documentElement.scrollTop = 0;
              this.messageService.clear();
              this.messageService.add({severity: 'error', detail: error.error.description});
            }
  );
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
          id:this.user.id,
          username: this.user.username,
          fullName: this.user.fullName,
          email: this.user.email
        }

        this.generalService.editInfo(model).subscribe(
          (responseData: any) => {
            document.documentElement.scrollTop = 0;
            this.messageService.clear();
            this.messageService.add({severity: 'success', detail: "User data saved"});
            this.myProfile.reset();          
          },
          (error: any) => {
              document.documentElement.scrollTop = 0;
                this.messageService.clear();
                console.log(error);
                this.messageService.add({severity: 'error', detail: error.error.description});
              
          }
      );
    }
  }

  cancel(){
      this.router.navigate(['/dashboard']);
  }

}
