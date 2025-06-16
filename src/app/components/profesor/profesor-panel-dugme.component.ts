import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router'; // dodaj ovo

@Component({
  selector: 'app-profesor-panel-dugme',
  standalone: true,
  imports: [RouterModule], // dodaj ovo
  templateUrl: './profesor-panel-dugme.component.html',
  styleUrls: ['./profesor-panel-dugme.component.css']
})
export class ProfesorPanelDugmeComponent {
  @Input() label: string = '';
  @Input() route: string = '';
}