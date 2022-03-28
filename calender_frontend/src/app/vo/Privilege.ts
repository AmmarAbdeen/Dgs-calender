export class Privilege{   
    id:number;
    code:String;
	adminPrivilege:boolean;
	mainPrivilege:boolean;
	englishName:string;
	arabicName:string;
	parentID:number;
	routerLink:string;
	backEndURL:string;
	iconPath:string ;
	childrenPrivilege:Privilege[];
	selected:boolean
	privilegeorder:number;
}