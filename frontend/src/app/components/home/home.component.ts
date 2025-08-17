import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="row">
      <div class="col-md-4 mb-4">
        <div class="card h-100">
          <div class="card-body text-center">
            <i class="fas fa-users fa-3x text-primary mb-3"></i>
            <h5 class="card-title">Gestão de Usuários</h5>
            <p class="card-text">
              Cadastre e gerencie os usuários da biblioteca com informações completas.
            </p>
            <a routerLink="/usuarios" class="btn btn-primary">
              <i class="fas fa-arrow-right me-1"></i>
              Gerenciar Usuários
            </a>
          </div>
        </div>
      </div>
      <div class="col-md-4 mb-4">
        <div class="card h-100">
          <div class="card-body text-center">
            <i class="fas fa-book fa-3x text-success mb-3"></i>
            <h5 class="card-title">Catálogo de Livros</h5>
            <p class="card-text">
              Mantenha um catálogo completo com informações detalhadas dos livros.
            </p>
            <a routerLink="/livros" class="btn btn-success">
              <i class="fas fa-arrow-right me-1"></i>
              Gerenciar Livros
            </a>
          </div>
        </div>
      </div>
      <div class="col-md-4 mb-4">
        <div class="card h-100">
          <div class="card-body text-center">
            <i class="fas fa-exchange-alt fa-3x text-warning mb-3"></i>
            <h5 class="card-title">Controle de Empréstimos</h5>
            <p class="card-text">
              Controle empréstimos e devoluções com sistema de recomendações.
            </p>
            <a routerLink="/emprestimos" class="btn btn-warning">
              <i class="fas fa-arrow-right me-1"></i>
              Gerenciar Empréstimos
            </a>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .card {
      transition: transform 0.2s;
      border: none;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
    
    .card:hover {
      transform: translateY(-5px);
      box-shadow: 0 4px 8px rgba(0,0,0,0.2);
    }
    
    .fa-3x {
      font-size: 3em;
    }
  `]
})
export class HomeComponent implements OnInit {
  constructor(private apiService: ApiService) {}

  ngOnInit() {
    // Componente simplificado - não precisa carregar estatísticas
  }
}
