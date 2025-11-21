import java.util.Scanner;

public class SistemaVendas {

	//Colunas - convenção de indices
	public static final int PRD_ID = 0;
	public static final int PRD_NOME = 1;
	public static final int PRD_PRECO_COMPRA = 2;
	public static final int PRD_MARGEM = 3;
	public static final int PRD_ESTOQUE = 4; //coluna extra caso a gente coloque estoque
	
	//Matrizes globais (tamanhos conforme solicita no trabalho com 1+ colunas)
	public static String Clientes[][] = new String[5][3];
	public static String Vendedores[][] = new String[5][5];
	public static String Produtos[][] = new String[5][5];
	public static String Vendas[][] = new String[5][7];
	public static String VendasItens[][] = new String[25][6];
	public static String Comissoes[][] = new String[25][6];
	public static String Salarios[][] = new String[5][4];
	
	public static Scanner sc = new Scanner(System.in);
	
	public static void main(String[]args) {
		//para fins de teste, podemos colocar um produto
		seedExampleProducts();
		
	}
	
	public static void menuProdutos() {
		while (true) {
			System.out.println("\n---MENU PRODUTOS---");
			System.out.println("1 - INSERIR DADOS DO PRODUTO");
			System.out.println("2 - ALTERAR DADOS DO PRODUTO");
			System.out.println("3 - EXCLUIR DADOS DO PRODUTO");
			System.out.println("4 - IMPRIMIR FICHA DO PRUDOTO");
			System.out.println("0 - RETORNAR");
			System.out.println("Opção: ");
			String op = sc.nextLine();
			switch(op) {
			 case "1":
		            inserirProduto();
		            break;
		        case "2":
		            alterarProduto();
		            break;
		        case "3":
		            excluirProduto();
		            break;
		        case "4":
		            imprimirFichaProduto();
		            break;
		        case "0":
		            return;
		        default:
		            System.out.println("Opção inválida.");
			
			}
			
		}
	}
	
	// Inserir Produto
	public static void inserirProduto() {
		int slot = findFreeProductSlot ();
		if (slot == -1) {
			System.out.println("Erro: não há espaço para novos produtos (matriz cheia).");
			return;
		}
		
		int novoId = generateNextProductId();
		System.out.println("Inserindo produto. ID gerado: " + novoId);
		
		System.out.print("Nome: ");
		String nome = sc.nextLine();
		
		String precoStr;
		while (true) {
			System.out.print("Preço de compra (ex: 5.50): ");
			precoStr = sc.nextLine();
			try {
				Double.parseDouble(precoStr.replace(",", ","));
				break;
				
			} catch (Exception e) {
				System.out.print("Valor inválido. Tente novamente.");
			}
		}
		
		String margemStr;
		while(true) {
			System.out.print("Margem (%) (ex: 50 ou 25.5): ");
			margemStr = sc.nextLine();
			try {
				Double.parseDouble(margemStr.replace(",", ","));
				break;
			} catch (Exception e) {
				System.out.println("Valor inválido. Tente novamente.");
			}
			
		}
		
		String estoqueStr; 
		while (true) {
			System.out.print("Estoque (quantidade inteira): ");
			estoqueStr = sc.nextLine();
			try {
				Integer.parseInt(estoqueStr);
				break;
			} catch (Exception e) {
				System.out.println("Valor inválido. Tente novamente. ");
			}
		}
		
		Produtos[slot][PRD_ID] = String.valueOf(novoId);
		Produtos[slot][PRD_NOME] = nome;
		Produtos[slot][PRD_PRECO_COMPRA] = precoStr;
		Produtos[slot][PRD_MARGEM] = margemStr;
		Produtos[slot][PRD_ESTOQUE] = estoqueStr;
		
		System.out.println("Produto inserido com sucesso (ID " + novoId + ").");
	}
	
	//Alterar produto
	
	public static void alterarProduto() {
		System.out.print("Informe o ID do produto a alterar: ");
		String id = sc.nextLine();
		int idx = findProductIndexById(id);
		if (idx == -1) {
			System.out.print("Produto não contrado.");
			return;
			
		}
		if (hasProductSales(id)) {
			System.out.println("Alteração proibida: Produto possui vendas registradas.");
			return;
			
		}
		System.out.println("Dados atuais: ");
		printProductLine(idx);
		
		System.out.println("Digite novos valores . Deixe em branco e pressione ENTER para manter o atual.");
		System.out.print("Nome [" + Produtos[idx][PRD_NOME] + "]: ");
        String nome = sc.nextLine();
        if (nome != null && !nome.trim().isEmpty()) Produtos[idx][PRD_NOME] = nome;

        System.out.print("Preço de compra [" + Produtos[idx][PRD_PRECO_COMPRA] + "]: ");
        String preco = sc.nextLine();
        if (preco != null && !preco.trim().isEmpty()) {
            try {
                Double.parseDouble(preco.replace(",", "."));
                Produtos[idx][PRD_PRECO_COMPRA] = preco;
            } catch (Exception e) {
                System.out.println("Preço inválido. Mantendo anterior.");
            }
        }

        System.out.print("Margem [" + Produtos[idx][PRD_MARGEM] + "]: ");
        String margem = sc.nextLine();
        if (margem != null && !margem.trim().isEmpty()) {
            try {
                Double.parseDouble(margem.replace(",", "."));
                Produtos[idx][PRD_MARGEM] = margem;
            } catch (Exception e) {
                System.out.println("Margem inválida. Mantendo anterior.");
            }
        }

        System.out.print("Estoque [" + Produtos[idx][PRD_ESTOQUE] + "]: ");
        String estoque = sc.nextLine();
        if (estoque != null && !estoque.trim().isEmpty()) {
            try {
                Integer.parseInt(estoque);
                Produtos[idx][PRD_ESTOQUE] = estoque;
            } catch (Exception e) {
                System.out.println("Estoque inválido. Mantendo anterior.");
            }
        }

        System.out.println("Produto atualizado.");
    }

