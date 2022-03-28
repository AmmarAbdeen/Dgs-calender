import { DatePipe } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, NgForm, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService, SelectItem } from 'primeng/api';
import { GeneralService } from '../service/general.service';
import { Event } from '../vo/Event';

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.scss']
})
export class EventComponent implements OnInit {

  form = new FormGroup({
    title: new FormControl('', [Validators.required]),
    start: new FormControl('', [Validators.required]),
    end: new FormControl(),
    allDay:new FormControl(),
    sector: new FormControl('', [Validators.required])
});

cEvent : Event;
sectors: SelectItem[];
validateAllForm = false;
@ViewChild('addEvent') addNewEventForm: NgForm;
  constructor(private router: Router,
    private route: ActivatedRoute,
    private generalService: GeneralService,
    private messageService: MessageService,
    private datePipe:DatePipe) {
    this.cEvent = new Event();
   }

  ngOnInit(): void {
    this.getSectors();
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
          start: this.cEvent.start != null ?this.datePipe.transform(this.cEvent.start, 'yyyy-MM-dd HH:mm:ss'):null,
          end:  this.cEvent.end != null ?this.datePipe.transform(this.cEvent.end, 'yyyy-MM-dd HH:mm:ss'):null,
          allDay: this.cEvent.allDay,
          title: this.cEvent.title,
          sector : this.cEvent.sector
        }

        this.generalService.addEvent(model).subscribe(
          (responseData: any) => {
            document.documentElement.scrollTop = 0;
            this.messageService.clear();
            this.messageService.add({severity: 'success', detail: "Your Event saved"});
            this.addNewEventForm.reset();          
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

  getSectors(){
    this.sectors =[];
    this.generalService.getSectors().subscribe(
      (responseData: any) => {
          for (let i = 0; i < responseData.length; i++) {
              this.sectors.push({
                  label: responseData[i].name,
                  value: responseData[i].name
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
 
  cancel() {
    this.router.navigate(['/']);
  }
}
