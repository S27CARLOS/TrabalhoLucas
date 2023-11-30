import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Programa {

    public static void main(String[] args) {
        List<Voo> voos = new ArrayList<>();
        Queue<Pendente> pen = new LinkedList<>();
        Stack<Check_in> checkin = new Stack<>();
        DateTimeFormatter f1 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        int num = 1000;
        String origem = "Brasil";
        String destino = "Estados Unidos";
        LocalDateTime partida = LocalDateTime.parse("20/12/2023 12:00", f1);
        LocalDateTime chegada = LocalDateTime.parse("02/01/2024 02:12", f1);
        int max = 400;
        voos.add(new Voo(num, origem, destino, partida, chegada, max));

        num = 2001;
        origem = "Estados Unidos";
        destino = "Japão";
        partida = LocalDateTime.parse("20/12/2023 10:23", f1);
        chegada = LocalDateTime.parse("30/12/2023 11:11", f1);
        max = 410;
        voos.add(new Voo(num, origem, destino, partida, chegada, max));

        Locale.setDefault(Locale.US);
        Scanner s = new Scanner(System.in);

        Usuario usuarioLogado = null; // Usuário atualmente logado (administrador ou cliente), está sendo associada a classe "Usuario" do programa separada

        boolean sair = false;
        do {
            System.out.println("Bem-vindo ao trabalho de sistema de aeroporto proposto pelo Lucas");
            System.out.println("-----------------------------------------------------------------");
            System.out.println("Por favor, escolha o seu tipo de usuário:");
            System.out.println("1 - Administrador");
            System.out.println("2 - Cliente");
            System.out.println("3 - Deslogar");

            int tipoUsuario = 0;
            
            // Lê a entrada do usuário e verifica se é um número inteiro (método da classe scanner "hasNextInt")
             if (s.hasNextInt()) {
                tipoUsuario = s.nextInt();
                s.nextLine();

                if (tipoUsuario < 1 || tipoUsuario > 3) {
                    System.out.println("Opção inválida. Tente novamente.");
                    continue;
                }
            } else {
                System.out.println("Opção inválida. Por favor, digite um número válido de escolha.");
                s.nextLine();
                continue;
            }

            if (tipoUsuario == 1) {
                // Administrador
                usuarioLogado = new Usuario("Admin", 0, "admin", "admin@admin.com", true);
                System.out.println("Você está logado como administrador.");

                boolean adminLogado = true;
                while (adminLogado) {
                    System.out.println("Escolha uma opção:");
                    System.out.println("1 - Ver voos disponíveis, pendentes e cheios");
                    System.out.println("2 - Ver reservas pendentes");
                    System.out.println("3 - Confirmar reservas");
                    System.out.println("4 - Adicionar Voo");
                    System.out.println("5 - Sair (Deslogar como administrador)");

                    int escolhaAdmin = s.nextInt();
                    s.nextLine();

                    switch (escolhaAdmin) {

                        case 1:
                            // Ver voos disponíveis, pendentes e cheios
                            System.out.println("Lista de voos disponíveis:");
                        for (Voo v : voos) {
                            if (!v.testCheio(checkin)) {
                                v.showListDisp(checkin);
                                System.out.println("-----------------------");
                            }
                        }
                        System.out.println("Lista de voos pendentes:");
                        for (Pendente p : pen) {
                            if (p != null) {
                                p.showListPendente(voos);
                                System.out.println("-----------------------");
                            }
                        }
                        System.out.println("Lista de voos cheios:");
                        for (Voo v : voos) {
                            if (v.testCheio(checkin)) {
                                v.showListCheio(checkin);
                                System.out.println("-----------------------");
                            }
                        }
                        
                            break;
                        case 2:
                            // Ver reservas pendentes (Somente para administradores)
                            System.out.println("Lista de reservas pendentes:");
                            for (Pendente p : pen) {
                                if (!p.isConfirmada()) {
                                    p.showListPendente(voos);
                                    System.out.println("-----------------------");
                                }
                            }
                            
                            break;
                        case 3:
                            // Confirmar reservas (lógica para administradores)
                            System.out.println("Confirmar reservas pendentes (digite o número da reserva):");
                        int numReserva = s.nextInt();
                        s.nextLine(); 
                        for (Pendente p : pen) {
                            if (p != null && !p.isConfirmada() && p.getId() == numReserva) {
                                p.setConfirmada(true);
                                System.out.println("Reserva " + numReserva + " confirmada com sucesso!");
                            }
                        }
                            break;
                        case 4:
                            // Administrador adcionar voo
                            System.out.println("Digite os detalhes do novo voo:");
                            System.out.print("Número do Voo: ");
                            int novoNum = s.nextInt();
                            s.nextLine();
                            System.out.print("Origem: ");
                            String novaOrigem = s.nextLine();
                            System.out.print("Destino: ");
                            String novoDestino = s.nextLine();
                            System.out.print("Data e Hora de Partida *Digite nesse molde ->* (dd/MM/yyyy HH:mm): ");
                            String novaPartidaString = s.nextLine();
                            LocalDateTime novaPartida = LocalDateTime.parse(novaPartidaString, f1);
                            System.out.print("Data e Hora de Chegada *Digite nesse molde ->* (dd/MM/yyyy HH:mm): ");
                            String novaChegadaString = s.nextLine();
                            LocalDateTime novaChegada = LocalDateTime.parse(novaChegadaString, f1);
                            System.out.print("Capacidade Máxima: ");
                            int novaCapacidade = s.nextInt();
                            s.nextLine();

                            voos.add(new Voo(novoNum, novaOrigem, novoDestino, novaPartida, novaChegada, novaCapacidade));
                            System.out.println("Novo voo adicionado com sucesso!");
                            break;
                        case 5:
                            adminLogado = false; // Deslogar o administrador
                            System.out.println("Deslogado como administrador.");
                            break;
                        default:
                            System.out.println("Opção inválida. Tente novamente.");
                    }
                }
            } else if (tipoUsuario == 2) {
                // Cliente
                System.out.println("Você está logado como cliente.");
                boolean clienteLogado = true; // Cliente está logado
                while (clienteLogado) {
                    System.out.println("Escolha uma opção:");
                    System.out.println("1 - Ver voos disponíveis, pendentes e cheios");
                    System.out.println("2 - Fazer reserva");
                    System.out.println("3 - Fazer check-in");
                    System.out.println("4 - Sair (Deslogar como cliente)");

                    int escolhaCliente = s.nextInt();
                    s.nextLine(); 

                    switch (escolhaCliente) {
                        
                        case 1:
                            // Ver voos disponíveis, pendentes e cheios 
                            System.out.println("Lista de voos disponíveis:");
                            for (Voo v : voos) {
                                if (!v.testCheio(checkin)) {
                                    v.showListDisp(checkin);
                                    System.out.println("-----------------------");
                                }
                            }
                            System.out.println("Lista de voos pendentes:");
                            for (Pendente p : pen) {
                                if (p != null) {
                                    p.showListPendente(voos);
                                    System.out.println("-----------------------");
                                }
                            }
                            System.out.println("Lista de voos cheios:");
                            for (Voo v : voos) {
                                if (v.testCheio(checkin)) {
                                    v.showListCheio(checkin);
                                    System.out.println("-----------------------");
                                }
                            }
                            break;
                        case 2:
                            // Fazer reserva (clientes)
                            boolean fazerOutraReserva = false;
                            do {
                                System.out.print("Digite o número do voo: ");
                                int id = s.nextInt();
                                boolean vooEncontrado = false;

                                // Verificar se o voo existe
                                for (Voo v : voos) {
                                    if (id == v.getNum_voo()) {
                                        if (!v.testCheio(checkin)) {
                                            System.out.print("Digite o seu nome: ");
                                            s.nextLine(); 
                                            String nome = s.nextLine();
                                            System.out.print("Digite a sua idade: ");
                                            int idade = s.nextInt();
                                            s.nextLine(); 
                                            System.out.print("Digite o seu cpf: ");
                                            String cpf = s.nextLine();
                                            System.out.print("Digite o seu e-mail: ");
                                            String email = s.nextLine();

                                            // A reserva é inicializada como não confirmada
                                            Pendente pendente = new Pendente(new Usuario(nome, idade, cpf, email, false), id, false);
                                            pen.add(pendente);
                                            vooEncontrado = true;
                                            System.out.println("Reserva realizada com sucesso");
                                        } else {
                                            System.out.println("Este voo está cheio.");
                                        }
                                        break;
                                    }
                                }

                                if (!vooEncontrado) {
                                    System.out.println("Este voo não existe.");
                                }

                                System.out.println("Gostaria de fazer outra reserva (s/n)?");
                                char choice = s.next().charAt(0);

                                while (choice != 's' && choice != 'n') {
                                    System.out.println("Opção inválida. Digite 's' para sim ou 'n' para não.");
                                    choice = s.next().charAt(0);
                                }

                                if (choice == 's') {
                                    fazerOutraReserva = true;
                                } else if (choice == 'n') {
                                    fazerOutraReserva = false;
                                }
                            } while (fazerOutraReserva);
                            break;
                        case 3:
                            // Fazer check-in (clientes)
                            System.out.println("Lista de Voos com Reservas Confirmadas:");
                            boolean temReservaConfirmada = false;
                            for (Pendente p : pen) {
                                if (p != null && p.isConfirmada()) {
                                    p.showListPendente(voos);
                                    System.out.println("-----------------------");
                                    temReservaConfirmada = true;
                                }
                            }
                            if (!temReservaConfirmada) {
                                System.out.println("Não há reservas confirmadas para fazer check-in.");
                            } else {
                                System.out.print("Escreva o número do voo para fazer check-in: ");
                                int id = s.nextInt();
                                s.nextLine(); 
                                System.out.print("Escreva o CPF associado à reserva: ");
                                String cpf = s.nextLine();
                                boolean checkinRealizado = false;
                                for (Pendente p : pen) {
                                    if (p.getId() == id && p.isConfirmada() && p.getCpf().equals(cpf)) {
                                        // Realizar check-in apenas se a reserva estiver confirmada
                                        checkin.add(new Check_in(id, cpf));
                                        System.out.println("Check-in realizado com sucesso!");
                                        checkinRealizado = true;
                                        break;
                                    }
                                }
                                if (!checkinRealizado) {
                                    System.out.println("Check-in não realizado. Verifique os dados inseridos.");
                                }
                            }
                            break;
                        case 4:
                            clienteLogado = false; // Deslogar o cliente
                            System.out.println("Você foi deslogado como cliente.");
                            break;
                        default:
                            System.out.println("Opção inválida. Tente novamente.");
                    }
                }
            } 
            
            else if (tipoUsuario == 3) {
                // Deslogar
                usuarioLogado = null;
                System.out.println("Você foi deslogado.");
            } else {
                System.out.println("Tipo de usuário inválido. Tente novamente.");
            }

        } while (!sair);
        s.close();
    }

}
