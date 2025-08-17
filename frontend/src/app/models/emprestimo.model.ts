export interface Emprestimo {
  id?: number;
  usuarioId: number;
  usuarioNome?: string;
  livroId: number;
  livroTitulo?: string;
  dataEmprestimo?: string;
  dataDevolucao?: string;
  status?: string;
}
