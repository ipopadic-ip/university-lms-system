import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PregledPredmetaComponent } from './prikaz-predmeta.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { PredmetService } from '../../../core/services/predmet.service';

describe('PregledPredmetaComponent', () => {
  let component: PregledPredmetaComponent;
  let fixture: ComponentFixture<PregledPredmetaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PregledPredmetaComponent],
      providers: [PredmetService]
    }).compileComponents();

    fixture = TestBed.createComponent(PregledPredmetaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should have an empty predmeti array initially', () => {
    expect(component.predmeti).toEqual([]);
  });
});