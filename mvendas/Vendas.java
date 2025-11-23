package mvendas;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Vendas {

    // Colunas - convenção de indices (Produtos já existentes)
    public static final int PRD_ID = 0;
    public static final int PRD_NOME = 1;
    public static final int PRD_PRECO_COMPRA = 2;
    public static final int PRD_MARGEM = 3;
    public static final int PRD_ESTOQUE = 4; // coluna extra caso a gente coloque estoque

    // Colunas Clientes
    public static final int CLI_ID = 0;
    public static final int CLI_NOME = 1;
    public static final int CLI_TELEFONE = 2;

    // Colunas Vendedores
    public static final int VND_ID = 0;
    public static final int VND_NOME = 1;
    public static final int VND_TELEFONE = 2;
    public static final int VND_SALARIO = 3;
    public static final int VND_COMISSAO = 4;

    // Colunas Vendas (conforme PDF)
    public static final int VDA_ID = 0;
    public static final int VDA_CLI_ID = 1;
    public static final int VDA_VND_ID = 2;
    public static final int VDA_VND_COMISSAO = 3;
    public static final int VDA_DATA = 4;
    public static final int VDA_CANCELADA = 5;
    public static final int VDA_IMPRIMIU = 6;

    // Colunas VendasItens (conforme PDF)
    public static final int VDI_ID = 0;
    public static final int VDI_VDA_ID = 1;
    public static final int VDI_PRD_ID = 2;
    public static final int VDI_VL_UNITARIO = 3;
    public static final int VDI_QTD = 4;
    public static final int VDI_VL_TOTAL = 5;

    // Colunas Comissoes (definidas agora)
    public static final int CMI_ID = 0;
    public static final int CMI_VDI_ID = 1;
    public static final int CMI_PRD_ID = 2;
    public static final int CMI_VND_ID = 3;
    public static final int CMI_VL_COMISSAO = 4;
    public static final int CMI_VL_COMISSAO2 = 5;

    // Matrizes globais (tamanhos conforme solicita no trabalho com 1+ colunas)
    public static String Clientes[][] = new String[5][3];
    public static String Vendedores[][] = new String[5][5];
    public static String Produtos[][] = new String[5][5];
    public static String Vendas[][] = new String[5][7];
    public static String VendasItens[][] = new String[25][6];
    public static String Comissoes[][] = new String[25][6];
    public static String Salarios[][] = new String[5][4];

    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // para fins de teste, podemos colocar um produto
        seedExampleProducts();

        // exemplo seeds mínimos para clientes e vendedores (opcional, útil para testes)
        Clientes[0][CLI_ID] = "1";
        Clientes[0][CLI_NOME] = "João Silva";
        Clientes[0][CLI_TELEFONE] = "999999999";

        Vendedores[0][VND_ID] = "1";
        Vendedores[0][VND_NOME] = "Pedro Souza";
        Vendedores[0][VND_TELEFONE] = "999888777";
        Vendedores[0][VND_SALARIO] = "1200.00";
        Vendedores[0][VND_COMISSAO] = "5.0";

        // Menu principal
        while (true) {
            System.out.println("+==================================+");
            System.out.println("|        >MENU PRINCIPAL<          |");
            System.out.println("|==================================|");
            System.out.println("1 - PRODUTOS                       |");
            System.out.println("2 - CLIENTES                       |");
            System.out.println("3 - VENDEDORES                     |");
            System.out.println("4 - VENDAS                         |");
            System.out.println("5 - RELATÓRIOS                     |");
            System.out.println("0 - SAIR                           |");
            System.out.println("|---------------------------------->>>>>");
            System.out.print("Opção: ");
            String op = sc.nextLine();
            switch (op) {
                case "1":
                    menuProdutos();
                    break;
                case "2":
                    menuClientes();
                    break;
                case "3":
                    menuVendedores();
                    break;
                case "4":
                    menuVendas(); // agora chama o menu de vendas implementado
                    break;
                case "5":
                    System.out.println("Menu Relatórios (a implementar).");
                    break;
                case "0":
                    System.out.println("Encerrando...");
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    /***********************
     * MENU PRODUTOS (SEU CÓDIGO ORIGINAL)
     ************************/
    public static void menuProdutos() {
        while (true) {
            System.out.println("\n---MENU PRODUTOS---");
            System.out.println("1 - INSERIR DADOS DO PRODUTO");
            System.out.println("2 - ALTERAR DADOS DO PRODUTO");
            System.out.println("3 - EXCLUIR DADOS DO PRODUTO");
            System.out.println("4 - IMPRIMIR FICHA DO PRUDOTO");
            System.out.println("0 - RETORNAR");
            System.out.print("Opção: ");
            String op = sc.nextLine();
            switch (op) {
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
        int slot = findFreeProductSlot();
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
                Double.parseDouble(precoStr.replace(",", "."));
                break;
            } catch (Exception e) {
                System.out.println("Valor inválido. Tente novamente.");
            }
        }

        String margemStr;
        while (true) {
            System.out.print("Margem (%) (ex: 50 ou 25.5): ");
            margemStr = sc.nextLine();
            try {
                Double.parseDouble(margemStr.replace(",", "."));
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

    // Alterar produto
    public static void alterarProduto() {
        System.out.print("Informe o ID do produto a alterar: ");
        String id = sc.nextLine();
        int idx = findProductIndexById(id);
        if (idx == -1) {
            System.out.println("Produto não encontrado.");
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

    // Imprimir ficha produto
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

    /***********************
     * MENU CLIENTES (NOVO)
     ************************/
    public static void menuClientes() {
        while (true) {
            System.out.println("\n---MENU CLIENTES---");
            System.out.println("1 - INSERIR DADOS DO CLIENTE");
            System.out.println("2 - ALTERAR DADOS DO CLIENTE");
            System.out.println("3 - EXCLUIR DADOS DO CLIENTE");
            System.out.println("4 - IMPRIMIR FICHA DO CLIENTE");
            System.out.println("0 - RETORNAR");
            System.out.print("Opção: ");
            String op = sc.nextLine();
            switch (op) {
                case "1":
                    inserirCliente();
                    break;
                case "2":
                    alterarCliente();
                    break;
                case "3":
                    excluirCliente();
                    break;
                case "4":
                    imprimirFichaCliente();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    public static void inserirCliente() {
        int slot = findFreeClientSlot();
        if (slot == -1) {
            System.out.println("Erro: não há espaço para novos clientes (matriz cheia).");
            return;
        }

        int novoId = generateNextClientId();
        System.out.println("Inserindo cliente. ID gerado: " + novoId);

        System.out.print("Nome: ");
        String nome = sc.nextLine();

        String telefone;
        while (true) {
            System.out.print("Telefone (somente números): ");
            telefone = sc.nextLine();
            if (telefone.matches("\\d+")) break;
            System.out.println("Telefone inválido. Digite apenas números.");
        }

        Clientes[slot][CLI_ID] = String.valueOf(novoId);
        Clientes[slot][CLI_NOME] = nome;
        Clientes[slot][CLI_TELEFONE] = telefone;

        System.out.println("Cliente inserido com sucesso (ID " + novoId + ").");
    }

    public static void alterarCliente() {
        System.out.print("Informe o ID do cliente a alterar: ");
        String id = sc.nextLine();
        int idx = findClientIndexById(id);
        if (idx == -1) {
            System.out.println("Cliente não encontrado.");
            return;
        }
        if (hasClientSales(id)) {
            System.out.println("Alteração proibida: Cliente possui vendas registradas.");
            return;
        }
        System.out.println("Dados atuais:");
        printClientLine(idx);

        System.out.println("Digite novos valores. Deixe em branco e pressione ENTER para manter o atual.");
        System.out.print("Nome [" + Clientes[idx][CLI_NOME] + "]: ");
        String nome = sc.nextLine();
        if (nome != null && !nome.trim().isEmpty()) Clientes[idx][CLI_NOME] = nome;

        System.out.print("Telefone [" + Clientes[idx][CLI_TELEFONE] + "]: ");
        String tel = sc.nextLine();
        if (tel != null && !tel.trim().isEmpty()) {
            if (tel.matches("\\d+")) {
                Clientes[idx][CLI_TELEFONE] = tel;
            } else {
                System.out.println("Telefone inválido. Mantendo anterior.");
            }
        }

        System.out.println("Cliente atualizado.");
    }

    public static void excluirCliente() {
        System.out.print("Informe o ID do cliente a excluir: ");
        String id = sc.nextLine();
        int idx = findClientIndexById(id);
        if (idx == -1) {
            System.out.println("Cliente não encontrado.");
            return;
        }
        if (hasClientSales(id)) {
            System.out.println("Exclusão proibida: cliente possui vendas registradas.");
            return;
        }
        System.out.println("Dados do cliente:");
        printClientLine(idx);
        System.out.print("Confirma exclusão? (S/N): ");
        String resp = sc.nextLine().trim().toUpperCase();
        if (resp.equals("S")) {
            for (int c = 0; c < Clientes[idx].length; c++) Clientes[idx][c] = null;
            System.out.println("Cliente excluído.");
        } else {
            System.out.println("Exclusão cancelada.");
        }
    }

    public static void imprimirFichaCliente() {
        System.out.print("Informe o ID do cliente a imprimir: ");
        String id = sc.nextLine();
        int idx = findClientIndexById(id);
        if (idx == -1) {
            System.out.println("Cliente não encontrado.");
            return;
        }
        System.out.println("=== FICHA DO CLIENTE ===");
        printClientLine(idx);
        System.out.print("Imprimir fisicamente? (S/N) (nota: não implementado fisicamente): ");
        String r = sc.nextLine().trim().toUpperCase();
        if (r.equals("S")) {
            System.out.println("(Simulação) Enviando para impressora... [não implementado]");
        } else {
            System.out.println("Impressão em tela finalizada.");
        }
    }

    /***********************
     * MENU VENDEDORES (NOVO)
     ************************/
    public static void menuVendedores() {
        while (true) {
            System.out.println("\n---MENU VENDEDORES---");
            System.out.println("1 - INSERIR DADOS DO VENDEDOR");
            System.out.println("2 - ALTERAR DADOS DO VENDEDOR");
            System.out.println("3 - EXCLUIR DADOS DO VENDEDOR");
            System.out.println("4 - IMPRIMIR FICHA DO VENDEDOR");
            System.out.println("0 - RETORNAR");
            System.out.print("Opção: ");
            String op = sc.nextLine();
            switch (op) {
                case "1":
                    inserirVendedor();
                    break;
                case "2":
                    alterarVendedor();
                    break;
                case "3":
                    excluirVendedor();
                    break;
                case "4":
                    imprimirFichaVendedor();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    public static void inserirVendedor() {
        int slot = findFreeVendorSlot();
        if (slot == -1) {
            System.out.println("Erro: não há espaço para novos vendedores (matriz cheia).");
            return;
        }

        int novoId = generateNextVendorId();
        System.out.println("Inserindo vendedor. ID gerado: " + novoId);

        System.out.print("Nome: ");
        String nome = sc.nextLine();

        String telefone;
        while (true) {
            System.out.print("Telefone (somente números): ");
            telefone = sc.nextLine();
            if (telefone.matches("\\d+")) break;
            System.out.println("Telefone inválido. Digite apenas números.");
        }

        String salarioStr;
        while (true) {
            System.out.print("Salário (ex: 1200.50): ");
            salarioStr = sc.nextLine();
            try {
                Double.parseDouble(salarioStr.replace(",", "."));
                break;
            } catch (Exception e) {
                System.out.println("Valor inválido. Tente novamente.");
            }
        }

        String comissaoStr;
        while (true) {
            System.out.print("Comissão (%) (ex: 5.0): ");
            comissaoStr = sc.nextLine();
            try {
                Double.parseDouble(comissaoStr.replace(",", "."));
                break;
            } catch (Exception e) {
                System.out.println("Valor inválido. Tente novamente.");
            }
        }

        Vendedores[slot][VND_ID] = String.valueOf(novoId);
        Vendedores[slot][VND_NOME] = nome;
        Vendedores[slot][VND_TELEFONE] = telefone;
        Vendedores[slot][VND_SALARIO] = salarioStr;
        Vendedores[slot][VND_COMISSAO] = comissaoStr;

        System.out.println("Vendedor inserido com sucesso (ID " + novoId + ").");
    }

    public static void alterarVendedor() {
        System.out.print("Informe o ID do vendedor a alterar: ");
        String id = sc.nextLine();
        int idx = findVendorIndexById(id);
        if (idx == -1) {
            System.out.println("Vendedor não encontrado.");
            return;
        }
        if (hasVendorSales(id)) {
            System.out.println("Alteração proibida: Vendedor possui vendas registradas.");
            return;
        }
        System.out.println("Dados atuais:");
        printVendorLine(idx);

        System.out.println("Digite novos valores. Deixe em branco e pressione ENTER para manter o atual.");
        System.out.print("Nome [" + Vendedores[idx][VND_NOME] + "]: ");
        String nome = sc.nextLine();
        if (nome != null && !nome.trim().isEmpty()) Vendedores[idx][VND_NOME] = nome;

        System.out.print("Telefone [" + Vendedores[idx][VND_TELEFONE] + "]: ");
        String tel = sc.nextLine();
        if (tel != null && !tel.trim().isEmpty()) {
            if (tel.matches("\\d+")) {
                Vendedores[idx][VND_TELEFONE] = tel;
            } else {
                System.out.println("Telefone inválido. Mantendo anterior.");
            }
        }

        System.out.print("Salário [" + Vendedores[idx][VND_SALARIO] + "]: ");
        String sal = sc.nextLine();
        if (sal != null && !sal.trim().isEmpty()) {
            try {
                Double.parseDouble(sal.replace(",", "."));
                Vendedores[idx][VND_SALARIO] = sal;
            } catch (Exception e) {
                System.out.println("Salário inválido. Mantendo anterior.");
            }
        }

        System.out.print("Comissão (%) [" + Vendedores[idx][VND_COMISSAO] + "]: ");
        String com = sc.nextLine();
        if (com != null && !com.trim().isEmpty()) {
            try {
                Double.parseDouble(com.replace(",", "."));
                Vendedores[idx][VND_COMISSAO] = com;
            } catch (Exception e) {
                System.out.println("Comissão inválida. Mantendo anterior.");
            }
        }

        System.out.println("Vendedor atualizado.");
    }

    public static void excluirVendedor() {
        System.out.print("Informe o ID do vendedor a excluir: ");
        String id = sc.nextLine();
        int idx = findVendorIndexById(id);
        if (idx == -1) {
            System.out.println("Vendedor não encontrado.");
            return;
        }
        if (hasVendorSales(id)) {
            System.out.println("Exclusão proibida: vendedor possui vendas registradas.");
            return;
        }
        System.out.println("Dados do vendedor:");
        printVendorLine(idx);
        System.out.print("Confirma exclusão? (S/N): ");
        String resp = sc.nextLine().trim().toUpperCase();
        if (resp.equals("S")) {
            for (int c = 0; c < Vendedores[idx].length; c++) Vendedores[idx][c] = null;
            System.out.println("Vendedor excluído.");
        } else {
            System.out.println("Exclusão cancelada.");
        }
    }

    public static void imprimirFichaVendedor() {
        System.out.print("Informe o ID do vendedor a imprimir: ");
        String id = sc.nextLine();
        int idx = findVendorIndexById(id);
        if (idx == -1) {
            System.out.println("Vendedor não encontrado.");
            return;
        }
        System.out.println("=== FICHA DO VENDEDOR ===");
        printVendorLine(idx);
        System.out.print("Imprimir fisicamente? (S/N) (nota: não implementado fisicamente): ");
        String r = sc.nextLine().trim().toUpperCase();
        if (r.equals("S")) {
            System.out.println("(Simulação) Enviando para impressora... [não implementado]");
        } else {
            System.out.println("Impressão em tela finalizada.");
        }
    }

    /***********************
     * MENU VENDAS (NOVO)
     ************************/
    public static void menuVendas() {
        while (true) {
            System.out.println("\n---MENU VENDAS---");
            System.out.println("1 - INSERIR NOVA VENDA");
            System.out.println("2 - CANCELAR VENDA");
            System.out.println("3 - IMPRIMIR PEDIDO DE VENDA");
            System.out.println("0 - RETORNAR");
            System.out.print("Opção: ");
            String op = sc.nextLine();
            switch (op) {
                case "1":
                    inserirVenda();
                    break;
                case "2":
                    cancelarVenda();
                    break;
                case "3":
                    imprimirPedidoVenda();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    // Inserir nova venda (cabeçalho) e iniciar cadastro de itens
    public static void inserirVenda() {
        int vendaSlot = findFreeVendaSlot();
        if (vendaSlot == -1) {
            System.out.println("Erro: não há espaço para novas vendas (matriz cheia).");
            return;
        }

        int novoVdaId = generateNextVendaId();
        System.out.println("Gerando nova venda. ID: " + novoVdaId);

        // Ler cliente
        System.out.print("Informe ID do cliente: ");
        String cliId = sc.nextLine();
        int cliIdx = findClientIndexById(cliId);
        if (cliIdx == -1) {
            System.out.println("Cliente não encontrado. Aborting venda.");
            return;
        }
        System.out.println("Cliente: " + Clientes[cliIdx][CLI_NOME]);

        // Ler vendedor
        System.out.print("Informe ID do vendedor: ");
        String vndId = sc.nextLine();
        int vndIdx = findVendorIndexById(vndId);
        if (vndIdx == -1) {
            System.out.println("Vendedor não encontrado. Aborting venda.");
            return;
        }
        System.out.println("Vendedor: " + Vendedores[vndIdx][VND_NOME]);
        System.out.println("Comissão do vendedor: " + Vendedores[vndIdx][VND_COMISSAO] + "%");

        // Data
        LocalDateTime now = LocalDateTime.now();

        // Salvar a venda (apenas os campos exigidos)
        Vendas[vendaSlot][VDA_ID] = String.valueOf(novoVdaId);
        Vendas[vendaSlot][VDA_CLI_ID] = cliId;
        Vendas[vendaSlot][VDA_VND_ID] = vndId;
        Vendas[vendaSlot][VDA_VND_COMISSAO] = Vendedores[vndIdx][VND_COMISSAO]; // grava o percentual vigente
        Vendas[vendaSlot][VDA_DATA] = now.toString();
        Vendas[vendaSlot][VDA_CANCELADA] = "N";
        Vendas[vendaSlot][VDA_IMPRIMIU] = "N";

        System.out.println("Venda criada com sucesso (ID " + novoVdaId + "). Iniciando lançamento de itens...");
        inserirItensVenda(novoVdaId, vndId);
    }

    // Inserir itens de uma venda específica
    public static void inserirItensVenda(int vdaId, String vndId) {
        double totalPedido = 0.0;
        int itemOrdinal = 1;

        while (true) {
            System.out.print("Informe ID do produto (ou 0 para finalizar pedido): ");
            String prdId = sc.nextLine();
            if (prdId.equals("0")) {
                // finalizar: se nenhum item inserido, podemos cancelar a venda (mantive como comportamento simples),
                // mas aqui apenas finalizamos o lançamento
                break;
            }

            int prdIdx = findProductIndexById(prdId);
            if (prdIdx == -1) {
                System.out.println("Produto não encontrado.");
                continue;
            }

            // verificar estoque
            String estoqueStr = Produtos[prdIdx][PRD_ESTOQUE];
            int estoque = 0;
            try {
                estoque = Integer.parseInt(estoqueStr);
            } catch (Exception e) {
                System.out.println("Estoque inválido no cadastro do produto. Operação cancelada para este item.");
                continue;
            }
            if (estoque <= 0) {
                System.out.println("Produto sem estoque disponível. Não é possível vender.");
                continue;
            }

            // calcular preço de venda: precoCompra * (1 + margem/100)
            double precoCompra = Double.parseDouble(Produtos[prdIdx][PRD_PRECO_COMPRA].replace(",", "."));
            double margem = Double.parseDouble(Produtos[prdIdx][PRD_MARGEM].replace(",", "."));
            double precoVenda = precoCompra * (1.0 + (margem / 100.0));
            // formatagem será feita quando mostrar

            System.out.println("Produto: " + Produtos[prdIdx][PRD_NOME]);
            System.out.println("Preço unitário calculado: " + String.format("%.2f", precoVenda));
            System.out.println("Estoque disponível: " + estoque);

            // ler quantidade
            int qtd = 0;
            while (true) {
                System.out.print("Quantidade: ");
                String qtdStr = sc.nextLine();
                try {
                    qtd = Integer.parseInt(qtdStr);
                    if (qtd <= 0) {
                        System.out.println("Quantidade deve ser maior que zero.");
                        continue;
                    }
                    if (qtd > estoque) {
                        System.out.println("Quantidade maior que estoque disponível.");
                        continue;
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("Quantidade inválida. Digite um número inteiro.");
                }
            }

            double totalItem = precoVenda * qtd;
            System.out.println("Total do item: " + String.format("%.2f", totalItem));

            // confirmar inclusão do item
            System.out.print("Confirmar inclusão do item? (S/N): ");
            String confirma = sc.nextLine().trim().toUpperCase();
            if (!confirma.equals("S")) {
                System.out.println("Item não incluído.");
            } else {
                // gerar VDI_ID e salvar em VendasItens
                int slotItem = findFreeVendaItemSlot();
                if (slotItem == -1) {
                    System.out.println("Erro: não há espaço para novos itens da venda (matriz cheia).");
                    return;
                }
                int novoVdiId = generateNextVendaItemId();

                VendasItens[slotItem][VDI_ID] = String.valueOf(novoVdiId);
                VendasItens[slotItem][VDI_VDA_ID] = String.valueOf(vdaId);
                VendasItens[slotItem][VDI_PRD_ID] = prdId;
                VendasItens[slotItem][VDI_VL_UNITARIO] = String.format("%.2f", precoVenda);
                VendasItens[slotItem][VDI_QTD] = String.valueOf(qtd);
                VendasItens[slotItem][VDI_VL_TOTAL] = String.format("%.2f", totalItem);

                // reduzir estoque
                int novoEstoque = estoque - qtd;
                Produtos[prdIdx][PRD_ESTOQUE] = String.valueOf(novoEstoque);

                // gerar comissão para o vendedor (usar percentual gravado na venda)
                // procurar venda para recuperar percentual (ou passar como parâmetro)
                int vdaIdx = findVendaIndexById(String.valueOf(vdaId));
                String percStr = (vdaIdx != -1) ? Vendas[vdaIdx][VDA_VND_COMISSAO] : null;
                double perc = 0.0;
                if (percStr != null) {
                    try {
                        perc = Double.parseDouble(percStr.replace(",", "."));
                    } catch (Exception e) {
                        perc = 0.0;
                    }
                }

                double valorComissao = (totalItem * perc) / 100.0;

                // inserir registro na tabela Comissoes
                int slotCom = findFreeCommissionSlot();
                if (slotCom == -1) {
                    System.out.println("Aviso: não há espaço para gravar comissão (tabela cheia). Comissão não registrada.");
                } else {
                    int novoCmiId = generateNextCommissionId();
                    Comissoes[slotCom][CMI_ID] = String.valueOf(novoCmiId);
                    Comissoes[slotCom][CMI_VDI_ID] = String.valueOf(novoVdiId);
                    Comissoes[slotCom][CMI_PRD_ID] = prdId;
                    Comissoes[slotCom][CMI_VND_ID] = vndId;
                    Comissoes[slotCom][CMI_VL_COMISSAO] = String.format("%.2f", valorComissao);
                    Comissoes[slotCom][CMI_VL_COMISSAO2] = String.format("%.2f", valorComissao); // guarda a cópia original
                }

                totalPedido += totalItem;
                System.out.println("Item incluído com sucesso (VDI_ID=" + novoVdiId + ").");
                itemOrdinal++;
            }

            // perguntar se quer continuar lançando items ou finalizar
            System.out.print("Deseja incluir mais itens? (S/N): ");
            String mais = sc.nextLine().trim().toUpperCase();
            if (!mais.equals("S")) {
                break;
            }
        }

        // Ao fechar o pedido, exibir resumo e finalizar
        System.out.println("=== RESUMO DO PEDIDO (Venda ID " + vdaId + ") ===");
        double soma = 0.0;
        for (int i = 0; i < VendasItens.length; i++) {
            String vv = VendasItens[i][VDI_VDA_ID];
            if (vv != null && vv.equals(String.valueOf(vdaId))) {
                System.out.println("Item ID: " + VendasItens[i][VDI_ID] +
                        " Produto ID: " + VendasItens[i][VDI_PRD_ID] +
                        " Unit: " + VendasItens[i][VDI_VL_UNITARIO] +
                        " Qtd: " + VendasItens[i][VDI_QTD] +
                        " Total: " + VendasItens[i][VDI_VL_TOTAL]);
                try {
                    soma += Double.parseDouble(VendasItens[i][VDI_VL_TOTAL].replace(",", "."));
                } catch (Exception e) {}
            }
        }

        System.out.println("TOTAL GERAL: " + String.format("%.2f", soma));
        System.out.println("Venda finalizada (na tela). Você pode imprimir o pedido em 'IMPRIMIR PEDIDO DE VENDA' no menu VENDAS.");
    }

    // Cancelar venda -- conforme regras: não cancelar se já tiver sido impressa; marcar VDA_Cancelada="S", devolver estoque e zerar CMI_VL_COMISSAO (mantendo CMI_VL_COMISSAO2)
    public static void cancelarVenda() {
        System.out.print("Informe ID da venda a cancelar: ");
        String vdaId = sc.nextLine();
        int vdaIdx = findVendaIndexById(vdaId);
        if (vdaIdx == -1) {
            System.out.println("Venda não encontrada.");
            return;
        }
        if ("S".equalsIgnoreCase(Vendas[vdaIdx][VDA_IMPRIMIU])) {
            System.out.println("Venda já foi impressa. Não é permitido cancelar (por segurança).");
            return;
        }
        if ("S".equalsIgnoreCase(Vendas[vdaIdx][VDA_CANCELADA])) {
            System.out.println("Venda já está cancelada.");
            return;
        }

        // Mostrar venda e itens
        System.out.println("=== DADOS DA VENDA ===");
        System.out.println("ID: " + Vendas[vdaIdx][VDA_ID]);
        System.out.println("Cliente ID: " + Vendas[vdaIdx][VDA_CLI_ID]);
        System.out.println("Vendedor ID: " + Vendas[vdaIdx][VDA_VND_ID]);
        System.out.println("Data: " + Vendas[vdaIdx][VDA_DATA]);
        System.out.println("Itens:");
        double soma = 0.0;
        for (int i = 0; i < VendasItens.length; i++) {
            String vv = VendasItens[i][VDI_VDA_ID];
            if (vv != null && vv.equals(Vendas[vdaIdx][VDA_ID])) {
                System.out.println(" Item VDI_ID: " + VendasItens[i][VDI_ID] +
                        " PRD_ID: " + VendasItens[i][VDI_PRD_ID] +
                        " Qtd: " + VendasItens[i][VDI_QTD] +
                        " Total: " + VendasItens[i][VDI_VL_TOTAL]);
                try {
                    soma += Double.parseDouble(VendasItens[i][VDI_VL_TOTAL].replace(",", "."));
                } catch (Exception e) {}
            }
        }
        System.out.println("Total: " + String.format("%.2f", soma));
        System.out.print("Confirma cancelamento desta venda? (S/N): ");
        String resp = sc.nextLine().trim().toUpperCase();
        if (!resp.equals("S")) {
            System.out.println("Cancelamento abortado.");
            return;
        }

        // Setar cancelada = "S"
        Vendas[vdaIdx][VDA_CANCELADA] = "S";

        // Para cada item da venda: devolver estoque e zerar comissão (CMI_VL_COMISSAO), mantendo CMI_VL_COMISSAO2
        for (int i = 0; i < VendasItens.length; i++) {
            String vv = VendasItens[i][VDI_VDA_ID];
            if (vv != null && vv.equals(Vendas[vdaIdx][VDA_ID])) {
                String prdId = VendasItens[i][VDI_PRD_ID];
                int qtd = 0;
                try {
                    qtd = Integer.parseInt(VendasItens[i][VDI_QTD]);
                } catch (Exception e) {}
                // achar produto e devolver estoque
                int prdIdx = findProductIndexById(prdId);
                if (prdIdx != -1) {
                    int estoqueAtual = 0;
                    try {
                        estoqueAtual = Integer.parseInt(Produtos[prdIdx][PRD_ESTOQUE]);
                    } catch (Exception e) {}
                    Produtos[prdIdx][PRD_ESTOQUE] = String.valueOf(estoqueAtual + qtd);
                }

                // procurar comissao(s) relacionadas ao VDI_ID
                String vdiId = VendasItens[i][VDI_ID];
                for (int j = 0; j < Comissoes.length; j++) {
                    if (Comissoes[j][CMI_VDI_ID] != null && Comissoes[j][CMI_VDI_ID].equals(vdiId)) {
                        // zerar CMI_VL_COMISSAO
                        Comissoes[j][CMI_VL_COMISSAO] = "0.00";
                        // manter CMI_VL_COMISSAO2 intacto
                    }
                }
            }
        }

        System.out.println("Venda cancelada com sucesso (VDA_ID=" + Vendas[vdaIdx][VDA_ID] + "). Estoques e comissões atualizados conforme regras.");
    }

    // Imprimir pedido de venda: exibe dados e marca VDA_Imprimiu = "S" se não cancelada
    public static void imprimirPedidoVenda() {
        System.out.print("Informe ID da venda para imprimir: ");
        String vdaId = sc.nextLine();
        int vdaIdx = findVendaIndexById(vdaId);
        if (vdaIdx == -1) {
            System.out.println("Venda não encontrada.");
            return;
        }
        if ("S".equalsIgnoreCase(Vendas[vdaIdx][VDA_CANCELADA])) {
            System.out.println("Venda está cancelada. Não é permitido imprimir.");
            return;
        }

        // Montar impressão
        System.out.println("=== PEDIDO DE VENDA ===");
        System.out.println("Venda ID: " + Vendas[vdaIdx][VDA_ID]);
        System.out.println("Data: " + Vendas[vdaIdx][VDA_DATA]);
        String cliId = Vendas[vdaIdx][VDA_CLI_ID];
        int cliIdx = findClientIndexById(cliId);
        if (cliIdx != -1) System.out.println("Cliente: " + Clientes[cliIdx][CLI_NOME] + " (ID " + cliId + ")");
        String vndId = Vendas[vdaIdx][VDA_VND_ID];
        int vndIdx = findVendorIndexById(vndId);
        if (vndIdx != -1) System.out.println("Vendedor: " + Vendedores[vndIdx][VND_NOME] + " (ID " + vndId + ")");
        System.out.println("Comissão registrada na venda (%): " + Vendas[vdaIdx][VDA_VND_COMISSAO]);
        System.out.println("Itens:");

        double soma = 0.0;
        for (int i = 0; i < VendasItens.length; i++) {
            String vv = VendasItens[i][VDI_VDA_ID];
            if (vv != null && vv.equals(Vendas[vdaIdx][VDA_ID])) {
                System.out.println(" Item VDI_ID: " + VendasItens[i][VDI_ID] +
                        " PRD_ID: " + VendasItens[i][VDI_PRD_ID] +
                        " Unit: " + VendasItens[i][VDI_VL_UNITARIO] +
                        " Qtd: " + VendasItens[i][VDI_QTD] +
                        " Total: " + VendasItens[i][VDI_VL_TOTAL]);
                try {
                    soma += Double.parseDouble(VendasItens[i][VDI_VL_TOTAL].replace(",", "."));
                } catch (Exception e) {}
            }
        }
        System.out.println("TOTAL PEDIDO: " + String.format("%.2f", soma));

        System.out.print("Confirmar impressão (simula imprimir e marca como impresso)? (S/N): ");
        String r = sc.nextLine().trim().toUpperCase();
        if (r.equals("S")) {
            Vendas[vdaIdx][VDA_IMPRIMIU] = "S";
            System.out.println("(Simulação) Pedido enviado para impressora. Venda marcada como impressa.");
        } else {
            System.out.println("Impressão cancelada (não marcou como impressa).");
        }
    }

    /***********************
     * HELPERS (Produtos já existentes + novos)
     ************************/
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
            String vPrd = VendasItens[i][VDI_PRD_ID];
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

    // CLIENTES - helpers
    public static int generateNextClientId() {
        int max = 0;
        for (int i = 0; i < Clientes.length; i++) {
            String val = Clientes[i][CLI_ID];
            if (val != null && !val.trim().isEmpty()) {
                try {
                    int id = Integer.parseInt(val);
                    if (id > max) max = id;
                } catch (Exception e) {}
            }
        }
        return max + 1;
    }

    public static int findFreeClientSlot() {
        for (int i = 0; i < Clientes.length; i++) {
            if (Clientes[i][CLI_ID] == null || Clientes[i][CLI_ID].trim().isEmpty()) return i;
        }
        return -1;
    }

    public static int findClientIndexById(String id) {
        for (int i = 0; i < Clientes.length; i++) {
            if (Clientes[i][CLI_ID] != null && Clientes[i][CLI_ID].equals(id)) return i;
        }
        return -1;
    }

    public static boolean hasClientSales(String cliId) {
        for (int i = 0; i < Vendas.length; i++) {
            String vCli = Vendas[i][VDA_CLI_ID];
            if (vCli != null && vCli.equals(cliId)) return true;
        }
        return false;
    }

    public static void printClientLine(int idx) {
        System.out.println("ID: " + Clientes[idx][CLI_ID]);
        System.out.println("Nome: " + Clientes[idx][CLI_NOME]);
        System.out.println("Telefone: " + Clientes[idx][CLI_TELEFONE]);
    }

    // VENDEDORES - helpers
    public static int generateNextVendorId() {
        int max = 0;
        for (int i = 0; i < Vendedores.length; i++) {
            String val = Vendedores[i][VND_ID];
            if (val != null && !val.trim().isEmpty()) {
                try {
                    int id = Integer.parseInt(val);
                    if (id > max) max = id;
                } catch (Exception e) {}
            }
        }
        return max + 1;
    }

    public static int findFreeVendorSlot() {
        for (int i = 0; i < Vendedores.length; i++) {
            if (Vendedores[i][VND_ID] == null || Vendedores[i][VND_ID].trim().isEmpty()) return i;
        }
        return -1;
    }

    public static int findVendorIndexById(String id) {
        for (int i = 0; i < Vendedores.length; i++) {
            if (Vendedores[i][VND_ID] != null && Vendedores[i][VND_ID].equals(id)) return i;
        }
        return -1;
    }

    public static boolean hasVendorSales(String vndId) {
        for (int i = 0; i < Vendas.length; i++) {
            String vVnd = Vendas[i][VDA_VND_ID];
            if (vVnd != null && vVnd.equals(vndId)) return true;
        }
        return false;
    }

    public static void printVendorLine(int idx) {
        System.out.println("ID: " + Vendedores[idx][VND_ID]);
        System.out.println("Nome: " + Vendedores[idx][VND_NOME]);
        System.out.println("Telefone: " + Vendedores[idx][VND_TELEFONE]);
        System.out.println("Salário: " + Vendedores[idx][VND_SALARIO]);
        System.out.println("Comissão (%): " + Vendedores[idx][VND_COMISSAO]);
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

    /***********************
     * HELPERS (VENDAS e COMISSOES)
     ************************/

    // Vendas helpers
    public static int generateNextVendaId() {
        int max = 0;
        for (int i = 0; i < Vendas.length; i++) {
            String val = Vendas[i][VDA_ID];
            if (val != null && !val.trim().isEmpty()) {
                try {
                    int id = Integer.parseInt(val);
                    if (id > max) max = id;
                } catch (Exception e) {}
            }
        }
        return max + 1;
    }

    public static int findFreeVendaSlot() {
        for (int i = 0; i < Vendas.length; i++) {
            if (Vendas[i][VDA_ID] == null || Vendas[i][VDA_ID].trim().isEmpty()) return i;
        }
        return -1;
    }

    public static int findVendaIndexById(String id) {
        for (int i = 0; i < Vendas.length; i++) {
            if (Vendas[i][VDA_ID] != null && Vendas[i][VDA_ID].equals(id)) return i;
        }
        return -1;
    }

    // VendasItens helpers
    public static int generateNextVendaItemId() {
        int max = 0;
        for (int i = 0; i < VendasItens.length; i++) {
            String val = VendasItens[i][VDI_ID];
            if (val != null && !val.trim().isEmpty()) {
                try {
                    int id = Integer.parseInt(val);
                    if (id > max) max = id;
                } catch (Exception e) {}
            }
        }
        return max + 1;
    }

    public static int findFreeVendaItemSlot() {
        for (int i = 0; i < VendasItens.length; i++) {
            if (VendasItens[i][VDI_ID] == null || VendasItens[i][VDI_ID].trim().isEmpty()) return i;
        }
        return -1;
    }

    // Comissoes helpers
    public static int generateNextCommissionId() {
        int max = 0;
        for (int i = 0; i < Comissoes.length; i++) {
            String val = Comissoes[i][CMI_ID];
            if (val != null && !val.trim().isEmpty()) {
                try {
                    int id = Integer.parseInt(val);
                    if (id > max) max = id;
                } catch (Exception e) {}
            }
        }
        return max + 1;
    }

    public static int findFreeCommissionSlot() {
        for (int i = 0; i < Comissoes.length; i++) {
            if (Comissoes[i][CMI_ID] == null || Comissoes[i][CMI_ID].trim().isEmpty()) return i;
        }
        return -1;
    }

    
}
