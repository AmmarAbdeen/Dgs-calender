import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { Event } from '../vo/Event';
import { TokenDecoded } from '../vo/TokenDecoded';
import jwt_decode from 'jwt-decode';
import { Privilege } from '../vo/Privilege';
import { User } from '../vo/User';
@Injectable({
  providedIn: 'root'
})
export class GeneralService {
  decoded: TokenDecoded;
  constructor(private httpClient: HttpClient,private router: Router) { }

  getAllEvents() {
    const url = environment.BaseUrl + '/event/allevents';
    return this.httpClient.get(url);
  }

  updateEvent(body:Event){
    const url = environment.BaseUrl + '/event/updateevent';
    const header = { 'Content-Type': 'application/json; charset=utf-8' };
    return this.httpClient.post(url, JSON.stringify(body), {headers: header});
  }

  addEvent(body:any){
    const url = environment.BaseUrl + '/event/addevent';
    const header = { 'Content-Type': 'application/json; charset=utf-8' };
    return this.httpClient.post(url, JSON.stringify(body), {headers: header});
  }

  addUser(body:any){
    const url = environment.BaseUrl + '/user/adduser';
    const header = { 'Content-Type': 'application/json; charset=utf-8' };
    return this.httpClient.post(url, JSON.stringify(body), {headers: header});
  }

  editInfo(body:any){
    const url = environment.BaseUrl + '/user/editinfo';
    const header = { 'Content-Type': 'application/json; charset=utf-8' };
    return this.httpClient.post(url, JSON.stringify(body), {headers: header});
  }

  signUser(body:any){
    const url = environment.BaseUrl + '/signuser';
    const header = { 'Content-Type': 'application/json; charset=utf-8' };
    return this.httpClient.post(url, JSON.stringify(body), {headers: header});
  }

  verifyToSendEmail(body:any){
    const url = environment.BaseUrl + '/verifytosendemail';
    const header = { 'Content-Type': 'application/json; charset=utf-8' };
    return this.httpClient.post(url, JSON.stringify(body), {headers: header});
  }

  addSector(body:any){
    const url = environment.BaseUrl + '/sectors/addsector';
    const header = { 'Content-Type': 'application/json; charset=utf-8' };
    return this.httpClient.post(url, JSON.stringify(body), {headers: header});
  }

  changePassword(body: any) {
    const url = environment.BaseUrl + '/user/changepassword';
    const header = { 'Content-Type': 'application/json; charset=utf-8' };
    return this.httpClient.post(url, JSON.stringify(body), { headers: header });
}

  getLookupsByType(type:String) {
    const url = environment.BaseUrl + '/lookups/alllookups/'+ type;
    return this.httpClient.get(url);
  }

  getParentPrivilegesByUser(id :any){
    const url = environment.BaseUrl + '/user/getparentprivileges/'+id;
    const header = { 'Content-Type': 'application/json; charset=utf-8' };
    return this.httpClient.get<Privilege[]>(url, { headers: header });
  }

  getAllPrivilegesByUser(id :any){
    const url = environment.BaseUrl + '/user/getallprivileges/'+id;
    const header = { 'Content-Type': 'application/json; charset=utf-8' };
    return this.httpClient.get<Privilege[]>(url, { headers: header });
  }

  getAllUsers(){
    const url = environment.BaseUrl + '/user/getallusers';
    const header = { 'Content-Type': 'application/json; charset=utf-8' };
    return this.httpClient.get<User[]>(url, { headers: header });
  }

  getUserByUsername(username: any) {
    const url = environment.BaseUrl + '/user/getuserinfo/' + username;
    const header = {'Content-Type': 'application/json; charset=utf-8'};
    return this.httpClient.get<User>(url, {headers: header});
}

  getSectors() {
    const url = environment.BaseUrl + '/sectors/allsectors';
    return this.httpClient.get(url);
  }

  login(body: any) {
    const url = environment.BaseUrl + '/login';
    const header = { 'Content-Type': 'application/json; charset=utf-8' };
    return this.httpClient.post(url, JSON.stringify(body), { headers: header });
  }
 

  logout() {
    localStorage.removeItem('session-token');
    localStorage.removeItem('user');
    this.router.navigate(['']);
  }

  checkExpiredToken() {
    const token = localStorage.getItem('session-token');
    if(token != null){
          this.decoded = jwt_decode(token);
          if (this.decoded.exp === undefined) {
              return false;
             }
           const date = new Date(0);
           const tokenExpDate = date.setUTCSeconds(this.decoded.exp);
           if (tokenExpDate.valueOf() > new Date().valueOf()) {
                return true;
           } else {
                return false;
            }
      }else{
        return false;
      }
  }
getToken() {
  return localStorage.getItem('session-token');
}
}
