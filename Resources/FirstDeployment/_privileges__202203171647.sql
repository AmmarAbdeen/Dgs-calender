INSERT INTO "privileges" (id,creation_time,admin_privilege,arabic_name,backend_url,code,english_name,icon_path,main_privilege,menu_item,privilege_order,router_link,parent_id) VALUES 
(2,NULL,true,'الحدث','','Event','Event','pi pi-fw pi-calendar-plus',true,false,2,'/event',NULL)
,(5,NULL,true,'المستخدم','','User','User','pi pi-user',true,false,3,'/user',NULL)
,(8,NULL,true,'القطاعات',NULL,'Sectors','Sectors','pi pi-fw pi-list',true,false,4,'/sector',NULL)
,(1,NULL,false,'لوحة القيادة','/event/allevents','Dashboard','Dashboard','pi pi-fw pi-home',true,false,1,'/dashboard',NULL)
,(3,NULL,true,'اضافة حدث','/event/addevent','Add Event','Add Event','pi pi-fw pi-calendar-plus',false,true,1,'/event',2)
,(4,NULL,true,'تعديل حدث','/event/updateevent','Update Event','Update Event','pi pi-fw pi-calendar-plus',false,false,0,'/dashboard',2)
,(6,NULL,true,'اضافة مستخدم','/user/adduser','Add User','Add User','pi pi-fw pi-id-card',false,true,1,'/user',5)
,(7,NULL,true,'عمليات البحث','/lookups/alllookups','Lookups','Lookups','',false,false,0,'/user',5)
,(9,NULL,true,'اضافة قطاع','/sectors/addsectors','Add Sectors','Add Sectors','pi pi-fw pi-plus-circle',false,true,1,'/sector',8)
,(10,NULL,false,'صفحتي','/user/editinfo','EditProfile','EditProfile','pi pi-user',false,false,5,'/myprofile',NULL)
;
INSERT INTO "privileges" (id,creation_time,admin_privilege,arabic_name,backend_url,code,english_name,icon_path,main_privilege,menu_item,privilege_order,router_link,parent_id) VALUES 
(11,NULL,true,'عرض كل الوظائف','/user/getparentprivileges','getPrivileges','get Privileges','pi pi-user',false,false,2,'/user',5)
,(12,NULL,true,'عرض كل الوظائف','/user/getallprivileges','getallprivileges','get all privileges','pi pi-user',false,false,1,'/user',11)
,(13,NULL,true,'ادارة المستخدم','/user/getallusers','UserManagement','User Management','pi pi-search',false,true,2,'/usermanagement',5)
,(14,NULL,true,'معلومات المستخدم','/user/getuserinfo','getUserInfo','Get Users Info','pi pi-user',false,false,3,'/user',5)
;