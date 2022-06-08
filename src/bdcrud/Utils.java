package bdcrud;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Utils {
	
	static Scanner sc = new Scanner(System.in);
	
	public static Connection conectar() {
		String CLASSE_DRIVER = "com.mysql.cj.jdbc.Driver";
		String USUARIO = "EduardoBorges";
		String SENHA = "1234";
		String URL_SERVIDOR = "jdbc:mysql://localhost:3306/jmysql?useSSL=false";
		
		try {
			Class.forName(CLASSE_DRIVER);
			return DriverManager.getConnection(URL_SERVIDOR, USUARIO, SENHA); 
		}catch(Exception e) {
			if(e instanceof ClassNotFoundException) {
				System.err.println("Verifique o driver de conexão.");
			} else {
				System.err.println("Verifique se o servidor está ativo.");
			}
			System.exit(-1);
			return null;
		}
	}
	
	public static void desconectar(Connection conn) {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("Não foi possivel fechar a conexão.");
				e.printStackTrace();
			}
		}
	}
	
	public static void listar() {
		String BUSCAR_TODOS = "SELECT * FROM produtos";
		
		try {
			Connection conn = conectar();
			 PreparedStatement produtos = conn.prepareStatement(BUSCAR_TODOS);
			 ResultSet res = produtos.executeQuery();
			 
			 res.last();
			 int quantidade = res.getRow();
			 res.beforeFirst();
			 
			 if(quantidade > 0) {
				 System.out.println("Listando produtos...");
				 System.out.println("--------------------");
				
				 while(res.next()) {
					 System.out.println("ID: " + res.getInt(1));
					 System.out.println("Produto: " + res.getInt(2));
					 System.out.println("Preço: " + res.getInt(3));
					 System.out.println("Estoque: " + res.getInt(4));
					 System.out.println("--------------------");
				 }
				 
			 }else {
				 System.out.println("Não existem produtos cadastrados.");
			 }
		} catch(Exception e) {
			e.printStackTrace();
			System.err.println("Erro buscando produtos.");
			System.exit(-1);
		}
	}
	
	public static void inserir() {
		System.out.print("Informe o nome do produto: ");
		String nome = sc.nextLine();
		
		System.out.print("Informe o preço do produto: ");
		double preco = sc.nextDouble();
		
		System.out.print("Informe a quantidade em estoque: ");
		int estoque = sc.nextInt();
		
		String INSERIR = "INSERT INTO produtos (nome, preco, estoque) VALUES (?, ?, ?)";
		
		try {
			Connection conn = conectar();
			PreparedStatement salvar = conn.prepareStatement(INSERIR);
			
			salvar.setString(1, nome);
			salvar.setDouble(2, preco);
			salvar.setInt(3, estoque);
			
			salvar.executeUpdate();
			salvar.close();
			
			desconectar(conn);
			System.out.println("O produtos " + nome + " foi inserido com sucesso.");
		} catch(Exception e) {
			e.printStackTrace();
			System.err.println("Erro salvando produto.");
			System.exit(-1);
		}
	}
	
	public static void atualizar() {
		System.out.print("Informe o código do produto: ");
		int id = Integer.parseInt(sc.nextLine());
		
		String BUSCAR_POR_ID = "SELECT * FROM produtos WHERE id = ?";
		
		try {
			Connection conn = conectar();
			PreparedStatement produto = conn.prepareStatement(BUSCAR_POR_ID);
			produto.setInt(1, id);
			ResultSet res = produto.executeQuery();
			
			res.last();
			int quantidade = res.getRow();
			res.beforeFirst();
			
			if(quantidade > 0) {
				System.out.print("Informe o nome do produto: ");
				String nome = sc.nextLine();
				
				System.out.print("Informe o preço do protudo: ");
				double preco = sc.nextDouble();
				
				System.out.print("Informe a quantidade em estoque: ");
				int estoque = sc.nextInt();
				
				String ATUALIZAR = "UPDATE produto SET nome = ?, preco = ?, estoque = ? WHERE id = ?";
				PreparedStatement update = conn.prepareStatement(ATUALIZAR);
				
				update.setString(1, nome);
				update.setDouble(2, preco);
				update.setInt(3, estoque);
				update.setInt(4, id);
				
				update.executeQuery();
				update.close();
				desconectar(conn);
				System.out.println("O produto " + nome + " foi atualizado com sucesso!");
			}else {
				System.out.println("Não existe produto com o id informado.");
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println("Erro ao atualizar produto.");
			System.exit(-1);
		}
	}
	
	public static void deletar() {
		String DELETAR = "DELETE FROM produtos WHERE id = ?";
		String BUSCAR_POR_ID = "SELECT * FROM produtos WHERE id = ?";
		
		System.out.print("Informe o código do produto: ");
		int id = Integer.parseInt(sc.nextLine());
		
		try {
			Connection conn = conectar();
			PreparedStatement produto = conn.prepareStatement(BUSCAR_POR_ID);
			produto.setInt(1, id);
			ResultSet res = produto.executeQuery();
			
			res.last();
			int quantidade = res.getRow();
			res.beforeFirst();
			
			if(quantidade > 0) {
				PreparedStatement deletar = conn.prepareStatement(DELETAR);
				deletar.setInt(1, id);
				deletar.executeQuery();
				deletar.close();
				desconectar(conn);
				System.out.println("O produto foi deletado com sucesso.");
			}else {
				System.out.println("Não existe o produto com o id informado.");
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println("Erro ao deletar produto.");
			System.exit(-1);
		}
	}
	
	public static void menu() {
		System.out.println("========== Gerenciamento de Produtos ==========");
		System.out.println("Selecione uma opção: ");
		System.out.println("1 - Listar produtos.");
		System.out.println("2 - Inserir produtos.");
		System.out.println("3 - Atualizar produtos.");
		System.out.println("4 - Deletar produtos.");
		
		int opcao = Integer.parseInt(sc.nextLine());
		if(opcao == 1) {
			listar();
		}else if(opcao == 2) {
			inserir();
		}else if(opcao == 3) {
			atualizar();
		}else if(opcao == 4) {
			deletar();
		}else {
			System.out.println("Opção inválida.");
		}
	}

}
