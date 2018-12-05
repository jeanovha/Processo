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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Admin
 */
public class TelaProcesso extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    String CLASS = "";
   

    /**
     * Creates new form TelaProcesso
     */
    public TelaProcesso() {
        initComponents();
        conexao = ModuloConexao.conector();
        dispose();
        pesquisar_cliente();
        pesquisarProcesso();
        
        
    
        
       
       

        Date dataPro = new Date();
        DateFormat formatador = DateFormat.getDateInstance(DateFormat.SHORT);
        txtProDataPro.setText(formatador.format(dataPro));

    }

    private void pesquisar_cliente() {
        String sql = "Select idcli as ID, nomecli as Nome, fonecli as Telefone from tbclientes where nomecli like ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtCliPesqisar.getText() + "%");
            rs = pst.executeQuery();
            tblClientes.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void setar_campos() {
        int setar = tblClientes.getSelectedRow();

        txtCliId.setText(tblClientes.getModel().getValueAt(setar, 0).toString());
        txtNomeCli.setText(tblClientes.getModel().getValueAt(setar, 1).toString());

    }

    public String dataRenovacao() throws ParseException {

        String dataRen = txtProDataVen.getText();
        Date data = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(dataRen));
        cal.add(Calendar.DAY_OF_MONTH, -120);
        data = cal.getTime();

        SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM-/dd");

        formato.applyPattern("dd/MM/yyyy");

        String dataFor = formato.format(data);

        return dataFor;

    }

    private void inserir_processo() {

        String sql = "insert into tbprocesso(atividadepro,numpro,tipopro,datapro,dataexp,dataven,datareno,idcli)values (?,?,?,?,?,?,?,?)";

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtProAtividade.getText());
            pst.setString(2, txtProNumPro.getText());
            pst.setString(3, txtProTipoPro.getText());
            pst.setString(4, dataMysql(txtProDataPro.getText()));
            pst.setString(5, dataMysql(txtProDataExp.getText()));
            pst.setString(6, dataMysql(txtProDataVen.getText()));
            pst.setString(7, dataMysql(dataRenovacao()));
            pst.setString(8, txtCliId.getText());

            if ((txtProNumPro.getText().isEmpty()) || (txtCliId.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios", null, JOptionPane.OK_OPTION);
            } else {

                // linha abaixa atualiza a tabela do banco de dados  
                // a estrura abaixo e usada para confirmar a inserção dos  na tabela
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Processo adicionado com sucesso", null, JOptionPane.YES_OPTION);

                    // os linhas abaixo limpa todos os campos apos adicionar
                    txtProNumPro.setText(null);
                    txtProDataPro.setText(null);
                    txtProDataExp.setText(null);
                    txtProDataVen.setText(null);
                    txtProDataReno.setText(null);
                    txtCliId.setText(null);
                    txtNomeCli.setText(null);
                    txtProAtividade.setText(null);

                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        pesquisarProcesso();
    }

    public String dataMysql(String dataCampo) {
        String dia = dataCampo.substring(0, 2);

        String mes = dataCampo.substring(3, 5);

        String ano = dataCampo.substring(6, 10);

        String data = ano + "-" + mes + "-" + dia;

        return data;

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
        dataTeste();
        mudarCor();
       
    }

    public void setar_campos_Pro() {

        int setar = tblPro.getSelectedRow();

        txtProId.setText(tblPro.getModel().getValueAt(setar, 0).toString());

        txtProAtividade.setText(tblPro.getModel().getValueAt(setar, 1).toString());
        txtProNumPro.setText(tblPro.getModel().getValueAt(setar, 3).toString());
        txtProTipoPro.setText(tblPro.getModel().getValueAt(setar, 4).toString());
        txtNomeCli.setText(tblPro.getModel().getValueAt(setar, 2).toString());
        txtProDataPro.setText(tblPro.getModel().getValueAt(setar, 5).toString());
        txtProDataExp.setText(tblPro.getModel().getValueAt(setar, 6).toString());
        txtProDataVen.setText(tblPro.getModel().getValueAt(setar, 7).toString());
        txtProDataReno.setText(tblPro.getModel().getValueAt(setar, 8).toString());
        //txtCliId.setText(tblPro.getModel().getValueAt(setar, 8).toString());
        // a linha abaixo desabilita o botão adicionar
        btnProInserir.setEnabled(false);
        txtCliPesqisar.setEnabled(false);
        tblClientes.setVisible(false);
         
 
    }

    private void alterarProcesso() {
        String sql = "update tbprocesso set atividadepro=?,numpro=?,tipopro=?,datapro=?,dataexp=?,dataven=?, datareno=? where idpro=?";

        try {

            pst = conexao.prepareStatement(sql);

            pst.setString(1, txtProAtividade.getText());
            pst.setString(2, txtProNumPro.getText());
            pst.setString(3, txtProTipoPro.getText());
            pst.setString(4, dataMysql(txtProDataPro.getText()));
            pst.setString(5, dataMysql(txtProDataExp.getText()));
            pst.setString(6, dataMysql(txtProDataVen.getText()));
            pst.setString(7, dataMysql(dataRenovacao()));

            pst.setString(8, txtProId.getText());

            //As linhas abaixo valida os campos obrigatorios 
            if ((txtProNumPro.getText().isEmpty()) || (txtProAtividade.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios");

            } else {

                // linha abaixa atualiza a tabela do banco de dados  
                // a estrura abaixo e usada para confirmar a inserção dos  na tabela
                int adicionado = pst.executeUpdate();

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Processo alterado com sucesso");

                    // os linhas abaixo limpa todos os campos apos adicionar
                    txtProAtividade.setText(null);
                    txtProNumPro.setText(null);
                    txtProTipoPro.setText(null);
                    txtProDataPro.setText(null);
                    txtProDataExp.setText(null);
                    txtProDataVen.setText(null);
                    txtProDataReno.setText(null);
                    txtCliId.setText(null);
                    txtNomeCli.setText(null);
                    txtProId.setText(null);

                    btnProInserir.setEnabled(true);
                    txtCliPesqisar.setEnabled(true);
                    tblClientes.setVisible(true);
                    
                    dataTeste();

                    pesquisarProcesso();

                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void excluirProceswso() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir o processo", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from tbprocesso where idpro=?";

            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtProId.getText());
                int apagado = pst.executeUpdate();

                System.out.println(apagado);

                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Proceso removido com sucesso");

                    txtProAtividade.setText(null);
                    txtProNumPro.setText(null);
                    txtProTipoPro.setText(null);
                    txtProDataPro.setText(null);
                    txtProDataExp.setText(null);
                    txtProDataVen.setText(null);
                    txtProDataReno.setText(null);

                    pesquisarProcesso();
                }
            } catch (Exception e) {

                JOptionPane.showMessageDialog(null, e);

            }

        }
        pesquisarProcesso();
    }

    public void dataTeste() {
        String situacao = null;

        String sql = "select * from tbprocesso";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {

                String ati = rs.getString("dataven");

                Calendar c = Calendar.getInstance();

                Date hoje = new Date();

                c.add(Calendar.DAY_OF_MONTH, 120);

                hoje = c.getTime();

                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                Date data = formato.parse(ati);
                formato.applyPattern("dd/MM/yyyy");

                if (hoje.after(data)) {
                    situacao = "Vencendo";

                } else {
                    situacao = "Pendente";

                }

                //--------------------------------------------------------------------------------------------------------------------------------------
                if (situacao.equals("Vencendo")) {

                    String sql2 = "update tbprocesso set situacao=? where idpro=?";

                    try {
                        pst = conexao.prepareStatement(sql2);

                        pst.setString(1, situacao);

                        pst.setString(2, rs.getString("idpro"));

                        pst.executeUpdate();

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);
                    }

                } else {
                    String sql2 = "update tbprocesso set situacao=? where idpro=?";

                    try {
                        pst = conexao.prepareStatement(sql2);

                        pst.setString(1, situacao);
                        pst.setString(2, rs.getString("idpro"));

                        pst.executeUpdate();

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);
                    }

                }

                //---------------------------------------------------------------------------------------------------------------------------------------  
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*
    DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            String str = (String) value;
            if ("PAGAMENTO EFETUADO".equals(str)) {
                c.setForeground(java.awt.Color.RED);
            } else {
                c.setForeground(java.awt.Color.BLUE);
            }
            return c;

        }

    };
