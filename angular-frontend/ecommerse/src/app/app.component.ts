import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit{

constructor(private httpClient: HttpClient) {
}
public products: any[] | undefined;

  ngOnInit(): void {
const baseUrl = "http://localhost:8222/api/v1/products";

this.httpClient.get<any[]>(baseUrl).subscribe((res:any) => {

    console.log(res)
  this.products = res;
  }
  , error => {console.log("failed")}
);
  }


}
