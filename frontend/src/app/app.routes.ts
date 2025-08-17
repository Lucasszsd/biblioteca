import { Routes } from '@angular/router';
import { UsuariosComponent } from './components/usuarios/usuarios.component';
import { LivrosComponent } from './components/livros/livros.component';
import { EmprestimosComponent } from './components/emprestimos/emprestimos.component';
import { HomeComponent } from './components/home/home.component';
export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'usuarios', component: UsuariosComponent },
  { path: 'livros', component: LivrosComponent },
  { path: 'emprestimos', component: EmprestimosComponent },
  { path: '**', redirectTo: '' }
];
