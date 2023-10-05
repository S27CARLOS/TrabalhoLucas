import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

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
        int max = 412;
        voos.add(new Voo(num, origem, destino, partida, chegada, max));

        num = 2001;
        origem = "Estados Unidos";
        destino = "Japão";
        partida = LocalDateTime.parse("20/12/2023 10:23", f1);
        chegada = LocalDateTime.parse("30/12/2023 11:11", f1);
        max = 400;
        voos.add(new Voo(num, origem, destino, partida, chegada, max));

        Locale.setDefault(Locale.US);
        Scanner s = new Scanner(System.in);

        boolean sair = false;
        do {
            System.out.println("Lista de Voos Disponíveis:");
            for (Voo v : voos) {
                System.out.println(v.toString());
                System.out.println("-----------------------");
            }
            

            System.out.println("Escreva 1 para fazer uma reserva.");
            System.out.println("Escreva 2 para fazer um check-in de uma reserva.");
            System.out.println("Escreva 3 para ver os voos disponíveis, pendentes e cheios.");
            System.out.println("Escreva 4 para sair.");
            int i = s.nextInt();
            s.nextLine(); 

            if (i == 1) {
                
                System.out.println("Deseja fazer uma reserva (s/n)?");
                char choice = s.nextLine().charAt(0);
                if (choice == 's') {
                    boolean fazerOutraReserva = false;
                    do {
                        System.out.print("Escreva o número do voo: ");
                        int id = s.nextInt();
                        boolean vooEncontrado = false;
                        for (Voo v : voos) {
                            if (id == v.getNum_voo()) {
                                if (!v.testCheio(checkin)) {
                                    System.out.print("Escreva o seu nome: ");
                                    s.nextLine();
                                    String nome = s.nextLine();
                                    System.out.print("Escreva o sua idade: ");
                                    int idade = s.nextInt();
                                    s.nextLine(); 
                                    System.out.print("Escreva o seu cpf: ");
                                    String cpf = s.nextLine();
                                    System.out.print("Escreva o seu e-mail: ");
                                    String email = s.nextLine();
                                    Pendente pendente = new Pendente(new Usuario(nome, cpf, email, idade), id);
                                    pen.add(pendente);
                                    vooEncontrado = true;
                                    System.out.println("Reserva realizada com sucesso");
                                    System.out.println("Gostaria de fazer outra reserva (s/n)?");
                                    choice = s.nextLine().charAt(0);
                                    while (choice != 's' && choice != 'n') {
                                        System.out.println("Opção inválida. Digite 's' para sim ou 'n' para não.");
                                        choice = s.nextLine().charAt(0);
                                    }
                                    if (choice == 's') {
                                        fazerOutraReserva = true;
                                    } else if (choice == 'n') {
                                        fazerOutraReserva = false;
                                    }
                                } else {
                                    System.out.println("Este voo está cheio.");
                                    System.out.println("Gostaria de fazer outra reserva (s/n)?");
                                    choice = s.nextLine().charAt(0);
                                    while (choice != 's' && choice != 'n') {
                                        System.out.println("Opção inválida. Digite 's' para sim ou 'n' para não.");
                                        choice = s.nextLine().charAt(0);
                                    }
                                    if (choice == 's') {
                                        fazerOutraReserva = true;
                                    } else if (choice == 'n') {
                                        fazerOutraReserva = false;
                                    }
                                }
                                break;
                            }
                        }
                        if (!vooEncontrado) {
                            System.out.println("Este voo não existe.");
                            System.out.println("Gostaria de fazer outra reserva (s/n)?");
                            choice = s.nextLine().charAt(0);
                            while (choice != 's' && choice != 'n') {
                                System.out.println("Opção inválida. Digite 's' para sim ou 'n' para não.");
                                choice = s.nextLine().charAt(0);
                            }
                            if (choice == 's') {
                                fazerOutraReserva = true;
                            } else if (choice == 'n') {
                                fazerOutraReserva = false;
                            }
                        }
                    } while (fazerOutraReserva);
                }
            } else if (i == 2) {
                
                for (Pendente p : pen) {
                    if (p != null) {
                        p.showListPend(voos);
                        System.out.println("-----------------------");
                    }
                }
                System.out.println("Escolha o número do voo");
                int id = s.nextInt();
                s.nextLine(); 
                String cpf;
                List<Pendente> reservasEncontradas = new ArrayList<>();

                for (Pendente p : pen) {
                    if (p.getId() == id) {
                        reservasEncontradas.add(p);
                    }
                }

                if (!reservasEncontradas.isEmpty()) {
                    // Se houver várias reservas para o mesmo voo, peça ao usuário para escolher com base no CPF
                    if (reservasEncontradas.size() > 1) {
                        System.out.println("Há várias reservas para este voo. Escolha com base no CPF:");

                        for (Pendente p : reservasEncontradas) {
                            System.out.println("CPF: " + p.getCpf());
                        }

                        System.out.print("Digite o CPF da reserva que deseja fazer o check-in: ");
                        cpf = s.nextLine();

                        Pendente reservaEscolhida = null;

                        for (Pendente p : reservasEncontradas) {
                            if (p.getCpf().equals(cpf)) {
                                reservaEscolhida = p;
                                break;
                            }
                        }

                        if (reservaEscolhida != null) {
                            checkin.add(new Check_in(id, cpf));
                            pen.remove(reservaEscolhida);
                            System.out.println("Check-in realizado com sucesso!");
                        } else {
                            System.out.println("CPF não encontrado. Check-in não realizado.");
                        }
                    } else {
                        // Se houver apenas uma reserva para o voo, não é necessário pedir o CPF
                        Pendente reservaUnica = reservasEncontradas.get(0);
                        cpf = reservaUnica.getCpf();
                        checkin.add(new Check_in(id, cpf));
                        pen.remove(reservaUnica);
                        System.out.println("Check-in realizado com sucesso!");
                    }
                } else {
                    System.out.println("Nenhuma reserva encontrada para este voo.");
                }

                System.out.println("Pressione Enter para continuar...");
                s.nextLine();
            } else if (i == 3) {
                
                System.out.println("Lista de voos disponíveis:");
                for (Voo v : voos) {
                    if (!v.testCheio(checkin)) {
                        v.showListDisp(checkin);
                        System.out.println("-----------------------");
                    }
                }
                System.out.println("Lista de voos reservados:");
                for (Pendente p : pen) {
                    if (p != null) {
                        p.showListPend(voos);
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
            } else if (i == 4) {
                sair = true;
                System.out.println("Obrigado por testar.");
            } else {
                System.out.println("Não é uma opção válida.");
            }
        } while (!sair);
    }
}
