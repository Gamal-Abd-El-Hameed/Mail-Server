import { saveAs } from 'file-saver';
import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { UploadService } from 'src/app/services/upload/upload.service';
import { MailComponent } from '../mail/mail.component';
import { NavigatorComponent } from '../navigator/navigator.component';
import { HttpErrorResponse, HttpEvent, HttpEventType } from '@angular/common/http';
import { ProviderService } from 'src/app/services/provider/provider.service';

@Component({
  selector: 'app-viewer',
  templateUrl: './viewer.component.html',
  styleUrls: ['./viewer.component.css']
})
export class ViewerComponent implements OnInit {
  from?: String
  to!: String
  subject!: String
  body!: String
  filenames: string[] = []
  attachmentsID?: String[]
  importance?: String
  // Attributes to be generated either from the Backend
  date?: String
  id!: String
  // Attributes to help view content if an email exists
  empty?: boolean
  selected?: boolean

  fill(
    to: String,
    subject: String,
    body: String,
    id: String,
    from?: String,
    priority?: String,
    date?: String,
    filenames?: Array<string> 
    ) {
      if(from){
      this.from = from
      }
      if(date){
        this.date = date
      }
      this.to = to
      this.subject = subject
      this.body = body
      this.id = id
      if(priority){
        this.importance = priority
      }
      if(filenames){
        for(let i=0; i<filenames.length; i++){
          this.filenames.push(filenames[i]);
        }
      }
      this.selected = false
  }

  fileStatus = { status: '', requestType: '', percent: 0 };
  
  onDownloadFile(filename: string): void {
    this.fileService.download(filename).subscribe((data: HttpEvent<string[] | Blob>) => {
        console.log(data);
        this.resportProgress(data);
      },
      (error: HttpErrorResponse) => {
        console.log(error);
      }
    );
  }

  private resportProgress(httpEvent: HttpEvent<string[] | Blob>): void {
    switch (httpEvent.type) {
      case HttpEventType.UploadProgress:
        this.updateStatus(httpEvent.loaded, httpEvent.total!, 'Uploading... ');
        break;
      case HttpEventType.DownloadProgress:
        this.updateStatus(httpEvent.loaded, httpEvent.total!, 'Downloading... ');
        break;
      case HttpEventType.ResponseHeader:
        console.log('Header returned', httpEvent);
        break;
      case HttpEventType.Response:
        if (httpEvent.body instanceof Array) {
          this.fileStatus.status = 'done';
          for (const filename of httpEvent.body) {
            this.filenames?.unshift(filename);
          }
        } else {
          saveAs(new File([httpEvent.body!], httpEvent.headers.get('File-Name')!,
            { type: `${httpEvent.headers.get('Content-Type')};charset=utf-8` }));
          // saveAs(new Blob([httpEvent.body!], 
          //   { type: `${httpEvent.headers.get('Content-Type')};charset=utf-8`}),
          //    httpEvent.headers.get('File-Name'));
        }
        this.fileStatus.status = 'done';
        break;
      default:
        console.log(httpEvent);
        break;

    }
  }

  private updateStatus(loaded: number, total: number, requestType: string): void {
    this.fileStatus.status = 'progress';
    this.fileStatus.requestType = requestType;
    this.fileStatus.percent = Math.round(100 * loaded / total);
  }

  logged() {
    return NavigatorComponent.logged
  }

  constructor(
    private providerService: ProviderService,
    private fileService: UploadService,
    private router: Router) 
    {
        this.fill(NavigatorComponent.mail.to,NavigatorComponent.mail.subject,NavigatorComponent.mail.body,NavigatorComponent.mail.id,NavigatorComponent.mail.from,NavigatorComponent.mail.importance,NavigatorComponent.mail.date,NavigatorComponent.mail.filenames);
        this.router.navigateByUrl("account/mail");
    }
  // constructor(
  //   private fileService: UploadService,
  //   private Router: Router,
  //   private to: String,
  //   private subject: String,
  //   private body: String,
  //   private id: String,
  //   private from?: String,
  //   private priority?: String,//copy pasta
  //   private date?: String,
  //   private filenames?: Array<string> 
  //   //read the thing oui
  // ) {
  //   if(from){
  //     this.from = from
  //     }
  //     if(date){
  //       this.date = date
  //     }
  //     this.to = to
  //     this.subject = subject
  //     this.body = body
  //     this.id = id
  //     if(priority){
  //       this.importance = priority
  //     }
  //     filenames=[];
  //     if(filenames){
  //       for(let i=0; i<filenames.length; i++){
  //         this.filenames.push(filenames[i]);
  //       }
  //     }
  //     this.selected = false;
  //     this.Router.navigateByUrl('/account/mail')
  // }

  // // User-entered attributes to be sent to the Backend.
  // from?: String
  // to: String
  // subject: String
  // body: String
  // filenames: string[] = ["CSE214-2021-Lecture-1.pdf",
  //   "CSE214-2021-Lecture-2.pdf",
  //   "CSE214-2021-Lecture-3.pdf",
  //   "CSE214-2021-Lecture-4.pdf"]
  // // attachmentsID?: String[]
  // importance?: String
  // // Attributes to be generated either from the Backend
  // date?: String
  // id: String
  // // Attributes to help view content if an email exists
  // empty?: boolean
  // selected?: boolean

  // fileStatus = { status: '', requestType: '', percent: 0 };
  
  // onDownloadFile(filename: string): void {
  //   this.fileService.download(filename).subscribe((data: HttpEvent<string[] | Blob>) => {
  //       console.log(data);
  //       this.resportProgress(data);
  //     },
  //     (error: HttpErrorResponse) => {
  //       console.log(error);
  //     }
  //   );
  // }

  // private resportProgress(httpEvent: HttpEvent<string[] | Blob>): void {
  //   switch (httpEvent.type) {
  //     case HttpEventType.UploadProgress:
  //       this.updateStatus(httpEvent.loaded, httpEvent.total!, 'Uploading... ');
  //       break;
  //     case HttpEventType.DownloadProgress:
  //       this.updateStatus(httpEvent.loaded, httpEvent.total!, 'Downloading... ');
  //       break;
  //     case HttpEventType.ResponseHeader:
  //       console.log('Header returned', httpEvent);
  //       break;
  //     case HttpEventType.Response:
  //       if (httpEvent.body instanceof Array) {
  //         this.fileStatus.status = 'done';
  //         for (const filename of httpEvent.body) {
  //           this.filenames?.unshift(filename);
  //         }
  //       } else {
  //         saveAs(new File([httpEvent.body!], httpEvent.headers.get('File-Name')!,
  //           { type: `${httpEvent.headers.get('Content-Type')};charset=utf-8` }));
  //         // saveAs(new Blob([httpEvent.body!], 
  //         //   { type: `${httpEvent.headers.get('Content-Type')};charset=utf-8`}),
  //         //    httpEvent.headers.get('File-Name'));
  //       }
  //       this.fileStatus.status = 'done';
  //       break;
  //     default:
  //       console.log(httpEvent);
  //       break;

  //   }
  // }

  // private updateStatus(loaded: number, total: number, requestType: string): void {
  //   this.fileStatus.status = 'progress';
  //   this.fileStatus.requestType = requestType;
  //   this.fileStatus.percent = Math.round(100 * loaded / total);
  // }

  // logged() {
  //   return NavigatorComponent.logged
  // }

  ngOnInit(): void {
  }

}
