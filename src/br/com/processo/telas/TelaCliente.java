/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.processo.telas;

import br.com.processo.DAO.ModuloConexao;
import br.com.processo.Model.ValidaCPF;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Admin
 */
public class TelaCliente extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaCliente
     */
    public TelaCliente() {
        initComponents();
        conexao = ModuloConexao.conector();
        pesquisarCliente();
        /*
        String data = "10-10-2018";
        
        String dia = data.substring(0,2);
        
        String mes = data.substring(3,5);
        
        String ano = data.substring(6,10);
        
        
        System.out.println(ano+"-"+mes+"-"+dia);
         */

    }

    public String validarCPF(String cpf) {
        ValidaCPF valCpf = new ValidaCPF();

        String CPF = cpf;

        if (ValidaCPF.isCPF(CPF) == true) {

        } else {

            JOptionPane.showMessageDialog(null, "CPF/Cnpj Ivalido!!", null, JOptionPane.ERROR);
        }

        return CPF;

    }

    private void adicionar() {
        String sql = "insert into tbclientes(nomecli, endcli, fonecli, emailcli, cpfcli,razaosocialcli) values (?,?,?,?,?,?)";

        try {

            pst = conexao.prepareStatement(sql);

            pst.setString(1, txtCliNome.getText());
            pst.setString(2, txtCliEnd.getText());
            pst.setString(3, txtCliFone.getText());
            pst.setString(4, txtCliEmail.getText());
            pst.setString(5, validarCPF(txtCliCpf.getText()));
            pst.setString(6, txtCliRazao.getText());

            //As linhas abaixo valida os campos obrigatorios 
            if ((txtCliNome.getText().isEmpty()) || (txtCliCpf.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios2");

            } else {

                // linha abaixa atualiza a tabela do banco de dados  
                // a estrura abaixo e usada para confirmar a inserção dos  na tabela
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente adicionado com sucesso");

                    // os linhas abaixo limpa todos os campos apos adicionar
                    txtCliNome.setText(null);
                    txtCliEnd.setText(null);
                    txtCliFone.setText(null);
                    txtCliEmail.setText(null);
                    txtCliCpf.setText(null);
                    txtCliRazao.setText(null);

                    pesquisarCliente();

                }
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "CPF/CNPJ Inválido");
        }

    }

    private void pesquisarCliente() {
        String sql = "Select idcli as ID, nomecli as Nome, endcli as Endereco, fonecli as Telefone, emailcli as Email, cpfcli as CPF, razaosocialcli as RazaoSocial from tbclientes where idcli like ? or nomecli like ? or endcli like ? or fonecli like ? or emailcli like ? or cpfcli like ? or razaosocialcli like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtCliPesquisar.getText() + "%");
            pst.setString(2, txtCliPesquisar.getText() + "%");
            pst.setString(3, txtCliPesquisar.getText() + "%");
            pst.setString(4, txtCliPesquisar.getText() + "%");
            pst.setString(5, txtCliPesquisar.getText() + "%");
            pst.setString(6, txtCliPesquisar.getText() + "%");
            pst.setString(7, txtCliPesquisar.getText() + "%");
            rs = pst.executeQuery();
            tblClientes.setModel(DbUtils.resultSetToTableModel(rs));
            /*
            // a linha abaixo usa a biblioteca rs2xml.jar para preencher a tabela
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date minhaData = format.parse("12/05/2004");
            Date minhaDataAual = new Date();
           
           if(minhaDataAual.equals(minhaDataAual)){
               System.out.println("Ainda vai acontecer o dia");
          } else if (minhaData.after(new Date())) {
               System.out.println("O dia já aconteceu");
          }
             */
 /*
            Calendar c = Calendar.getInstance();
            
            if(minhaDataAual.after()){
                System.out.println("certo");
            else{
                System.out.println("errado");
            
            }
            }
            //System.out.println("Data/Hora atual: " + c.getTime());
            //System.out.println("Ano: " + c.get(Calendar.YEAR));
            //System.out.println("Mês: " + c.get(Calendar.MONTH));
            System.out.println("Dia do Mês: " + c.get(Calendar.DAY_OF_MONTH));

            tblClientes.setModel(DbUtils.resultSetToTableModel(rs));
             */

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void setar_campos() {
        int setar = tblClientes.getSelectedRow();

        txtCliId.setText(tblClientes.getModel().getValueAt(setar, 0).toString());
        txtCliNome.setText(tblClientes.getModel().getValueAt(setar, 1).toString());
        txtCliEnd.setText(tblClientes.getModel().getValueAt(setar, 2).toString());
        txtCliFone.setText(tblClientes.getModel().getValueAt(setar, 3).toString());
        txtCliEmail.setText(tblClientes.getModel().getValueAt(setar, 4).toString());
        txtCliCpf.setText(tblClientes.getModel().getValueAt(setar, 5).toString());
        txtCliRazao.setText(tblClientes.getModel().getValueAt(setar, 6).toString());
        // a linha abaixo desabilita o botão adicionar
        btnCliInserir.setEnabled(false);
    }

    private void alterarCliente() {
        String sql = "update tbclientes set nomecli=?,endcli=?,fonecli=?,emailcli=?,cpfcli=?,razaosocialcli=? where idcli=?";

        try {

            pst = conexao.prepareStatement(sql);

            pst.setString(1, txtCliNome.getText());
            pst.setString(2, txtCliEnd.getText());
            pst.setString(3, txtCliFone.getText());
            pst.setString(4, txtCliEmail.getText());
            pst.setString(5, txtCliCpf.getText());
            pst.setString(6, txtCliRazao.getText());
            pst.setString(7, txtCliId.getText());

            //As linhas abaixo valida os campos obrigatorios 
            if ((txtCliNome.getText().isEmpty()) || (txtCliCpf.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios");

            } else {

                // linha abaixa atualiza a tabela do banco de dados  
                // a estrura abaixo e usada para confirmar a inserção dos  na tabela
                int adicionado = pst.executeUpdate();

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente alterado com sucesso");

                    // os linhas abaixo limpa todos os campos apos adicionar
                    txtCliNome.setText(null);
                    txtCliEnd.setText(null);
                    txtCliFone.setText(null);
                    txtCliEmail.setText(null);
                    txtCliCpf.setText(null);
                    txtCliRazao.setText(null);

                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    private void excluir() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir o cliente", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from tbclientes where idcli=?";

            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtCliId.getText());
                int apagado = pst.executeUpdate();

                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente removido com sucesso");

                    txtCliNome.setText(null);
                    txtCliEnd.setText(null);
                    txtCliFone.setText(null);
                    txtCliEmail.setText(null);
                    txtCliCpf.setText(null);
                    txtCliRazao.setText(null);

                    pesquisarCliente();
                }
            } catch (Exception e) {

                JOptionPane.showMessageDialog(null, e);

            }

        }
    }

    //O metodo abaixo e para setar os campos do formulário com o conteúdo da tabela
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtCliEnd = new javax.swing.JTextField();
        txtCliFone = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtCliEmail = new javax.swing.JTextField();
        txtCliId = new javax.swing.JTextField();
        txtCliCpf = new javax.swing.JTextField();
        txtCliNome = new javax.swing.JTextField();
        txtCliRazao = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnCliInserir = new javax.swing.JButton();
        btnCliRemover = new javax.swing.JButton();
        btnCliAlterar = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        txtCliPesquisar = new javax.swing.JTextField();
        Pesquisar = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Clientes");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastro de Cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 0, 0))); // NOI18N
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel6.setText("Razão Social");

        jLabel7.setText("ID");

        txtCliId.setEnabled(false);

        jLabel1.setText(" Nome");

        jLabel2.setText("Endereço");

        jLabel3.setText("Telefone");

        jLabel4.setText("E-mail");

        jLabel5.setText("* cpf/cnpj");

        btnCliInserir.setBackground(new java.awt.Color(27, 187, 125));
        btnCliInserir.setForeground(new java.awt.Color(255, 255, 255));
        btnCliInserir.setText("Inserir");
        btnCliInserir.setBorderPainted(false);
        btnCliInserir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCliInserir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCliInserirMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCliInserirMouseExited(evt);
            }
        });
        btnCliInserir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCliInserirActionPerformed(evt);
            }
        });

        btnCliRemover.setBackground(new java.awt.Color(217, 81, 51));
        btnCliRemover.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCliRemover.setForeground(new java.awt.Color(255, 255, 255));
        btnCliRemover.setText("Excluir");
        btnCliRemover.setBorderPainted(false);
        btnCliRemover.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCliRemover.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCliRemoverMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCliRemoverMouseExited(evt);
            }
        });
        btnCliRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCliRemoverActionPerformed(evt);
            }
        });

        btnCliAlterar.setBackground(new java.awt.Color(0, 51, 51));
        btnCliAlterar.setForeground(new java.awt.Color(255, 255, 255));
        btnCliAlterar.setText("Alterar");
        btnCliAlterar.setBorderPainted(false);
        btnCliAlterar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCliAlterar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCliAlterarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCliAlterarMouseExited(evt);
            }
        });
        btnCliAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCliAlterarActionPerformed(evt);
            }
        });

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 0, 0));
        jLabel8.setText("*");

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 0, 0));
        jLabel9.setText("*");

        jLabel10.setText("Campos Obrigatorio");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel9)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtCliId, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtCliNome, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtCliCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(txtCliFone, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(80, 80, 80)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6))
                        .addGap(5, 5, 5)
                        .addComponent(jLabel8)
                        .addGap(4, 4, 4)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtCliEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                                .addComponent(txtCliEnd)
                                .addComponent(txtCliRazao))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(btnCliInserir)
                        .addGap(80, 80, 80)
                        .addComponent(btnCliAlterar)
                        .addGap(149, 149, 149)
                        .addComponent(btnCliRemover)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnCliAlterar, btnCliInserir, btnCliRemover});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCliId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCliNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(txtCliEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel9))
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtCliFone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCliEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtCliCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCliRazao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(82, 82, 82)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCliInserir)
                    .addComponent(btnCliAlterar)
                    .addComponent(btnCliRemover, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(45, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnCliAlterar, btnCliInserir, btnCliRemover});

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pesquisa de Cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 0, 0))); // NOI18N

        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblClientes);

        txtCliPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCliPesquisarKeyReleased(evt);
            }
        });

        Pesquisar.setText("Pesquisar");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(Pesquisar)
                .addGap(18, 18, 18)
                .addComponent(txtCliPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 751, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCliPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Pesquisar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCliInserirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCliInserirActionPerformed
        adicionar();        // TODO add your handling code here:
    }//GEN-LAST:event_btnCliInserirActionPerformed
