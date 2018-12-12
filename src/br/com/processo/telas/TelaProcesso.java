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
            pst.setString(3, txtProTipoPro.getSelectedItem().toString());
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
                    
                    dataTeste();

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
        
        
        //dataTeste();
        mudarCor();
       
    }

    public void setar_campos_Pro() {

        int setar = tblPro.getSelectedRow();

        txtProId.setText(tblPro.getModel().getValueAt(setar, 0).toString());

        txtProAtividade.setText(tblPro.getModel().getValueAt(setar, 1).toString());
        txtProNumPro.setText(tblPro.getModel().getValueAt(setar, 3).toString());
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
            pst.setString(3, txtProTipoPro.getSelectedItem().toString());
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
            rs.close();

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
                    setForeground(java.awt.Color.getHSBColor(255, 252, 59));
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
        jLabel7 = new javax.swing.JLabel();
        txtProDataExp = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        txtProDataVen = new javax.swing.JFormattedTextField();
        btnProInserir = new javax.swing.JButton();
        btnProAlterar = new javax.swing.JButton();
        btnProPes = new javax.swing.JButton();
        btnProExcluir = new javax.swing.JButton();
        btnFecha = new javax.swing.JButton();
        txtProTipoPro = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtProId = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtNomeCli = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtProDataReno = new javax.swing.JFormattedTextField();
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

        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setTitle("Processo");
        setFrameIcon(null);
        setVisible(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastro de Processo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12), new java.awt.Color(255, 0, 0))); // NOI18N
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel1.setText("* Nº Processo");

        jLabel2.setText("Data do Processo");

        txtProNumPro.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        try {
            txtProDataPro.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtProDataPro.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtProDataPro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProDataProActionPerformed(evt);
            }
        });

        jLabel3.setText("Atividade");

        txtProAtividade.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel6.setText("Tipo Processo");

        jLabel7.setText("Data Expedição");

        try {
            txtProDataExp.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtProDataExp.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel8.setText("Data Vencimento");

        try {
            txtProDataVen.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtProDataVen.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

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

        btnFecha.setBackground(new java.awt.Color(255, 102, 102));
        btnFecha.setText("Fechar");
        btnFecha.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFechaActionPerformed(evt);
            }
        });

        txtProTipoPro.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtProTipoPro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Licença Previa", "Licença de Instalação", "Licença de Operação", "Renovação de Licença" }));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Itens Gerado Automatico"));

        jLabel10.setText("ID");

        txtProId.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtProId.setEnabled(false);

        jLabel12.setText("Cliente Selecionado");

        txtNomeCli.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNomeCli.setEnabled(false);

        jLabel9.setText("Data Renovação");

        try {
            txtProDataReno.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtProDataReno.setEnabled(false);
        txtProDataReno.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtProDataReno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProDataRenoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel10)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtProId, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNomeCli, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProDataReno, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtProId, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNomeCli, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(25, 25, 25)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtProDataReno, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnProInserir)
                        .addGap(68, 68, 68)
                        .addComponent(btnProPes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnProAlterar)
                        .addGap(26, 26, 26)
                        .addComponent(btnProExcluir)
                        .addGap(32, 32, 32)
                        .addComponent(btnFecha))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtProNumPro, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProDataPro, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProAtividade, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProTipoPro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProDataExp, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProDataVen, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(58, 58, 58)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtProNumPro, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtProDataPro, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtProAtividade, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                            .addComponent(jLabel3))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtProTipoPro, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtProDataExp, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtProDataVen, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(jLabel8))
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnProInserir)
                    .addComponent(btnProAlterar)
                    .addComponent(btnProPes)
                    .addComponent(btnProExcluir)
                    .addComponent(btnFecha))
                .addGap(14, 14, 14))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Selecione Cliente  "));

        txtCliPesqisar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCliPesqisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCliPesqisarKeyReleased(evt);
            }
        });

        jLabel4.setText("Pesquisar");

        jLabel5.setText("* ID");

        txtCliId.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
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
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtCliPesqisar, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtCliId, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jLabel5)
                        .addComponent(txtCliId, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtCliPesqisar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pesquisar Processo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(255, 0, 0))); // NOI18N

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
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtProPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtProPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(19, 19, 19)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 194, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.getAccessibleContext().setAccessibleName(" Cliente");

        setBounds(0, 0, 1262, 608);
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
    private javax.swing.JPanel jPanel4;
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
    private javax.swing.JComboBox<String> txtProTipoPro;
    // End of variables declaration//GEN-END:variables
}
