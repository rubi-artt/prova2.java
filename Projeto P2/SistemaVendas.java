
import java.time.LocalDateTime;
import java.util.Scanner;

public class SistemaVendas {

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
                    System.out.println("Menu Vendas (a implementar).");
                    // se quiser eu implemento em seguida
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
}
