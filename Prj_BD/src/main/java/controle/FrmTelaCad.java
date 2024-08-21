package controle;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import conexao.Conexao;
import java.text.ParseException;


public class FrmTelaCad extends JFrame {
    JTable TBTclientes;
    JScrollPane Jcliente;
    Conexao con_cliente;

    
    JTextField cod;
    JTextField txtnome;
    JFormattedTextField dataftf;
    JFormattedTextField telefoneftf;
    JTextField emailftf;

    public FrmTelaCad() {
        super("Conexão com o Java e o mysql");
        Container tela = getContentPane();
        tela.setLayout(null);
        tela.setBackground(Color.RED);

        con_cliente = new Conexao(); // iniciando o objjeto
        con_cliente.conecta(); // chamando o método que conecta

        JLabel codlabe = new JLabel("Código:");
        codlabe.setBounds(20, 20, 100, 20);
        tela.add(codlabe);/*
        add as telas
        */

        cod = new JTextField();
        cod.setBounds(90, 20, 50, 20);
        tela.add(cod);

        JLabel nomeJLabel = new JLabel("Nome:");
        nomeJLabel.setBounds(20, 50, 100, 20);
        tela.add(nomeJLabel);

        txtnome = new JTextField();
        txtnome.setBounds(90, 50, 150, 20);
        tela.add(txtnome);

        JLabel dataJLabel = new JLabel("Data:");
        dataJLabel.setBounds(20, 80, 100, 20);
        tela.add(dataJLabel);

        MaskFormatter maskarar;
        try {
            maskarar = new MaskFormatter("##/##/####");
            maskarar.setPlaceholderCharacter('_');
        } catch (ParseException e) {
            e.printStackTrace();
            maskarar = new MaskFormatter();
        }

        dataftf = new JFormattedTextField(maskarar);
        dataftf.setBounds(90, 80, 68, 20);
        dataftf.setColumns(10);
        tela.add(dataftf);

        JLabel jtel = new JLabel("Telefone:");
        jtel.setBounds(20, 110, 100, 20);
        tela.add(jtel);

        MaskFormatter telefoneMask;
        try {
            telefoneMask = new MaskFormatter("(##)#####-####");
            telefoneMask.setPlaceholderCharacter('_');
        } catch (ParseException e) {
            e.printStackTrace();
            telefoneMask = new MaskFormatter();
        }

        telefoneftf = new JFormattedTextField(telefoneMask);
        telefoneftf.setBounds(90, 110, 90, 20);
        telefoneftf.setColumns(10);
        tela.add(telefoneftf);

        JLabel jmail = new JLabel("Email:");
        jmail.setBounds(20, 140, 100, 20);
        tela.add(jmail);

        emailftf = new JTextField();
        emailftf.setBounds(90, 140, 150, 20);
        tela.add(emailftf);

        JButton primeiroButton = new JButton("Primeiro");
        primeiroButton.setBounds(20, 180, 100, 20);
        tela.add(primeiroButton);

        primeiroButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    con_cliente.resultset.first();
                    mostrar_Dados();
                } catch (SQLException erro) {
                    JOptionPane.showMessageDialog(null, "Não foi possível acessar o primeiro registro", "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        JButton anteriorButton = new JButton("Anterior");
        anteriorButton.setBounds(130, 180, 100, 20);
        tela.add(anteriorButton);

        anteriorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!con_cliente.resultset.isFirst()) {
                        con_cliente.resultset.previous();
                        mostrar_Dados();
                    }
                } catch (SQLException erro) {
                    JOptionPane.showMessageDialog(null, "Não foi possível acessar o registro anterior", "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        JButton proximoButton = new JButton("Próximo");
        proximoButton.setBounds(240, 180, 100, 20);
        tela.add(proximoButton);

        proximoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!con_cliente.resultset.isLast()) {
                        con_cliente.resultset.next();
                        mostrar_Dados();
                    }
                } catch (SQLException erro) {
                    JOptionPane.showMessageDialog(null, "Não foi possível acessar o próximo registro", "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        JButton ultimoButton = new JButton("ultimo");
        ultimoButton.setBounds(350, 180, 100, 20);
        tela.add(ultimoButton);

        ultimoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    con_cliente.resultset.last();
                    mostrar_Dados();
                } catch (SQLException erro) {
                    JOptionPane.showMessageDialog(null, "Não foi possível acessar o último registro", "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        JButton botaonovo = new JButton("Novo Registro");
        botaonovo.setBounds(540,235,140,20);
        tela.add(botaonovo);

        botaonovo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cod.setText("");
                txtnome.setText("");
                dataftf.setText("");
                telefoneftf.setText("");
                emailftf.setText("");
                cod.requestFocus();
            }
        });

        JButton gravacao = new JButton("Gravar");
        gravacao.setBounds(540,285,140,20);
        gravacao.setMnemonic(KeyEvent.VK_G);
        tela.add(gravacao);

        gravacao.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nome = txtnome.getText();
                String data_nasc = dataftf.getText();
                String telefone = telefoneftf.getText();
                String email = emailftf.getText();