*/
    
    public void mudarCor(){
 
        CLASS = "Vencendo";
        tblPro.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFous, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFous, row, column);

                //-----------------------------------------------------------------------------------------------------
                //Color c = Color.WHITE;
                //label.setForeground(java.awt.Color.BLACK);
                tblPro.setSelectionBackground(java.awt.Color.getHSBColor(27,187,125));
                Object texto = table.getValueAt(row, 9);
                
                if(CLASS.equals(texto)){
                    setForeground(java.awt.Color.RED);
                }else{
                    setForeground(Color.black);
                }
                //tblPro.setSelectionBackground(new java.awt.Color(51, 153,255));
                
                
                
                /*
                if (texto.equals(CLASS)) {

                    label.setForeground(java.awt.Color.BLUE);
                } else {
                    label.setForeground(java.awt.Color.RED);
                }

                //Cor da linha quando clicada
                
                */
                return label;
                
            }
           
        }
         );
    }
   

        //------------------------------------------------------------------------------------------------------------------------------------
    
     
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
        txtProNumPro = new javax.swing.JTextField();
        txtProDataPro = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        txtProAtividade = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtProTipoPro = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtProDataExp = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        txtProDataReno = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        txtProId = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtProDataVen = new javax.swing.JFormattedTextField();
        btnProInserir = new javax.swing.JButton();
        btnProAlterar = new javax.swing.JButton();
        btnProPes = new javax.swing.JButton();
        btnProExcluir = new javax.swing.JButton();
        txtNomeCli = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtCliPesqisar = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtCliId = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        txtProPesquisar = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPro = new javax.swing.JTable();
        btnFecha = new javax.swing.JButton();

        setBackground(java.awt.SystemColor.controlShadow);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setTitle("Processo");
        setVisible(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel1.setText("* Nº Processo");

        jLabel2.setText("Data");

        try {
            txtProDataPro.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtProDataPro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProDataProActionPerformed(evt);
            }
        });

        jLabel3.setText("Atividade");

        jLabel6.setText("Tipo Processo");

        jLabel7.setText("Data Expedição");

        try {
            txtProDataExp.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel9.setText("Data Renovação");

        try {
            txtProDataReno.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtProDataReno.setEnabled(false);
        txtProDataReno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProDataRenoActionPerformed(evt);
            }
        });

        jLabel10.setText("ID");

        txtProId.setEnabled(false);

        jLabel8.setText("Data Vencimento");

        try {
            txtProDataVen.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        btnProInserir.setText("Inserir");
        btnProInserir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnProInserir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProInserirActionPerformed(evt);
            }
        });

        btnProAlterar.setText("Alterar");
        btnProAlterar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnProAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProAlterarActionPerformed(evt);
            }
        });

        btnProPes.setText("Novo");
        btnProPes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnProPes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProPesActionPerformed(evt);
            }
        });

        btnProExcluir.setText("Excluir");
        btnProExcluir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnProExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProExcluirActionPerformed(evt);
            }
        });

        txtNomeCli.setEnabled(false);

        jLabel12.setText("Cliente");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtProTipoPro, javax.swing.GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE)
                            .addComponent(txtProAtividade)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel9)
                                            .addComponent(jLabel8))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtProDataReno, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtProDataVen, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(262, 262, 262))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtProDataExp, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNomeCli, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(txtProId, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(95, 95, 95)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtProNumPro, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(72, 72, 72)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtProDataPro, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(86, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(btnProInserir)
                .addGap(84, 84, 84)
                .addComponent(btnProAlterar)
                .addGap(64, 64, 64)
                .addComponent(btnProPes)
                .addGap(64, 64, 64)
                .addComponent(btnProExcluir)
                .addGap(35, 224, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel10)
                    .addComponent(txtProId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProNumPro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProDataPro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtProAtividade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtProTipoPro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtProDataExp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNomeCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtProDataVen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtProDataReno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnProInserir)
                    .addComponent(btnProAlterar)
                    .addComponent(btnProPes)
                    .addComponent(btnProExcluir))
                .addGap(26, 26, 26))
        );

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Selecione o Cliente"));

        txtCliPesqisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCliPesqisarKeyReleased(evt);
            }
        });

        jLabel4.setText("Pesquisar");

        jLabel5.setText("* ID");

        txtCliId.setEnabled(false);

        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Nome", "Telefone"
            }
        ));
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblClientes);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtCliPesqisar, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addGap(51, 51, 51)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCliId, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(96, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCliPesqisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(txtCliId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Pesquisar Processo"));

        txtProPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProPesquisarActionPerformed(evt);
            }
        });
        txtProPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtProPesquisarKeyReleased(evt);
            }
        });

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/processo/icones/images.jpg"))); // NOI18N

        tblPro.setAutoCreateRowSorter(true);
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
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1208, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtProPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtProPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btnFecha.setBackground(new java.awt.Color(255, 102, 102));
        btnFecha.setText("Fechar");
        btnFecha.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFechaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(91, 91, 91)
                                .addComponent(btnFecha))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 1232, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnFecha)
                        .addGap(30, 30, 30))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        jPanel2.getAccessibleContext().setAccessibleName(" Cliente");

        setBounds(0, 0, 1291, 608);
    }// </editor-fold>//GEN-END:initComponents

    private void txtCliPesqisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCliPesqisarKeyReleased
        // TODO add your handling code here:
        pesquisar_cliente();
    }//GEN-LAST:event_txtCliPesqisarKeyReleased

    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesMouseClicked
        setar_campos();        // TODO add your handling code here:
    }//GEN-LAST:event_tblClientesMouseClicked

    private void btnProInserirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProInserirActionPerformed
        inserir_processo();
    }//GEN-LAST:event_btnProInserirActionPerformed

    private void btnProPesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProPesActionPerformed
        btnProInserir.setEnabled(true);
        txtCliPesqisar.setEnabled(true);
        tblClientes.setVisible(true);

        txtProAtividade.setText(null);
        txtProNumPro.setText(null);
        txtProTipoPro.setText(null);
        txtProDataPro.setText(null);
        txtProDataExp.setText(null);
        txtProDataVen.setText(null);
        txtProDataReno.setText(null);
        txtCliId.setText(null);
        txtProId.setText(null);
        txtNomeCli.setText(null);


    }//GEN-LAST:event_btnProPesActionPerformed

    private void txtProPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProPesquisarKeyReleased
        pesquisarProcesso();      // TODO add your handling code here:
    }//GEN-LAST:event_txtProPesquisarKeyReleased

    private void tblProMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProMouseClicked
        setar_campos_Pro();
    }//GEN-LAST:event_tblProMouseClicked

    private void btnProAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProAlterarActionPerformed
        alterarProcesso();        // TODO add your handling code here:
    }//GEN-LAST:event_btnProAlterarActionPerformed

    private void btnProExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProExcluirActionPerformed
        excluirProceswso();        // TODO add your handling code here:
    }//GEN-LAST:event_btnProExcluirActionPerformed

    private void btnFechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFechaActionPerformed
        dispose();
    }//GEN-LAST:event_btnFechaActionPerformed

    private void txtProPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProPesquisarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProPesquisarActionPerformed

    private void txtProDataProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProDataProActionPerformed

    }//GEN-LAST:event_txtProDataProActionPerformed

    private void txtProDataRenoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProDataRenoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProDataRenoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFecha;
    private javax.swing.JButton btnProAlterar;
    private javax.swing.JButton btnProExcluir;
    private javax.swing.JButton btnProInserir;
    private javax.swing.JButton btnProPes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTable tblPro;
    private javax.swing.JTextField txtCliId;
    private javax.swing.JTextField txtCliPesqisar;
    private javax.swing.JTextField txtNomeCli;
    private javax.swing.JTextField txtProAtividade;
    private javax.swing.JFormattedTextField txtProDataExp;
    private javax.swing.JFormattedTextField txtProDataPro;
    private javax.swing.JFormattedTextField txtProDataReno;
    private javax.swing.JFormattedTextField txtProDataVen;
    private javax.swing.JTextField txtProId;
    private javax.swing.JTextField txtProNumPro;
    private javax.swing.JTextField txtProPesquisar;
    private javax.swing.JTextField txtProTipoPro;
    // End of variables declaration//GEN-END:variables
}
