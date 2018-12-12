/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.processo.telas;

import br.com.processo.DAO.ModuloConexao;
import static br.com.processo.telas.TelaRelatorio.tblPro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Admin
 */
public class TelaRelatorioRMA extends javax.swing.JInternalFrame {
 Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    /**
     * Creates new form TelaRelatorioRMA
     */
    public TelaRelatorioRMA() {
        initComponents();
        conexao = ModuloConexao.conector();
        pesquisarProcesso();
        pesquisarRma();
    }
    
    public void pesquisarProcesso() {
        

        //strftime('%d/%m/%Y', InvDate
        String sql = "select idpro as 'ID', atividadepro as 'Atividade', nomecli as 'Cliente', numpro as 'Número de Processo', tipopro as 'Tipo', DATE_FORMAT(datapro, '%d/%m/%Y') as 'Data Processo',"
                + "Date_FORMAT(dataexp,'%d/%m/%Y') as 'Data de Expedição', Date_FORMAT(dataven,'%d/%m/%Y') as 'Data de Vencimento',Date_FORMAT(datareno,'%d/%m/%Y') as 'Data de Renovação', situacao as 'Situação'  from tbprocesso inner join tbclientes on tbclientes.idcli = tbprocesso.idcli where idpro like ? or atividadepro like ? or nomecli like ? or numpro like ? ORDER BY tbprocesso.idpro DESC ";

        try {
            pst = conexao.prepareStatement(sql);
            //Passando o conteúdo da caixa de pesquisar para o ?
            //atenção ao "%" - continuaçao da string sql

            pst.setString(1, txtProPesquisar.getText() + "%");
            pst.setString(2, txtProPesquisar.getText() + "%");
            pst.setString(3, txtProPesquisar.getText() + "%");
            pst.setString(4, txtProPesquisar.getText() + "%");
            

            rs = pst.executeQuery();
            // a linha abaixo usa a biblioteca rs2xml.jar para preencher a tabela
            tblPro.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
      
       
    }
    
     private void setar_campos() {
        int setar = tblPro.getSelectedRow();

        txtIdPro.setText(tblPro.getModel().getValueAt(setar, 0).toString());
        

    }
     
     private void alterarRelatorioRMA() {
        String sql = "update tbrelatoriorma set id=?,periodo=?,quantidade=?,data=? where id=?";

        try {

            pst = conexao.prepareStatement(sql);

            pst.setString(1, txtIdRma.getText());
            pst.setString(2, txtPeriodoRma.getText());
            pst.setString(3, txtQuantidadeRma.getText());
            pst.setString(4, txtDataRma.getText());
            pst.setString(5, txtIdRma.getText());
            

           

            //As linhas abaixo valida os campos obrigatorios 
            if ((txtPeriodoRma.getText().isEmpty()) || (txtQuantidadeRma.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios");

            } else {

                // linha abaixa atualiza a tabela do banco de dados  
                // a estrura abaixo e usada para confirmar a inserção dos  na tabela
                int adicionado = pst.executeUpdate();

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "RelatórioRMA alterado com sucesso");

                    // os linhas abaixo limpa todos os campos apos adicionar
                    txtIdRma.setText(null);
                    txtPeriodoRma.setText(null);
                    txtQuantidadeRma.setText(null);
                    txtDataRma.setText(null);
                    

                    btnInserirRma.setEnabled(true);
                 
                    tblPro.setVisible(true);
                     pesquisarRma();
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
     private void setar_camposRMA() {
        int setar = tblRMA.getSelectedRow();

        txtIdRma.setText(tblRMA.getModel().getValueAt(setar, 0).toString());
        txtPeriodoRma.setText(tblRMA.getModel().getValueAt(setar, 1).toString());
        txtQuantidadeRma.setText(tblRMA.getModel().getValueAt(setar, 2).toString());
        txtDataRma.setText(tblRMA.getModel().getValueAt(setar, 3).toString());
        txtIdPro.setText(tblRMA.getModel().getValueAt(setar, 4).toString());
        
     
        

    }
     private void inserirRma() {
        String sql = "insert into tbrelatoriorma(periodo, quantidade, data, tbprocesso_idpro) values (?,?,?,?)";

        try {

            pst = conexao.prepareStatement(sql);

            pst.setString(1, txtPeriodoRma.getText());
            pst.setString(2, txtQuantidadeRma.getText());
            pst.setString(3, txtDataRma.getText());
            pst.setString(4, txtIdPro.getText());
            

            //As linhas abaixo valida os campos obrigatorios 
            if ((txtPeriodoRma.getText().isEmpty()) || (txtQuantidadeRma.getText().isEmpty()) || (txtIdPro.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campus obrigatorios");

            } else {

                // linha abaixa atualiza a tabela do banco de dados  
                // a estrura abaixo e usada para confirmar a inserção dos  na tabela
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente adicionado com sucesso");

                    // os linhas abaixo limpa todos os campos apos adicionar
                    txtIdRma.setText(null);
                    txtPeriodoRma.setText(null);
                    txtQuantidadeRma.setText(null);
                    txtDataRma.setText(null);
                    txtIdPro.setText(null);
                    
                    pesquisarRma();
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Preencha todos campos obrigatorios!!!");
        }

    }
     
      private void pesquisarRma() {
         String sql = "Select id as ID, periodo as 'Periodo', quantidade as Quantidade, data as 'Data de Relatório RMA', tbprocesso_idpro from tbrelatoriorma inner join tbprocesso on tbprocesso.idpro = tbrelatoriorma.tbprocesso_idpro where id like ? or periodo like ? or quantidade like ? or data like ?";
        try {
            pst = conexao.prepareStatement(sql);
            
            pst.setString(1, txtPesquisarRMA.getText() + "%");
            pst.setString(2, txtPesquisarRMA.getText() + "%");
            pst.setString(3, txtPesquisarRMA.getText() + "%");
            pst.setString(4, txtPesquisarRMA.getText() + "%");
 
            rs = pst.executeQuery();
            tblRMA.setModel(DbUtils.resultSetToTableModel(rs));
          

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnInserirRma = new javax.swing.JButton();
        txtIdRma = new javax.swing.JTextField();
        txtPeriodoRma = new javax.swing.JTextField();
        txtQuantidadeRma = new javax.swing.JTextField();
        txtDataRma = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtIdPro = new javax.swing.JTextField();
        btnAlterarRMA = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPro = new javax.swing.JTable();
        txtProPesquisar = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        txtPesquisarRMA = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblRMA = new javax.swing.JTable();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Cadastro de RMA"));

        jLabel1.setText("ID");

        jLabel2.setText("Periodo");

        jLabel3.setText("Quantidade");

        jLabel4.setText("Data");

        btnInserirRma.setText("Inserir");
        btnInserirRma.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnInserirRmaMouseClicked(evt);
            }
        });

        txtIdRma.setEnabled(false);

        jLabel5.setText("ID de Processo Selecionado");

        txtIdPro.setEnabled(false);

        btnAlterarRMA.setText("Alterar");
        btnAlterarRMA.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAlterarRMAMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtQuantidadeRma, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDataRma, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPeriodoRma, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIdRma, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnInserirRma)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAlterarRMA)
                                .addGap(77, 77, 77))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(150, 150, 150)
                                        .addComponent(txtIdPro, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel5))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtIdRma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtPeriodoRma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtQuantidadeRma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(txtDataRma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(txtIdPro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInserirRma)
                    .addComponent(btnAlterarRMA))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Selecionar Processo"));

        tblPro.setModel(new javax.swing.table.DefaultTableModel(
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
        tblPro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPro);

        txtProPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtProPesquisarKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1040, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtProPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(txtProPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Pesquisar Relatório RMA"));

        txtPesquisarRMA.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisarRMAKeyReleased(evt);
            }
        });

