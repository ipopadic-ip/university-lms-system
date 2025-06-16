import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProfesorPanelDugmeComponent } from './profesor-panel-dugme.component';
import { RouterTestingModule } from '@angular/router/testing';

describe('ProfesorPanelDugmeComponent', () => {
  let component: ProfesorPanelDugmeComponent;
  let fixture: ComponentFixture<ProfesorPanelDugmeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      declarations: [ProfesorPanelDugmeComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(ProfesorPanelDugmeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});