                try {
                    String insert_sql = "insert into tbclientes (nome,telefone,email,dt_nasc) values ('"+nome+"','"+telefone+"','"+email+"','"+data_nasc+"')";
                    con_cliente.statement.executeUpdate(insert_sql);
                    JOptionPane.showMessageDialog(null, "Gravação realizada com sucesso!!", "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);

                    con_cliente.executaSQL("select * from tbclientes order by cod");
                    preencherTabela();
                } catch (SQLException erro) {
                    JOptionPane.showMessageDialog(null, "\n Erro na gravação: \n" +erro, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        JButton altera = new JButton("Alterar");
        altera.setBounds(540, 325, 140, 20);
        tela.add(altera);

        altera.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nome = txtnome.getText();
                String data_nasc = dataftf.getText();
                String telefone = telefoneftf.getText();
                String email = emailftf.getText();
                String sql;
                String msg = "";

                try {
                    if (cod.getText().equals("")) {
                        sql = "insert into tbclientes (nome, telefone, email, dt_nasc) values ('" + nome + "', '" + telefone + "', '" + email + "', '" + data_nasc + "')";
                        msg = "Gravação de um novo registro";
                    } else {
                        sql = "update tbclientes set nome = '" + nome + "', telefone = '" + telefone + "', email = '" + email + "', dt_nasc = '" + data_nasc + "' where cod = " + cod.getText();
                        msg = "Alteração de registro";
                    }

                    con_cliente.statement.executeUpdate(sql);
                    JOptionPane.showMessageDialog(null, "Gravado com Sucesso!", "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);

                    con_cliente.executaSQL("select * from tbclientes order by cod");
                    preencherTabela();
                } catch (SQLException erro) {
                    JOptionPane.showMessageDialog(null, "\n Erro na gravação: \n" + erro, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        JButton exclui = new JButton("Excluir");
        exclui.setBounds(540,365,140,20);
        tela.add(exclui);

        exclui.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String sql = "";
                try {
                    int resposta = JOptionPane.showConfirmDialog(rootPane, "Deseja excluir o registro: ", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION, 3);
                    if (resposta==0){
                        sql = "delete from tbclientes where cod =" +cod.getText();
                        int excluir = con_cliente.statement.executeUpdate(sql);
                        if(excluir==1){
                            JOptionPane.showMessageDialog(null, "exclusão realizada com sucesso!", "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
                            con_cliente.executaSQL("select * from tbclientes order by cod");
                            con_cliente.resultset.first();
                            preencherTabela();
                            posicionarRegistro();
                        }
                        else{
                            JOptionPane.showMessageDialog(null, " Cancelado pelo usuário", "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                } catch (SQLException erro) {
                    JOptionPane.showMessageDialog(null, "Erro na exclusão", "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        JButton botaosair = new JButton("Sair");
        botaosair.setBounds(580, 30, 65, 20); 
        tela.add(botaosair);

       
        botaosair.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Fecha a janela
                System.exit(0);
            }
        });

        TBTclientes = new JTable();
        Jcliente = new JScrollPane(TBTclientes);
        Jcliente.setBounds(20, 220, 500, 200);
        tela.add(Jcliente);

        TBTclientes.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        TBTclientes.setFont(new Font("Arial", Font.BOLD, 12));
        TBTclientes.setModel(new DefaultTableModel(
                new Object[][] {
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null}
                },
                new String[] {"Código", "Nome", "Data Nascimento", "Telefone", "Email"}) {
            boolean[] canEdit = new boolean[] {false, false, false, false, false};

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        /* 
        Atalhos 
        */
        
        botaonovo.setMnemonic(KeyEvent.VK_N);
        altera.setMnemonic(KeyEvent.VK_A);
        
        
        TBTclientes.setAutoCreateRowSorter(true);


        JTextField pesquisaJTextField = new JTextField();
        pesquisaJTextField.setBounds(22,440,400,20);
        tela.add(pesquisaJTextField);

        JButton pesquisa = new JButton("Pesquisar");
        pesquisa.setBounds(422,440,100,20);
        pesquisa.setMnemonic(KeyEvent.VK_P);
        tela.add(pesquisa);

        pesquisa.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String pesquisarr = pesquisaJTextField.getText().trim();
                    
                    String sql = "select * from tbclientes where nome like '" + pesquisarr + "%'";
                    con_cliente.executaSQL(sql);
        
                    if (con_cliente.resultset.first()) {
                        preencherTabela();
                    } else {
                        JOptionPane.showMessageDialog(null, "\n Não existem dados com este parâmetro!", "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SQLException erro) {
                    JOptionPane.showMessageDialog(null, "\n Os dados digitados não foram localizados!\n" + erro, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
                

        setSize(715, 520);
        setVisible(true);
        setLocationRelativeTo(null);

        con_cliente.executaSQL("select * from tbclientes order by cod");
        preencherTabela();
        posicionarRegistro();
    }

    public void preencherTabela() {
        int width[] = {60, 150, 100, 120, 150};
        for (int i = 0; i < 5; i++) {
            TBTclientes.getColumnModel().getColumn(i).setPreferredWidth(width[i]);
        }

        DefaultTableModel modelo = (DefaultTableModel) TBTclientes.getModel();
        modelo.setNumRows(0);

        try {
            while (con_cliente.resultset.next()) {
                modelo.addRow(new Object[] {
                        con_cliente.resultset.getString("cod"),
                        con_cliente.resultset.getString("nome"),
                        con_cliente.resultset.getString("dt_nasc"),
                        con_cliente.resultset.getString("telefone"),
                        con_cliente.resultset.getString("email")
                });
            }
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "\n Erro ao listar dados da tabela! :\n" + erro, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void posicionarRegistro() {
        try {
            con_cliente.resultset.first();
            mostrar_Dados();
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Não foi possível posicionar no primeiro registro: " + erro, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void mostrar_Dados() {
        try {
            cod.setText(con_cliente.resultset.getString("cod"));
            txtnome.setText(con_cliente.resultset.getString("nome"));
            dataftf.setText(con_cliente.resultset.getString("dt_nasc"));
            telefoneftf.setText(con_cliente.resultset.getString("telefone"));
            emailftf.setText(con_cliente.resultset.getString("email"));
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Não localizou dados: " + erro, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /*
    Add as telas
    */
    

    public static void main(String[] args) {
        FrmTelaCad app = new FrmTelaCad();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}