// o evento abaixo pesquisa enquanto estiver digitando
    private void txtCliPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCliPesquisarKeyReleased
        // TODO add your handling code here:
        pesquisarCliente();
    }//GEN-LAST:event_txtCliPesquisarKeyReleased
//envento que sera usado para setar os campos da tabela clicando com o mouse
    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesMouseClicked
        // TODO add your handling code here:
        setar_campos();
    }//GEN-LAST:event_tblClientesMouseClicked

    private void btnCliAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCliAlterarActionPerformed

        alterarCliente();
    }//GEN-LAST:event_btnCliAlterarActionPerformed

    private void btnCliRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCliRemoverActionPerformed
        excluir();        // TODO add your handling code here:
    }//GEN-LAST:event_btnCliRemoverActionPerformed

    private void btnCliRemoverMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCliRemoverMouseEntered
        btnCliRemover.setBackground(new Color(235, 235, 235));
        btnCliRemover.setForeground(new Color(217, 81, 51));// TODO add your handling code here:
    }//GEN-LAST:event_btnCliRemoverMouseEntered

    private void btnCliRemoverMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCliRemoverMouseExited
        btnCliRemover.setBackground(new Color(217, 81, 51));
        btnCliRemover.setForeground(Color.white);        // TODO add your handling code here:
    }//GEN-LAST:event_btnCliRemoverMouseExited

    private void btnCliInserirMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCliInserirMouseEntered
        btnCliInserir.setBackground(new Color(235, 235, 235));
        btnCliInserir.setForeground(new Color(58, 65, 84)); // TODO add your handling code here:
