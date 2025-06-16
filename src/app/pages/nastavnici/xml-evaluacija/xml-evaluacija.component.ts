import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

@Component({
    standalone: true,
    imports: [CommonModule, FormsModule, HttpClientModule],
    selector: 'app-xml-evaluacija',
    templateUrl: './xml-evaluacija.component.html'
})
export class XmlEvaluacijaComponent {
    xmlContent: string = '';
    selectedFile: File | null = null;
    responseMessage: string = '';

    constructor(private http: HttpClient) { }

    onFileChange(event: any) {
        this.selectedFile = event.target.files[0];
    }

    uploadXmlFile() {
        if (!this.selectedFile) return;

        const formData = new FormData();
        formData.append('file', this.selectedFile);

        this.http.post('/api/evaluacija/xml/upload', formData, { responseType: 'text' })
            .subscribe({
                next: res => this.responseMessage = res,
                error: err => this.responseMessage = err.error
            });
    }

    pasteXmlContent() {
        this.http.post('/api/evaluacija/xml/paste', this.xmlContent, { responseType: 'text' })
            .subscribe({
                next: res => this.responseMessage = res,
                error: err => this.responseMessage = err.error
            });
    }
}
