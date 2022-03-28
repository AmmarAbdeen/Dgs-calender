import { Privilege } from "./Privilege";

export class User {

    id: number;
    username: string;
    password: string;
    fullName: string;
    email: string;
    admin:boolean;
    privileges:Privilege[];
}