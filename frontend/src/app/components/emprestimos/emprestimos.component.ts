import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpParams } from '@angular/common/http';
import { ApiService } from '../../services/api.service';
import { Emprestimo } from '../../models/emprestimo.model';
import { Usuario } from '../../models/usuario.model';
import { Livro } from '../../models/livro.model';
@Component({
  selector: 'app-emprestimos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="row">
      <div class="col-12">
        <div class="d-flex justify-content-between align-items-center mb-4">
          <h2>
            <i class="fas fa-exchange-alt me-2"></i>
            Gestão de Empréstimos
          </h2>
          <button class="btn btn-primary" (click)="showForm = true">
            <i class="fas fa-plus me-1"></i>
            Novo Empréstimo
          </button>
        </div>
      </div>
    </div>
    <!-- Formulário de Empréstimo -->
    <div class="row mb-4" *ngIf="showForm">
      <div class="col-12">
        <div class="card">
          <div class="card-header">
            <h5 class="mb-0">
              <i class="fas fa-plus me-2"></i>
              Novo Empréstimo
            </h5>
          </div>
          <div class="card-body">
            <form (ngSubmit)="createEmprestimo()" #emprestimoForm="ngForm">
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label for="usuarioId" class="form-label">Usuário *</label>
                  <select 
                    class="form-select" 
                    id="usuarioId" 
                    name="usuarioId"
                    [(ngModel)]="newEmprestimo.usuarioId" 
                    required
                    #usuarioId="ngModel">
                    <option value="">Selecione um usuário</option>
                    <option *ngFor="let usuario of usuarios" [value]="usuario.id">
                      {{ usuario.nome }} ({{ usuario.email }})
                    </option>
                  </select>
                  <div class="invalid-feedback" *ngIf="usuarioId.invalid && usuarioId.touched">
                    Usuário é obrigatório.
                  </div>
                </div>
                <div class="col-md-6 mb-3">
                  <label for="livroId" class="form-label">Livro *</label>
                  <select 
                    class="form-select" 
                    id="livroId" 
                    name="livroId"
                    [(ngModel)]="newEmprestimo.livroId" 
                    required
                    #livroId="ngModel">
                    <option value="">Selecione um livro</option>
                    <option *ngFor="let livro of livrosDisponiveis" [value]="livro.id">
                      {{ livro.titulo }} - {{ livro.autor }}
                    </option>
                  </select>
                  <div class="invalid-feedback" *ngIf="livroId.invalid && livroId.touched">
                    Livro é obrigatório.
                  </div>
                </div>
              </div>
              <div class="d-flex gap-2">
                <button type="submit" class="btn btn-primary" [disabled]="emprestimoForm.invalid">
                  <i class="fas fa-save me-1"></i>
                  Criar Empréstimo
                </button>
                <button type="button" class="btn btn-secondary" (click)="cancelForm()">
                  <i class="fas fa-times me-1"></i>
                  Cancelar
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
    <!-- Filtros -->
    <div class="row mb-4">
      <div class="col-12">
        <div class="card">
          <div class="card-body">
            <div class="row">
              <div class="col-md-4">
                <label for="statusFilter" class="form-label">Filtrar por Status</label>
                <select class="form-select" id="statusFilter" [(ngModel)]="statusFilter" (change)="filterEmprestimos()">
                  <option value="">Todos</option>
                  <option value="ATIVO">Ativos</option>
                  <option value="DEVOLVIDO">Devolvidos</option>
                </select>
              </div>
              <div class="col-md-4">
                <label for="usuarioFilter" class="form-label">Filtrar por Usuário</label>
                <select class="form-select" id="usuarioFilter" [(ngModel)]="usuarioFilter" (change)="filterEmprestimos()">
                  <option value="">Todos</option>
                  <option *ngFor="let usuario of usuarios" [value]="usuario.id">
                    {{ usuario.nome }}
                  </option>
                </select>
              </div>
              <div class="col-md-4 d-flex align-items-end">
                <button class="btn btn-outline-secondary" (click)="clearFilters()">
                  <i class="fas fa-times me-1"></i>
                  Limpar Filtros
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- Tabela de Empréstimos -->
    <div class="row">
      <div class="col-12">
        <div class="card">
          <div class="card-header">
            <h5 class="mb-0">Lista de Empréstimos</h5>
          </div>
          <div class="card-body">
            <div class="table-responsive">
              <table class="table table-striped table-hover">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Usuário</th>
                    <th>Livro</th>
                    <th>Data Empréstimo</th>
                    <th>Data Devolução</th>
                    <th>Status</th>
                    <th>Ações</th>
                  </tr>
                </thead>
                <tbody>
                  <tr *ngFor="let emprestimo of filteredEmprestimos">
                    <td>{{ emprestimo.id }}</td>
                    <td>{{ emprestimo.usuarioNome }}</td>
                    <td>{{ emprestimo.livroTitulo }}</td>
                    <td>{{ emprestimo.dataEmprestimo | date:'dd/MM/yyyy' }}</td>
                    <td>{{ emprestimo.dataDevolucao | date:'dd/MM/yyyy' || '-' }}</td>
                    <td>
                      <span class="badge" [class]="emprestimo.status === 'ATIVO' ? 'bg-warning' : 'bg-success'">
                        {{ emprestimo.status }}
                      </span>
                    </td>
                    <td>
                      <div class="btn-group" role="group">
                        <button 
                          class="btn btn-sm btn-outline-success" 
                          *ngIf="emprestimo.status === 'ATIVO'"
                          (click)="devolverLivro(emprestimo.id!)">
                          <i class="fas fa-undo"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-info" (click)="showRecomendacoes(emprestimo.usuarioId)">
                          <i class="fas fa-lightbulb"></i>
                        </button>
                      </div>
                    </td>
                  </tr>
                  <tr *ngIf="filteredEmprestimos.length === 0">
                    <td colspan="7" class="text-center">Nenhum empréstimo encontrado.</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- Modal de Recomendações -->
    <div class="modal fade" id="recomendacoesModal" tabindex="-1" *ngIf="showRecomendacoesModal">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">
              <i class="fas fa-lightbulb me-2"></i>
              Recomendações de Livros
            </h5>
            <button type="button" class="btn-close" (click)="closeRecomendacoesModal()"></button>
          </div>
          <div class="modal-body">
            <div class="row">
              <div class="col-md-6 mb-3" *ngFor="let livro of recomendacoes">
                <div class="card h-100">
                  <div class="card-body">
                    <h6 class="card-title">{{ livro.titulo }}</h6>
                    <p class="card-text"><strong>Autor:</strong> {{ livro.autor }}</p>
                    <p class="card-text"><strong>Categoria:</strong> {{ livro.categoria }}</p>
                    <p class="card-text"><strong>ISBN:</strong> {{ livro.isbn }}</p>
                    <button class="btn btn-sm btn-primary" (click)="emprestarRecomendado(livro)">
                      <i class="fas fa-plus me-1"></i>
                      Emprestar
                    </button>
                  </div>
                </div>
              </div>
              <div class="col-12" *ngIf="recomendacoes.length === 0">
                <p class="text-center text-muted">Nenhuma recomendação disponível.</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- Alertas -->
    <div class="alert alert-success alert-dismissible fade show" role="alert" *ngIf="successMessage">
      <i class="fas fa-check-circle me-2"></i>
      {{ successMessage }}
      <button type="button" class="btn-close" (click)="successMessage = ''"></button>
    </div>
    <div class="alert alert-danger alert-dismissible fade show" role="alert" *ngIf="errorMessage">
      <i class="fas fa-exclamation-circle me-2"></i>
      {{ errorMessage }}
      <button type="button" class="btn-close" (click)="errorMessage = ''"></button>
    </div>
  `,
  styles: [`
    .card {
      border: none;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
    
    .table th {
      background-color: #f8f9fa;
      border-top: none;
    }
    
    .btn-group .btn {
      margin-right: 2px;
    }
    
    .alert {
      position: fixed;
      top: 20px;
      right: 20px;
      z-index: 1050;
      min-width: 300px;
    }
    
    .modal {
      display: block;
      background-color: rgba(0,0,0,0.5);
    }
  `]
})
export class EmprestimosComponent implements OnInit {
  emprestimos: Emprestimo[] = [];
  filteredEmprestimos: Emprestimo[] = [];
  usuarios: Usuario[] = [];
  livrosDisponiveis: Livro[] = [];
  recomendacoes: Livro[] = [];
  newEmprestimo: Emprestimo = { usuarioId: 0, livroId: 0 };
  showForm = false;
  showRecomendacoesModal = false;
  statusFilter = '';
  usuarioFilter = '';
  successMessage = '';
  errorMessage = '';
  constructor(private apiService: ApiService) {}
  ngOnInit() {
    this.loadData();
  }
  loadData() {
    this.apiService.get<Emprestimo[]>('/emprestimos').subscribe({
      next: (data) => {
        this.emprestimos = data;
        this.filteredEmprestimos = data;
      },
      error: (error) => {
        this.errorMessage = 'Erro ao carregar empréstimos: ' + error.message;
      }
    });
    this.apiService.get<Usuario[]>('/usuarios').subscribe({
      next: (data) => {
        this.usuarios = data;
      },
      error: (error) => {
        this.errorMessage = 'Erro ao carregar usuários: ' + error.message;
      }
    });
    this.loadLivrosDisponiveis();
  }
  loadLivrosDisponiveis() {
    this.apiService.get<Livro[]>('/livros').subscribe({
      next: (data) => {
        this.livrosDisponiveis = data.filter(livro => {
          return !this.emprestimos.some(emp => 
            emp.livroId === livro.id && emp.status === 'ATIVO'
          );
        });
      },
      error: (error) => {
        this.errorMessage = 'Erro ao carregar livros: ' + error.message;
      }
    });
  }
  createEmprestimo() {
    const params = new HttpParams()
      .set('usuarioId', this.newEmprestimo.usuarioId.toString())
      .set('livroId', this.newEmprestimo.livroId.toString());
    
    this.apiService.post<Emprestimo>('/emprestimos', null, { params }).subscribe({
      next: (data) => {
        this.emprestimos.push(data);
        this.filteredEmprestimos = this.emprestimos;
        this.successMessage = 'Empréstimo criado com sucesso!';
        this.cancelForm();
        this.loadLivrosDisponiveis();
      },
      error: (error) => {
        this.errorMessage = 'Erro ao criar empréstimo: ' + error.message;
      }
    });
  }
  devolverLivro(emprestimoId: number) {
    if (confirm('Confirmar devolução do livro?')) {
      this.apiService.put<Emprestimo>(`/emprestimos/${emprestimoId}/devolver`, {}).subscribe({
        next: (data) => {
          const index = this.emprestimos.findIndex(e => e.id === emprestimoId);
          if (index !== -1) {
            this.emprestimos[index] = data;
            this.filteredEmprestimos = this.emprestimos;
          }
          this.successMessage = 'Livro devolvido com sucesso!';
          this.loadLivrosDisponiveis();
        },
        error: (error) => {
          this.errorMessage = 'Erro ao devolver livro: ' + error.message;
        }
      });
    }
  }
  showRecomendacoes(usuarioId: number) {
    if (!usuarioId) {
      this.errorMessage = 'Por favor, selecione um usuário primeiro.';
      return;
    }
    
    this.apiService.get<Livro[]>(`/emprestimos/usuario/${usuarioId}/recomendacoes`).subscribe({
      next: (data) => {
        this.recomendacoes = data;
        this.showRecomendacoesModal = true;
      },
      error: (error) => {
        this.errorMessage = 'Erro ao carregar recomendações: ' + error.message;
      }
    });
  }
  emprestarRecomendado(livro: Livro) {
    const usuario = this.usuarios.find(u => u.id === Number(this.usuarioFilter));
    if (usuario) {
      this.newEmprestimo = { usuarioId: usuario.id!, livroId: livro.id! };
      this.createEmprestimo();
      this.closeRecomendacoesModal();
    } else {
      this.errorMessage = 'Usuário não encontrado. Por favor, selecione um usuário primeiro.';
    }
  }
  closeRecomendacoesModal() {
    this.showRecomendacoesModal = false;
    this.recomendacoes = [];
  }
  filterEmprestimos() {
    this.filteredEmprestimos = this.emprestimos.filter(emp => {
      const statusMatch = !this.statusFilter || emp.status === this.statusFilter;
      const usuarioMatch = !this.usuarioFilter || emp.usuarioId === Number(this.usuarioFilter);
      return statusMatch && usuarioMatch;
    });
  }
  clearFilters() {
    this.statusFilter = '';
    this.usuarioFilter = '';
    this.filteredEmprestimos = this.emprestimos;
  }
  cancelForm() {
    this.showForm = false;
    this.newEmprestimo = { usuarioId: 0, livroId: 0 };
  }
}
