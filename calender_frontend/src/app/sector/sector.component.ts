import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, NgForm, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService, SelectItem } from 'primeng/api';
import { GeneralService } from '../service/general.service';
import { Sector } from '../vo/Sector';

@Component({
  selector: 'app-sector',
  templateUrl: './sector.component.html',
  styleUrls: ['./sector.component.scss']
})
export class SectorComponent implements OnInit {
  form = new FormGroup({
    name: new FormControl('', [Validators.required]),
    color: new FormControl('', [Validators.required])
});
colors: SelectItem[];
sector : Sector;
validateAllForm = false;
@ViewChild('addSector') addNewSectorForm: NgForm;
constructor(private router: Router,
  private route: ActivatedRoute,
  private generalService: GeneralService,
  private messageService: MessageService) {
  this.sector = new Sector();
 }

  ngOnInit(): void {
    this.getColors();
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
          name: this.sector.name,
          color:  this.sector.color
        }

        this.generalService.addSector(model).subscribe(
          (responseData: any) => {
            document.documentElement.scrollTop = 0;
            this.messageService.clear();
            this.messageService.add({severity: 'success', detail: "Sector data saved"});
            this.addNewSectorForm.reset();          
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
 
  cancel() {
    this.router.navigate(['/']);
  }

  getColors(){
    this.colors =[];
    this.generalService.getLookupsByType('color').subscribe(
      (responseData: any) => {
          for (let i = 0; i < responseData.length; i++) {
              this.colors.push({
                  label: responseData[i].nameEN,
                  value: responseData[i].code
              });
          }
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