        tblRMA.setModel(new javax.swing.table.DefaultTableModel(
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
        tblRMA.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblRMAMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblRMA);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(txtPesquisarRMA, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(txtPesquisarRMA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 458, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(45, 45, 45)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(123, 123, 123))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblProMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProMouseClicked
        setar_campos();
    }//GEN-LAST:event_tblProMouseClicked

    private void btnAlterarRMAMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAlterarRMAMouseClicked
        alterarRelatorioRMA();
    }//GEN-LAST:event_btnAlterarRMAMouseClicked

    private void btnInserirRmaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnInserirRmaMouseClicked
     inserirRma();
    }//GEN-LAST:event_btnInserirRmaMouseClicked

    private void tblRMAMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRMAMouseClicked
     setar_camposRMA();
    }//GEN-LAST:event_tblRMAMouseClicked

    private void txtProPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProPesquisarKeyReleased
        pesquisarProcesso();
    }//GEN-LAST:event_txtProPesquisarKeyReleased

    private void txtPesquisarRMAKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisarRMAKeyReleased
       pesquisarRma();
    }//GEN-LAST:event_txtPesquisarRMAKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterarRMA;
    private javax.swing.JButton btnInserirRma;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblPro;
    private javax.swing.JTable tblRMA;
    private javax.swing.JTextField txtDataRma;
    private javax.swing.JTextField txtIdPro;
    private javax.swing.JTextField txtIdRma;
    private javax.swing.JTextField txtPeriodoRma;
    private javax.swing.JTextField txtPesquisarRMA;
    private javax.swing.JTextField txtProPesquisar;
    private javax.swing.JTextField txtQuantidadeRma;
    // End of variables declaration//GEN-END:variables
}
