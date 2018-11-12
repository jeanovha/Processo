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
        String sql = "select idpro as 'ID', atividadepro as 'Atividade', nomecli as 'Cliente', numpro as 'Número de Processo', tipopro as 'Tipo', DATE_FORMAT(datapro, '%d/%m/%Y') as 'Data Processo',"
                + "Date_FORMAT(dataexp,'%d/%m/%Y') as 'Data de Expedição', Date_FORMAT(dataven,'%d/%m/%Y') as 'Data de Vencimento',Date_FORMAT(datareno,'%d/%m/%Y') as 'Data de Renovação', situacao as 'Situação'  from tbprocesso inner join tbclientes on tbclientes.idcli = tbprocesso.idcli where idpro like ? or atividadepro like ? or nomecli like ? or numpro like ? ORDER BY tbprocesso.situacao = 'Vencido', tbprocesso.idpro DESC ";

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
        jLabel11 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPro = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Relatorio");

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtProPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtProPesquisarKeyReleased(evt);
            }
        });

        jLabel11.setText("Pesquisar Proceso");

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
                .addContainerGap()
                .addComponent(txtProPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 981, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtProPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(48, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1015, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 516, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
    private javax.swing.JLabel jLabel11;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    public static javax.swing.JTable tblPro;
    private javax.swing.JTextField txtProPesquisar;
    // End of variables declaration//GEN-END:variables
}
