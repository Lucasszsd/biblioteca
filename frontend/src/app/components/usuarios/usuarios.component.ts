import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { Usuario } from '../../models/usuario.model';
@Component({
  selector: 'app-usuarios',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="row">
      <div class="col-12">
        <div class="d-flex justify-content-between align-items-center mb-4">
          <h2>
            <i class="fas fa-users me-2"></i>
            Gestão de Usuários
          </h2>
          <button class="btn btn-primary" (click)="showForm = true; editingUser = null">
            <i class="fas fa-plus me-1"></i>
            Novo Usuário
          </button>
        </div>
      </div>
    </div>
    <!-- Formulário -->
    <div class="row mb-4" *ngIf="showForm">
      <div class="col-12">
        <div class="card">
          <div class="card-header">
            <h5 class="mb-0">
              <i class="fas fa-user me-2"></i>
              {{ editingUser ? 'Editar' : 'Novo' }} Usuário
            </h5>
          </div>
          <div class="card-body">
            <form (ngSubmit)="saveUser()" #userForm="ngForm">
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label for="nome" class="form-label">Nome *</label>
                  <input 
                    type="text" 
                    class="form-control" 
                    id="nome" 
                    name="nome"
                    [(ngModel)]="currentUser.nome" 
                    required
                    #nome="ngModel">
                  <div class="invalid-feedback" *ngIf="nome.invalid && nome.touched">
                    Nome é obrigatório.
                  </div>
                </div>
                <div class="col-md-6 mb-3">
                  <label for="email" class="form-label">Email *</label>
                  <input 
                    type="email" 
                    class="form-control" 
                    id="email" 
                    name="email"
                    [(ngModel)]="currentUser.email" 
                    required
                    #email="ngModel">
                  <div class="invalid-feedback" *ngIf="email.invalid && email.touched">
                    Email válido é obrigatório.
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label for="telefone" class="form-label">Telefone *</label>
                  <input 
                    type="text" 
                    class="form-control" 
                    id="telefone" 
                    name="telefone"
                    [(ngModel)]="currentUser.telefone" 
                    required
                    #telefone="ngModel">
                  <div class="invalid-feedback" *ngIf="telefone.invalid && telefone.touched">
                    Telefone é obrigatório.
                  </div>
                </div>
              </div>
              <div class="d-flex gap-2">
                <button type="submit" class="btn btn-primary" [disabled]="userForm.invalid">
                  <i class="fas fa-save me-1"></i>
                  {{ editingUser ? 'Atualizar' : 'Salvar' }}
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
            <h5 class="mb-0">Lista de Usuários</h5>
          </div>
          <div class="card-body">
            <div class="table-responsive">
              <table class="table table-striped table-hover">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Nome</th>
                    <th>Email</th>
                    <th>Telefone</th>
                    <th>Data Cadastro</th>
                    <th>Ações</th>
                  </tr>
                </thead>
                <tbody>
                  <tr *ngFor="let usuario of usuarios">
                    <td>{{ usuario.id }}</td>
                    <td>{{ usuario.nome }}</td>
                    <td>{{ usuario.email }}</td>
                    <td>{{ usuario.telefone }}</td>
                    <td>{{ usuario.dataCadastro | date:'dd/MM/yyyy' }}</td>
                    <td>
                      <div class="btn-group" role="group">
                        <button class="btn btn-sm btn-outline-primary" (click)="editUser(usuario)">
                          <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-danger" (click)="deleteUser(usuario.id!)">
                          <i class="fas fa-trash"></i>
                        </button>
                      </div>
                    </td>
                  </tr>
                  <tr *ngIf="usuarios.length === 0">
                    <td colspan="6" class="text-center">Nenhum usuário encontrado.</td>
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
export class UsuariosComponent implements OnInit {
  usuarios: Usuario[] = [];
  currentUser: Usuario = { nome: '', email: '', telefone: '' };
  editingUser: Usuario | null = null;
  showForm = false;
  successMessage = '';
  errorMessage = '';
  constructor(private apiService: ApiService) {}
  ngOnInit() {
    this.loadUsuarios();
  }
  loadUsuarios() {
    this.apiService.get<Usuario[]>('/usuarios').subscribe({
      next: (data) => {
        this.usuarios = data;
      },
      error: (error) => {
        this.errorMessage = 'Erro ao carregar usuários: ' + error.message;
      }
    });
  }
  saveUser() {
    if (this.editingUser) {
      this.apiService.put<Usuario>(`/usuarios/${this.editingUser.id}`, this.currentUser).subscribe({
        next: (data) => {
        console.log(data)
          const index = this.usuarios.findIndex(u => u.id === this.editingUser!.id);
          if (index !== -1) {
            this.usuarios[index] = data;
          }
          this.successMessage = 'Usuário atualizado com sucesso!';
          this.cancelForm();
        },
        error: (error) => {
          this.errorMessage = 'Erro ao atualizar usuário: ' + error.message;
        }
      });
    } else {
      this.apiService.post<Usuario>('/usuarios', this.currentUser).subscribe({
        next: (data) => {
          this.usuarios.push(data);
          this.successMessage = 'Usuário criado com sucesso!';
          this.cancelForm();
        },
        error: (error) => {
          this.errorMessage = 'Erro ao criar usuário: ' + error.message;
        }
      });
    }
  }
  editUser(usuario: Usuario) {
    this.editingUser = usuario;
    this.currentUser = { ...usuario };
    this.showForm = true;
  }
  deleteUser(id: number) {
    if (confirm('Tem certeza que deseja excluir este usuário?')) {
      this.apiService.delete(`/usuarios/${id}`).subscribe({
        next: () => {
          this.usuarios = this.usuarios.filter(u => u.id !== id);
          this.successMessage = 'Usuário excluído com sucesso!';
        },
        error: (error) => {
          this.errorMessage = 'Erro ao excluir usuário: ' + error.message;
        }
      });
    }
  }
  cancelForm() {
    this.showForm = false;
    this.editingUser = null;
    this.currentUser = { nome: '', email: '', telefone: '' };
  }
}
