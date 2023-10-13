import model.dao.DaoFactory;
import model.dao.UsuarioDao;
import model.entities.Usuario;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Usucamp extends  JFrame{
    private int ID_MOD = -1;
    private JPanel usucamp;
    private JButton voltarButton;
    private JTextField nomeField;
    private JTextField cpfField;
    private JTextField empresaField;
    private JButton inserirButton;
    private JLabel empresa;

    private JList<Usuario> list1;
    private JButton atualizarButton;
    private JButton deletarButton;
    private JList<Usuario> userList; // Usaremos uma lista de usuários aqui

    private DefaultListModel<Usuario> listModel;


    public Usucamp() {
        listModel = new DefaultListModel<>(); // Inicialize o modelo da lista
        list1.setModel(listModel);
        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false); // Oculta a instância atual de UsuarioForm
                MainUi main = new MainUi(); // Passe a instância de UsuarioForm para Usucamp
                main.setContentPane(main.getMainPanel());
                main.setSize(500, 500);
                main.setLocationRelativeTo(null);
                main.setVisible(true);
            }
        });
        inserirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String nome = nomeField.getText();
                String cpf = cpfField.getText();
                String empresaText = empresaField.getText();
                if (nome.isEmpty() || cpf.isEmpty() || empresaText.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Preencha todos os campos antes de inserir um usuário.", "Erro", JOptionPane.ERROR_MESSAGE);
                } else {

                    try {
                        Integer id_empresa = Integer.parseInt(empresaText);
                        UsuarioDao usuarioDao = DaoFactory.createUsuarioDao();
                        Usuario usu = new Usuario();
                        usu.setNome(nome);
                        usu.setCpf(cpf);
                        usu.setId_empresa(id_empresa);
                        usuarioDao.insert(usu);
                        loadUserList();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "O campo Empresa deve ser um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        loadUserList();
        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                System.out.println(e);
                if (list1.getSelectedValue() != null) {
                    ID_MOD = list1.getSelectedValue().getId();
                    nomeField.setText(list1.getSelectedValue().getNome());
                    cpfField.setText(list1.getSelectedValue().getCpf());
                    empresaField.setText(Integer.toString( list1.getSelectedValue().getId_empresa()));

                }

            }
        });
        deletarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UsuarioDao usuarioDao = DaoFactory.createUsuarioDao();
                if(ID_MOD == -1){
                    JOptionPane.showMessageDialog(null, "Selecione o dado a ser deletado.", "Erro", JOptionPane.ERROR_MESSAGE);
                }else {
                    int confirm = JOptionPane.showConfirmDialog(null, "Tem certeza? Essa acao nao sera reversivel", "Erro", JOptionPane.OK_CANCEL_OPTION);
                    if(confirm == 0) {
                        usuarioDao.deleteById(ID_MOD);
                        loadUserList();
                    }
                    nomeField.setText("");
                    cpfField.setText("");
                    empresaField.setText("");
                    ID_MOD = -1;
                }
            }
        });


        atualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(ID_MOD == -1){
                    ID_MOD = -1;
                    JOptionPane.showMessageDialog(null, "Selecione o dado a ser deletado.", "Erro", JOptionPane.ERROR_MESSAGE);
                }else {
                    String nome = nomeField.getText();
                    String cpf = cpfField.getText();
                    String empresaText = empresaField.getText();
                    if (nome.isEmpty() || cpf.isEmpty() || empresaText.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Preencha todos os campos antes de atualizar um usuário.", "Erro", JOptionPane.ERROR_MESSAGE);
                    } else {
                        UsuarioDao usuarioDao = DaoFactory.createUsuarioDao();
                        Usuario usuAtt = new Usuario();
                        usuAtt.setId(ID_MOD);
                        usuAtt.setNome(nome);
                        usuAtt.setCpf(cpf);
                        usuAtt.setId_empresa(Integer.parseInt(empresaText));
                        usuarioDao.update(usuAtt);
                        loadUserList();
                        nomeField.setText("");
                        cpfField.setText("");
                        empresaField.setText("");
                        ID_MOD = -1;
                    }
                }

            }
        });
    }




    // Método para carregar a lista de usuários
    private void loadUserList() {
        UsuarioDao usuarioDao = DaoFactory.createUsuarioDao();
        List<Usuario> usuarios = usuarioDao.findAll(); // Suponha que você tenha um método findAll no seu DAO

        listModel.clear();
        for (Usuario usuario : usuarios) {
            listModel.addElement(usuario);

        }
    }

    // Resto do seu código

    public JPanel getUsucamp() {
        return usucamp;
    }

    public void setUsucamp(JPanel usucamp) {
        this.usucamp = usucamp;
    }



    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
