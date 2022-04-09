import { DatePipe } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, NgForm, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService, SelectItem, TreeNode } from 'primeng/api';
import { GeneralService } from '../service/general.service';
import { Privilege } from '../vo/Privilege';
import { User } from '../vo/User';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.scss']
})
export class AddUserComponent implements OnInit {
  form = new FormGroup({
    username: new FormControl('', [Validators.required]),
    fullName: new FormControl('', [Validators.required]),
    sector: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required,Validators.minLength(6), Validators.maxLength(10)]),
    email:new FormControl('', [Validators.required,Validators.pattern("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")])
});
colors: SelectItem[];
sectors: SelectItem[];
user : User;
userTree: TreeNode[];
parentprivileges: Privilege[];
treeSelection: TreeNode[];
selected: Privilege[];
privileges: Privilege[];
userInfo:any;
editMode = false;
navigateParams: any;
validateAllForm = false;
@ViewChild('addUser') addNewUserForm: NgForm;
constructor(private router: Router,
  private route: ActivatedRoute,
  private generalService: GeneralService,
  private messageService: MessageService,
  private datePipe:DatePipe) {
  this.user = new User();
 }

  ngOnInit(): void {
    this.getSectors();
    this.selected = [];
    this.treeSelection = [];
    this.userInfo = JSON.parse(localStorage.getItem('user'));
    this.route.queryParams.subscribe(params => {
      if (!params['username']) {
          this.setFormGroupControlsValsToEmpty();
          this.removeAllSelections();
          this.editMode = false;
      }
  });

  const username = this.route.snapshot.queryParams.username;
  if (username) {
    this.editMode=true;
    this.generalService.getUserByUsername(username).subscribe(
        (data) => {
            this.user = data;
            for (let i in this.user.privileges) {
                this.treeSelection.push(this.privilegeToNode(this.user.privileges[i]));
                this.selected.push(this.user.privileges[i]);
            }
        },
        (error) => {
                document.documentElement.scrollTop = 0;
                this.messageService.clear();
                this.messageService.add({severity: 'error', detail: error.error.description});
              }
    );
}
    this.getParentPrivileges();
    this.getAllPrivileges();
  }

  getAllPrivileges(){
    this.generalService.getAllPrivilegesByUser(this.userInfo.id).subscribe(
            (data) => {
                this.privileges = data;
            },
            (error) => {
                this.messageService.clear();
                this.messageService.add({ severity: 'error', detail: 'There is a problem loading Privileges'});
             
            }
        );
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

getParentPrivileges(){
  this.generalService.getParentPrivilegesByUser(this.userInfo.id).subscribe(
    (data) => {
        this.userTree = [];
        this.parentprivileges = data;
        for (let i in this.parentprivileges) {
                this.userTree.push(this.privilegeToNode(this.parentprivileges[i]));
            }
        },
    (error) => {
            this.messageService.clear();
            this.messageService.add({ severity: 'error', detail: 'There is a problem loading Privileges'});
        
        }
    );
}

private privilegeToNode(privilege: Privilege): TreeNode {
  let privilegeNode: TreeNode = { children: [] };
  if (privilege.childrenPrivilege == null) {
       privilegeNode.label = privilege.englishName; 
      privilegeNode.key = privilege.id.toString();
      privilegeNode.expandedIcon = "pi pi-folder-open";
      privilegeNode.collapsedIcon = "pi pi-folder";
      return privilegeNode;
  }
  else {
      privilegeNode.label = privilege.englishName; 
      privilegeNode.key = privilege.id.toString();
      privilegeNode.expandedIcon = "pi pi-folder-open";
      privilegeNode.collapsedIcon = "pi pi-folder";
      for (let i in privilege.childrenPrivilege) {
          privilegeNode.children.push(this.privilegeToNode(privilege.childrenPrivilege[i]))
      }
      return privilegeNode;
  }
}

nodeUnSelect(event) {
  for (let i = 0; i < this.selected.length; i++) {
      if (this.selected[i].id == event.node.key) {
          this.selected.splice(i, 1);
      }
  }
  this.removeChildren(event.node.children);
  this.removeParents(event.node);
}

removeChildren(children: any) {
    if (children.length == 0)
        return;

    for (let child in children) {
        this.removeChildren(children[child].children);
        for (let i = 0; i < this.selected.length; i++) {
            if (this.selected[i].id == children[child].key) {
                this.selected.splice(i, 1);
                break;
            }
        }
    }
}

removeParents(node: TreeNode) {
    while (node.parent != null) {
        for (let i = 0; i < this.selected.length; i++) {
            if (Number(node.parent.key) === this.selected[i].id) {
                this.selected.splice(i, 1);
                break;
            }
        }
        node = node.parent;
    }
}


nodeSelect(event) {
  for (let i in this.privileges) {
    for (let j in this.treeSelection) {
        let exist = false;
        if (this.treeSelection[j].key == this.privileges[i].id.toString()) {
            if (this.selected.length == 0) {
                this.selected.push(this.privileges[i]);
            }
            else {

                for (let k in this.selected) {
                    if (this.treeSelection[j].key == this.selected[k].id.toString()) {
                        exist = true;
                    }
                }
                if (!exist) {
                    this.selected.push(this.privileges[i]);

                }
            }
        }
        if (this.treeSelection[j].parent != null)
            this.addParent(this.treeSelection[j])
    }

}


}

nonSelectedHigherOrderPrivelege(event: any) {
  let parent =  event.node.parent;
  if (this.isSelected(parent.id)) {
      return;
  }
  else return parent;
  
}

getPrivilege(nodeKey: any) {
  for (let privilege of this.privileges) {
      if (nodeKey == privilege.id){
          return privilege;
        }
  }
  return null ;
}

isSelected(privilegeChildId: any) {
  for (let privilege of this.selected) {
      if (privilegeChildId == privilege.id)
          return true;
  }
  return false;
}

addParent(node: TreeNode) {
    for (let i in this.privileges) {
      if (node.parent != null) {
          if (node.parent.key == this.privileges[i].id.toString()) {
              let exist = false;
              for (let k in this.selected) {
                  if (node.parent.key == this.selected[k].id.toString()) {
                      exist = true;
                  }
              }
              if (!exist) {
                  this.selected.push(this.privileges[i]);
                  this.addParent(node.parent)
              }
          }
      }
    }
  }

  removeParentsPartialSelections(node: any) {
    while (node.parent != null) {
      node.parent.partialSelected = false;
      node = node.parent;
    }
  }

  removeTreeSelections(node: any) {
    while (this.treeSelection.pop().label.localeCompare(node.label) != 0);
}


  onSubmit(){
    this.user.privileges = [];

    if (this.form.invalid) {
      this.validateAllForm = true;
      this.form.setErrors({ ...this.form.errors, yourErrorName: true });
      document.documentElement.scrollTop = 0;
      this.messageService.clear();
      this.messageService.add({ severity: 'error', detail: "Please Enter your required fields" });
      return;
    }else  {
      if (this.selected.length == 0) {
        document.documentElement.scrollTop = 0;
        this.messageService.clear();
        this.messageService.add({ severity: 'error', detail: 'You must at least choose a Privilege' });
        return;
      }
        const model = {
          username: this.user.username,
          password:  this.user.password,
          fullName: this.user.fullName,
          email: this.user.email,
          admin:true,
          privileges: this.selected,
          sector : this.user.sector
        }

        this.generalService.addUser(model).subscribe(
          (responseData: any) => {
            document.documentElement.scrollTop = 0;
            this.messageService.clear();
            this.messageService.add({severity: 'success', detail: "User data saved"});
            this.addNewUserForm.reset();          
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

  setFormGroupControlsValsToEmpty() {
    const formVals = this.form.value;
    this.form.reset();
    for (let formVal in formVals) {
        this.form.patchValue({ [formVal]: '' });
    }
    console.log("form values:");
    console.log(this.form.value);
}


removeAllSelections() {
    for (let privilege in this.selected) {
        this.removeParentsPartialSelections(privilege);
    }
    this.selected = [];
    this.treeSelection = [];
}
 
  cancel() {
    this.router.navigate(['/']);
  }

  

}