    // Excluir produto
    public static void excluirProduto() {
        System.out.print("Informe o ID do produto a excluir: ");
        String id = sc.nextLine();
        int idx = findProductIndexById(id);
        if (idx == -1) {
            System.out.println("Produto não encontrado.");
            return;
        }
        if (hasProductSales(id)) {
            System.out.println("Exclusão proibida: produto possui vendas registradas.");
            return;
        }
        System.out.println("Dados do produto:");
        printProductLine(idx);
        System.out.print("Confirma exclusão? (S/N): ");
        String resp = sc.nextLine().trim().toUpperCase();
        if (resp.equals("S")) {
            for (int c = 0; c < Produtos[idx].length; c++) Produtos[idx][c] = null;
            System.out.println("Produto excluído.");
        } else {
            System.out.println("Exclusão cancelada.");
        }
    }

    // Imprimir ficha
    public static void imprimirFichaProduto() {
        System.out.print("Informe o ID do produto a imprimir: ");
        String id = sc.nextLine();
        int idx = findProductIndexById(id);
        if (idx == -1) {
            System.out.println("Produto não encontrado.");
            return;
        }
        System.out.println("=== FICHA DO PRODUTO ===");
        printProductLine(idx);
        System.out.print("Imprimir fisicamente? (S/N) (nota: não implementado fisicamente): ");
        String r = sc.nextLine().trim().toUpperCase();
        if (r.equals("S")) {
            System.out.println("(Simulação) Enviando para impressora... [não implementado]");
        } else {
            System.out.println("Impressão em tela finalizada.");
        }
    }

    // Helpers
    public static int generateNextProductId() {
        int max = 0;
        for (int i = 0; i < Produtos.length; i++) {
            String val = Produtos[i][PRD_ID];
            if (val != null && !val.trim().isEmpty()) {
                try {
                    int id = Integer.parseInt(val);
                    if (id > max) max = id;
                } catch (Exception e) {}
            }
        }
        return max + 1;
    }

    public static int findFreeProductSlot() {
        for (int i = 0; i < Produtos.length; i++) {
        	if (Produtos[i][PRD_ID] == null || Produtos[i][PRD_ID].trim().isEmpty()) return i;
        }
        return -1;
    }

    public static int findProductIndexById(String id) {
        for (int i = 0; i < Produtos.length; i++) {
            if (Produtos[i][PRD_ID] != null && Produtos[i][PRD_ID].equals(id)) return i;
        }
        return -1;
    }

    // Verifica se há vendas relacionadas ao PRD_ID na matriz VendasItens
    public static boolean hasProductSales(String prdId) {
        for (int i = 0; i < VendasItens.length; i++) {
            String vPrd = VendasItens[i][2]; // VDI_PRD_ID conforme especificação (coluna 2 neste exemplo)
            if (vPrd != null && vPrd.equals(prdId)) return true;
        }
        return false;
    }

    public static void printProductLine(int idx) {
        System.out.println("ID: " + Produtos[idx][PRD_ID]);
        System.out.println("Nome: " + Produtos[idx][PRD_NOME]);
        System.out.println("Preço compra: " + Produtos[idx][PRD_PRECO_COMPRA]);
        System.out.println("Margem (%): " + Produtos[idx][PRD_MARGEM]);
        System.out.println("Estoque: " + Produtos[idx][PRD_ESTOQUE]);
    }

    // Método para popular alguns produtos para teste
    public static void seedExampleProducts() {
        Produtos[0][PRD_ID] = "1";
        Produtos[0][PRD_NOME] = "Coca-Cola 2L";
        Produtos[0][PRD_PRECO_COMPRA] = "5.50";
        Produtos[0][PRD_MARGEM] = "50.0";
        Produtos[0][PRD_ESTOQUE] = "10";

        Produtos[1][PRD_ID] = "2";
        Produtos[1][PRD_NOME] = "Chocolate";
        Produtos[1][PRD_PRECO_COMPRA] = "3.00";
        Produtos[1][PRD_MARGEM] = "40.0";
       Produtos[1][PRD_ESTOQUE] = "5";
		
	}
	
}
