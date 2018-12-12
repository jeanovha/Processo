/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.processo.telas;
import br.com.processo.DAO.ModuloConexao;
import java.awt.Color;
import java.awt.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Admin
 */
public class TelaRelatorio extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    String CLASS ;

    /**
     * Creates new form TelaRelatorio
     */
    public TelaRelatorio() {
        initComponents();
 
        
                
        conexao = ModuloConexao.conector();
        pesquisarProcesso(); 
        
        
       
        
    }

    private void pesquisarProcesso() {
       //strftime('%d/%m/%Y', InvDate
        String sql = "select p.idpro as 'ID', p.atividadepro as 'Atividade', c.nomecli as 'Cliente', p.numpro as 'Número de Processo', p.tipopro as 'Tipo',"
                + "Date_FORMAT(p.dataexp,'%d/%m/%Y') as 'Data de Expedição', Date_FORMAT(p.dataven,'%d/%m/%Y') as 'Data de Vencimento', Date_FORMAT(p.datareno,'%d/%m/%Y') as 'Data de Renovação', Date_FORMAT(r.data,'%d/%m/%Y')as 'Data de Relatório RMA', p.situacao as 'Situação' from tbprocesso as p "
                + "inner join tbclientes as c on c.idcli = p.idcli"
                + " inner join tbrelatoriorma as r on r.tbprocesso_idpro = p.idpro "
                + "where p.idpro like ? or p.atividadepro like ? or c.nomecli like ? or p.numpro like ? ORDER BY p.idpro DESC ";

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
        /*
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 12);
        Date d = c.getTime();
        System.out.println(d);        
        d = new Date();
        c.setTime(d);
        c.add(Calendar.DAY_OF_MONTH, 1);
        d = c.getTime();
        System.out.println(d);
        
         */
         mudarCor();
    }
    
    public void mudarCor(){
 
        CLASS = "Vencido";
        tblPro.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFous, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFous, row, column);

                //-----------------------------------------------------------------------------------------------------
                //Color c = Color.WHITE;
                //label.setForeground(java.awt.Color.BLACK);
                tblPro.setSelectionBackground(java.awt.Color.GREEN);
                
                Object texto = table.getValueAt(row, 9);
                
                
                
                
                if(CLASS.equals(texto)){
                    setForeground(Color.RED);
                   
                }else{
                   setForeground(java.awt.Color.BLACK);
                }
                tblPro.setSelectionBackground(new java.awt.Color(51, 153,255));
                
                
                
                /*
                Object texto = table.getValueAt(row, 9);

                if (texto.equals(CLASS)) {

                    label.setForeground(java.awt.Color.BLUE);
                } else {
                    label.setForeground(java.awt.Color.RED);
                }

                //Cor da linha quando clicada
                
                */
                return label;
                
            }

            private Object value(int i, int par) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
           
        }
         );
       
   }
        
    /*
     public void setar_campos_Pro() {
        int setar = tblPro.getSelectedRow();
        
        txtProId.setText(tblPro.getModel().getValueAt(setar, 0).toString());
        txtProAtividade.setText(tblPro.getModel().getValueAt(setar, 1).toString());
        txtProNumPro.setText(tblPro.getModel().getValueAt(setar, 2).toString());
        txtProTipoPro.setText(tblPro.getModel().getValueAt(setar, 3).toString());
        txtProDataPro.setText(tblPro.getModel().getValueAt(setar, 4).toString());
        txtProDataExp.setText(tblPro.getModel().getValueAt(setar, 5).toString());
        txtProDataVen.setText(tblPro.getModel().getValueAt(setar, 6).toString());
        txtProDataReno.setText(tblPro.getModel().getValueAt(setar, 7).toString());
        txtCliId.setText(tblPro.getModel().getValueAt(setar, 8).toString());
        // a linha abaixo desabilita o botão adicionar
        
        btnProInserir.setEnabled(false);
        txtCliPesqisar.setEnabled(false);
        tblClientes.setVisible(false);
        */
       


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        txtProPesquisar = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPro = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Relatorio");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Pesquisar Processo"));

        txtProPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtProPesquisarKeyReleased(evt);
            }
        });

        tblPro.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
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
        jScrollPane2.setViewportView(tblPro);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtProPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 853, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtProPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 110, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        setBounds(0, 0, 1031, 546);
    }// </editor-fold>//GEN-END:initComponents

    private void txtProPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProPesquisarKeyReleased
        pesquisarProcesso();   
        
    }//GEN-LAST:event_txtProPesquisarKeyReleased

    private void tblProMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProMouseClicked
       pesquisarProcesso();
       
    }//GEN-LAST:event_tblProMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    public static javax.swing.JTable tblPro;
    private javax.swing.JTextField txtProPesquisar;
    // End of variables declaration//GEN-END:variables
}
