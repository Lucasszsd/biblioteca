import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { Livro } from '../../models/livro.model';
@Component({
  selector: 'app-livros',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="row">
      <div class="col-12">
        <div class="d-flex justify-content-between align-items-center mb-4">
          <h2>
            <i class="fas fa-book me-2"></i>
            Gestão de Livros
          </h2>
          <div>
            <button class="btn btn-primary" (click)="showForm = true; editingLivro = null">
              <i class="fas fa-plus me-1"></i>
              Novo Livro
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Formulário -->
    <div class="row mb-4" *ngIf="showForm">
      <div class="col-12">
        <div class="card">
          <div class="card-header">
            <h5 class="mb-0">
              <i class="fas fa-book me-2"></i>
              {{ editingLivro ? 'Editar' : 'Novo' }} Livro
            </h5>
          </div>
          <div class="card-body">
            <form (ngSubmit)="saveLivro()" #livroForm="ngForm">
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label for="titulo" class="form-label">Título *</label>
                  <input 
                    type="text" 
                    class="form-control" 
                    id="titulo" 
                    name="titulo"
                    [(ngModel)]="currentLivro.titulo" 
                    required
                    #titulo="ngModel">
                  <div class="invalid-feedback" *ngIf="titulo.invalid && titulo.touched">
                    Título é obrigatório.
                  </div>
                </div>
                <div class="col-md-6 mb-3">
                  <label for="autor" class="form-label">Autor *</label>
                  <input 
                    type="text" 
                    class="form-control" 
                    id="autor" 
                    name="autor"
                    [(ngModel)]="currentLivro.autor" 
                    required
                    #autor="ngModel">
                  <div class="invalid-feedback" *ngIf="autor.invalid && autor.touched">
                    Autor é obrigatório.
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col-md-4 mb-3">
                  <label for="isbn" class="form-label">ISBN *</label>
                  <input 
                    type="text" 
                    class="form-control" 
                    id="isbn" 
                    name="isbn"
                    [(ngModel)]="currentLivro.isbn" 
                    required
                    #isbn="ngModel">
                  <div class="invalid-feedback" *ngIf="isbn.invalid && isbn.touched">
                    ISBN é obrigatório.
                  </div>
                </div>
                <div class="col-md-4 mb-3">
                  <label for="categoria" class="form-label">Categoria *</label>
                  <input 
                    type="text" 
                    class="form-control" 
                    id="categoria" 
                    name="categoria"
                    [(ngModel)]="currentLivro.categoria" 
                    required
                    #categoria="ngModel">
                  <div class="invalid-feedback" *ngIf="categoria.invalid && categoria.touched">
                    Categoria é obrigatória.
                  </div>
                </div>
                <div class="col-md-4 mb-3">
                  <label for="dataPublicacao" class="form-label">Data de Publicação</label>
                  <input 
                    type="date" 
                    class="form-control" 
                    id="dataPublicacao" 
                    name="dataPublicacao"
                    [(ngModel)]="currentLivro.dataPublicacao">
                </div>
              </div>
              <div class="d-flex gap-2">
                <button type="submit" class="btn btn-primary" [disabled]="livroForm.invalid">
                  <i class="fas fa-save me-1"></i>
                  {{ editingLivro ? 'Atualizar' : 'Salvar' }}
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
    <!-- Tabela -->
    <div class="row">
      <div class="col-12">
        <div class="card">
          <div class="card-header">
            <h5 class="mb-0">Catálogo de Livros</h5>
          </div>
          <div class="card-body">
            <div class="table-responsive">
              <table class="table table-striped table-hover">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Título</th>
                    <th>Autor</th>
                    <th>ISBN</th>
                    <th>Categoria</th>
                    <th>Data Publicação</th>
                    <th>Ações</th>
                  </tr>
                </thead>
                <tbody>
                  <tr *ngFor="let livro of livros">
                    <td>{{ livro.id }}</td>
                    <td>{{ livro.titulo }}</td>
                    <td>{{ livro.autor }}</td>
                    <td>{{ livro.isbn }}</td>
                    <td>{{ livro.categoria }}</td>
                    <td>{{ livro.dataPublicacao | date:'dd/MM/yyyy' }}</td>
                    <td>
                      <div class="btn-group" role="group">
                        <button class="btn btn-sm btn-outline-primary" (click)="editLivro(livro)">
                          <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-danger" (click)="deleteLivro(livro.id!)">
                          <i class="fas fa-trash"></i>
                        </button>
                      </div>
                    </td>
                  </tr>
                  <tr *ngIf="livros.length === 0">
                    <td colspan="7" class="text-center">Nenhum livro encontrado.</td>
                  </tr>
                </tbody>
              </table>
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
  `]
})
export class LivrosComponent implements OnInit {
  livros: Livro[] = [];
  currentLivro: Livro = { titulo: '', autor: '', isbn: '', categoria: '' };
  editingLivro: Livro | null = null;
  showForm = false;
  successMessage = '';
  errorMessage = '';
  constructor(private apiService: ApiService) {}
  ngOnInit() {
    this.loadLivros();
  }
  loadLivros() {
    this.apiService.get<Livro[]>('/livros').subscribe({
      next: (data) => {
        this.livros = data;
      },
      error: (error) => {
        this.errorMessage = 'Erro ao carregar livros: ' + error.message;
      }
    });
  }

  saveLivro() {
    if (this.editingLivro) {
      this.apiService.put<Livro>(`/livros/${this.editingLivro.id}`, this.currentLivro).subscribe({
        next: (data) => {
          const index = this.livros.findIndex(l => l.id === this.editingLivro!.id);
          if (index !== -1) {
            this.livros[index] = data;
          }
          this.successMessage = 'Livro atualizado com sucesso!';
          this.cancelForm();
        },
        error: (error) => {
          this.errorMessage = 'Erro ao atualizar livro: ' + error.message;
        }
      });
    } else {
      this.apiService.post<Livro>('/livros', this.currentLivro).subscribe({
        next: (data) => {
          this.livros.push(data);
          this.successMessage = 'Livro criado com sucesso!';
          this.cancelForm();
        },
        error: (error) => {
          this.errorMessage = 'Erro ao criar livro: ' + error.message;
        }
      });
    }
  }
  editLivro(livro: Livro) {
    this.editingLivro = livro;
    this.currentLivro = { ...livro };
    this.showForm = true;
  }
  deleteLivro(id: number) {
    if (confirm('Tem certeza que deseja excluir este livro?')) {
      this.apiService.delete(`/livros/${id}`).subscribe({
        next: () => {
          this.livros = this.livros.filter(l => l.id !== id);
          this.successMessage = 'Livro excluído com sucesso!';
        },
        error: (error) => {
          this.errorMessage = 'Erro ao excluir livro: ' + error.message;
        }
      });
    }
  }
  cancelForm() {
    this.showForm = false;
    this.editingLivro = null;
    this.currentLivro = { titulo: '', autor: '', isbn: '', categoria: '' };
  }
}
