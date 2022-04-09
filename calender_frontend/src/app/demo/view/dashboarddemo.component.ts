import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService, SelectItem } from 'primeng/api';
import { GeneralService } from 'src/app/service/general.service';
import { Event } from 'src/app/vo/Event';
import { User } from 'src/app/vo/User';
import { BreadcrumbService } from '../../app.breadcrumb.service';
import { EventService } from '../service/eventservice';

@Component({
    templateUrl: './dashboard.component.html',
    providers: [ DatePipe]
})
export class DashboardDemoComponent implements OnInit {

    events: any[];

    sectorsInfo: any[];

    options: any;

    header: any;

    eventDialog: boolean;

    changedEvent: any;

    clickedEvent = null;

    cEvent: Event;

    adminUser =  true;

    sectors: SelectItem[];

    constructor(private eventService: EventService,private datepipe: DatePipe,private router: Router,private messageService: MessageService, private datePipe: DatePipe, private breadcrumbService: BreadcrumbService,private generalService:GeneralService) {
        this.breadcrumbService.setItems([
            {label: 'Dashboard'}
        ]);
        this.cEvent = new Event();
    }

    ngOnInit() {
        this.getSectors()
        this.adminUser =  JSON.parse(localStorage.getItem('user')).admin;
        this.generalService.getAllEvents().subscribe(
            (responseData: any) => {
                this.events = responseData;
                this.options = {...this.options, ...{events: responseData}};
            },
            (error: any) => {
                document.documentElement.scrollTop = 0;
                  this.messageService.clear();
                  console.log(error);
                  this.messageService.add({severity: 'error', detail: error.error.description});
            }
        );
        // this.eventService.getEvents().then(events => {
        //     this.events = events;
        //     this.options = {...this.options, ...{events: events}};
        // });

        this.options = {
            initialDate: this.datepipe.transform(new Date(), 'yyyy-MM-dd'),
            headerToolbar: {
                left: 'prev,next today',
                center: 'title',
                right: 'dayGridMonth,timeGridWeek,timeGridDay'
            },
            editable: true,
            selectable: true,
            selectMirror: true,
            dayMaxEvents: true,
            eventClick: (e) => {
                this.getSectors();
                this.eventDialog = true;
                this.clickedEvent = e.event;
                this.cEvent.id = e.event.id;
                this.cEvent.title = e.event.title;
                this.cEvent.start = e.event.start;
                this.cEvent.end = e.event.end;
                this.cEvent.sector = e.event.sector;
                this.cEvent.allDay = e.event.allDay;
            }
        };

        this.cEvent = {id:null,title: '', start: null, end: '', allDay: null,sector:null};
    
    }

    save() {
        
        this.cEvent.start =this.cEvent.start != null ?this.datePipe.transform(this.cEvent.start, 'yyyy-MM-dd HH:mm:ss'):null;
        this.cEvent.end = this.cEvent.end != null ?this.datePipe.transform(this.cEvent.end, 'yyyy-MM-dd HH:mm:ss'):null;
        this.generalService.updateEvent(this.cEvent).subscribe(
            (responseData: any) => {
                this.clickedEvent.setProp('title', responseData.title);
                this.clickedEvent.setStart(responseData.start);
                this.clickedEvent.setEnd(responseData.end);
                this.clickedEvent.setAllDay(responseData.allDay);
                this.eventDialog = false;
            },
            (error: any) => {
                document.documentElement.scrollTop = 0;
                this.eventDialog = true;
                  this.messageService.clear();
                  console.log(error);
                  this.messageService.add({severity: 'error', detail: error.error.description});
                
            }
        );
        this.cEvent = {id:null,title: '', start: null, end: '', allDay: null,sector:null};
        
    }

    reset() {
        this.cEvent = {id:null,title: '', start: null, end: '', allDay: null,sector:null};
    }

    routeToAdd() {
        this.router.navigate(['/event']);
    }


    getSectors(){
        this.sectors =[];
        this.sectorsInfo =[];
        this.generalService.getSectors().subscribe(
          (responseData: any) => {
              for (let i = 0; i < responseData.length; i++) {
                  this.sectors.push({
                      label: responseData[i].name,
                      value: responseData[i].name
                  });
              }
              this.sectorsInfo = responseData;
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