// TODO add your handling code here:
    }//GEN-LAST:event_btnCliInserirMouseEntered

    private void btnCliInserirMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCliInserirMouseExited
        btnCliInserir.setBackground(new Color(27, 187, 125));
        btnCliInserir.setForeground(Color.white);        // TODO add your handling code here:
    }//GEN-LAST:event_btnCliInserirMouseExited

    private void btnCliAlterarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCliAlterarMouseEntered
        btnCliAlterar.setBackground(new Color(235, 235, 235));
        btnCliAlterar.setForeground(new Color(58, 65, 84));

        // TODO add your handling code here:
    }//GEN-LAST:event_btnCliAlterarMouseEntered

    private void btnCliAlterarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCliAlterarMouseExited
       btnCliAlterar.setBackground(new Color(58, 65, 84));
        btnCliAlterar.setForeground(Color.white); // TODO add your handling code here:
    }//GEN-LAST:event_btnCliAlterarMouseExited


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Pesquisar;
    private javax.swing.JButton btnCliAlterar;
    private javax.swing.JButton btnCliInserir;
    private javax.swing.JButton btnCliRemover;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTextField txtCliCpf;
    private javax.swing.JTextField txtCliEmail;
    private javax.swing.JTextField txtCliEnd;
    private javax.swing.JTextField txtCliFone;
    private javax.swing.JTextField txtCliId;
    private javax.swing.JTextField txtCliNome;
    private javax.swing.JTextField txtCliPesquisar;
    private javax.swing.JTextField txtCliRazao;
    // End of variables declaration//GEN-END:variables
}
