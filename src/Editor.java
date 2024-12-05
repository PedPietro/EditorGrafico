import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Editor extends JFrame
{
    private static JButton btnPonto, btnLinha, btnCirculo, btnElipse, btnRetangulo, btnCor, btnAbrir, btnSalvar,btnApagar, btnSair;
    private static JPanel pnlBotoes;
    private static JInternalFrame frame;
    private static MeuJPanel pnlDesenho;

    private static List<Ponto> figuras = new ArrayList<Ponto>();

    private static Color corAtual = Color.black;
    private static Color Azul = Color.BLUE;
    private static Color Vermelho = Color.RED;
    private static Color Verde = Color.BLUE;

    private static JLabel statusBar1;
    private static JLabel statusBar2;

    private static boolean limparMensagem;

    private static boolean esperaPonto,
            esperaInicioReta, esperaFimReta,
            esperaInicioRaio, esperaFimRaio,
            esperaCentro, esperaInicioRaioA, esperaFimRaioA, esperaInicioRaioB, esperaFimRaioB,
            esperaInicioRetangulo, esperaFimRetangulo;

    private static Ponto p1 = new Ponto();

    private static Ponto inicioRaio = new Ponto();
    private static int tamRaioA;

    public static void main(String[] args){
        Editor aplicacao = new Editor();
        btnPonto.addActionListener(new DesenhaPonto());
        btnLinha.addActionListener(new DesenhaReta());
        btnCirculo.addActionListener(new DesenhaCirculo());
        btnElipse.addActionListener(new DesenhaOval());
        btnRetangulo.addActionListener(new DesenhaRetangulo());
        aplicacao.addWindowListener (
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e){
                        System.exit(0);
                    }
                }
        );
    }

    private class FazAbertura implements ActionListener{
        public void actionPerformed(ActionEvent e)
        {
            JFileChooser arqEscolhido = new JFileChooser();
            arqEscolhido.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = arqEscolhido.showOpenDialog(Editor.this);
            if (result == JFileChooser.APPROVE_OPTION)
            {
                File arquivo = arqEscolhido.getSelectedFile();
                System.out.println("Processando "+arquivo.getName());
                try {
                    BufferedReader arqFiguras = new BufferedReader(new FileReader(arquivo.getPath()));
                    try {
                        String linha = arqFiguras.readLine();
                        while (linha != null)
                        {
                            String tipo = linha.substring(0,5).trim();
                            int xBase = Integer.parseInt(linha.substring(5,10).trim());
                            int yBase = Integer.parseInt(linha.substring(10,15).trim());
                            int corR = Integer.parseInt(linha.substring(15,20).trim());
                            int corG = Integer.parseInt(linha.substring(20,25).trim());
                            int corB = Integer.parseInt(linha.substring(25,30).trim());
                            Color cor = new Color(corR, corG, corB);
                            System.out.println(linha);
                            switch (tipo.charAt(0))
                            {
                                case 'p' : // figura é um ponto
                                    figuras.add(new Ponto(xBase,yBase, cor));
                                    break;

                                case 'l' : // figura é uma linha
                                    int xFinal = Integer.parseInt(linha.substring(30,35).trim());
                                    int yFinal = Integer.parseInt(linha.substring(35,40).trim());
                                    figuras.add(new Linha(xBase, yBase, xFinal, yFinal, cor));
                                    break;

                                case 'c' : // figura é um círculo
                                    int raio = Integer.parseInt(linha.substring(30,35).trim());
                                    figuras.add(new Circulo(xBase, yBase, raio, cor));
                                    break;
                                case 'o' : // figura é uma oval
                                    int raioA = Integer.parseInt(linha.substring(30,35).trim());
                                    int raioB = Integer.parseInt(linha.substring(35,40).trim());
                                    figuras.add(new Oval(xBase, yBase, raioA, raioB, cor));
                                    break;
                                case 'r': // figura é um retângulo
                                    int xFim = Integer.parseInt(linha.substring(30, 35).trim());
                                    int yFim = Integer.parseInt(linha.substring(35,40).trim());
                                    figuras.add(new Retangulo(xBase, yBase, xFim, yFim, cor));
                                    break;
                            }
                            linha = arqFiguras.readLine();
                        }
                        arqFiguras.close();
                        frame.setTitle(arquivo.getName());
                        desenharObjetos(pnlDesenho.getGraphics());
                    }
                    catch (IOException ioe){
                        System.out.println("Erro de leitura no arquivo");
                    }
                }
                catch (FileNotFoundException erro) {
                    System.out.println("Arquivo não pôde ser aberto");
                }
            }
        }
    }

    private static class DesenhaPonto implements ActionListener {
        public void actionPerformed(ActionEvent e)
        {
            statusBar1.setText("Mensagem: clique o local do ponto desejado:");
            limpaEsperas();
            esperaPonto = true;
        }
    }

    private static class DesenhaReta implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            statusBar1.setText("Mensagem: clique o ponto inicial da reta");
            limpaEsperas();
            esperaInicioReta = true;
        }
    }

    private static class DesenhaCirculo implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            statusBar1.setText("Mensagem: clique o ponto inicial do raio");
            limpaEsperas();
            esperaInicioRaio = true;
        }
    }

    private static class DesenhaOval implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            statusBar1.setText("Mensagem: clique o ponto central da elipse");
            limpaEsperas();
            esperaCentro = true;
        }
    }

    private static class DesenhaRetangulo implements ActionListener{
        public void actionPerformed(ActionEvent e){
            statusBar1.setText("Mensagem: clique o ponto inicial do retângulo");
            limpaEsperas();
            esperaInicioRetangulo = true;
        }
    }

    private class MeuJPanel extends JPanel implements MouseMotionListener, MouseListener
    {
        JPanel pnlStatus = new JPanel();
        public MeuJPanel() {
            super();
            pnlStatus.setLayout(new GridLayout(1, 2));
            statusBar1 = new JLabel("Mensagem");
            statusBar2 = new JLabel("Coordenada");
            pnlStatus.add(statusBar1);
            pnlStatus.add(statusBar2);
            getContentPane().add(pnlStatus, BorderLayout.SOUTH);

            addMouseListener(this);
            addMouseMotionListener(this);
        }

        public void mouseMoved(MouseEvent e)
        {
            statusBar2.setText("Coordenada: "+e.getX()+","+e.getY());
        }

        public void mouseDragged(MouseEvent e)
        {

        }

        public void mouseClicked(MouseEvent e)
        {
            if(limparMensagem)
                statusBar1.setText("Mensagem:");
        }

        public void mousePressed(MouseEvent e)
        {
            if (esperaPonto)
            {
                Ponto novoPonto = new Ponto(e.getX(), e.getY(), corAtual);
                figuras.add(novoPonto);
                novoPonto.desenhar(novoPonto.getCor(), pnlDesenho.getGraphics());
                esperaPonto = false;
                System.out.println(figuras);
                limparMensagem = true;
            }

            else if (esperaInicioReta)
            {
                p1.setCor(corAtual);
                p1.setX(e.getX());
                p1.setY(e.getY());
                esperaInicioReta = false;
                esperaFimReta = true;
                statusBar1.setText("Mensagem: clique o ponto final da reta");
                limparMensagem = false;
            }
            else if (esperaFimReta)
            {
                esperaInicioReta = false;
                esperaFimReta = false;
                Linha novaLinha = new Linha(p1.getX(), p1.getY(), e.getX(), e.getY(), corAtual);
                figuras.add(novaLinha);
                novaLinha.desenhar(novaLinha.getCor(), pnlDesenho.getGraphics());
                limparMensagem = true;
            }

            else if (esperaInicioRaio)
            {
                inicioRaio.setCor(corAtual);
                inicioRaio.setX(e.getX());
                inicioRaio.setY(e.getY());
                esperaInicioRaio = false;
                esperaFimRaio = true;
                statusBar1.setText("Mensagem: clique o ponto final do raio");
                limparMensagem = false;
            }
            else if (esperaFimRaio)
            {
                esperaInicioRaio = false;
                esperaFimRaio = false;
                int tamRaio = (int) Math.sqrt(Math.pow(p1.getX() - e.getX(), 2) + Math.pow(p1.getY() - e.getY(), 2));
                Circulo novoCirculo = new Circulo(p1.getX(), p1.getY(), tamRaio, corAtual);
                figuras.add(novoCirculo);
                novoCirculo.desenhar(novoCirculo.getCor(), pnlDesenho.getGraphics());
                limparMensagem = true;
            }

            else if(esperaCentro){
                p1.setCor(corAtual);
                p1.setX(e.getX());
                p1.setY(e.getY());
                esperaCentro = false;
                esperaInicioRaioA = true;
                esperaFimRaioA = false;
                esperaInicioRaioB = false;
                esperaFimRaioB = false;
                statusBar1.setText("Mensagem: clique o ponto inicial do raioA (comprimento)");
                limparMensagem = false;
            }
            else if (esperaInicioRaioA)
            {
                inicioRaio.setCor(corAtual);
                inicioRaio.setX(e.getX());
                inicioRaio.setY(e.getY());
                esperaCentro = false;
                esperaInicioRaioA = false;
                esperaFimRaioA = true;
                esperaInicioRaioB = false;
                esperaFimRaioB = false;
                statusBar1.setText("Mensagem: clique o ponto final do raioA (comprimento)");
                limparMensagem = false;
            }
            else if(esperaFimRaioA)
            {
                tamRaioA = Math.abs(inicioRaio.getX() - e.getX());
                esperaCentro = false;
                esperaInicioRaioA = false;
                esperaFimRaioA = false;
                esperaInicioRaioB = true;
                esperaFimRaioB = false;
                statusBar1.setText("Mensagem: clique o ponto inicial do raioB (altura)");
                limparMensagem = false;
            }
            else if(esperaInicioRaioB){
                inicioRaio.setCor(corAtual);
                inicioRaio.setX(e.getX());
                inicioRaio.setY(e.getY());
                esperaCentro = false;
                esperaInicioRaioA = false;
                esperaFimRaioA = false;
                esperaInicioRaioB = false;
                esperaFimRaioB = true;
                statusBar1.setText("Mensagem: clique o ponto final do raioB (altura)");
                limparMensagem = false;
            }
            else if(esperaFimRaioB){
                esperaCentro = false;
                esperaInicioRaioA = false;
                esperaFimRaioA = false;
                esperaInicioRaioB = false;
                esperaFimRaioB = false;
                int tamRaioB = Math.abs(inicioRaio.getY() - e.getY());
                System.out.println(inicioRaio.getY());
                System.out.println(e.getY());
                Oval novoOval = new Oval(p1.getX(), p1.getY(), tamRaioA, tamRaioB, corAtual);
                figuras.add(novoOval);
                novoOval.desenhar(novoOval.getCor(), pnlDesenho.getGraphics());
                limparMensagem = true;
            }
            else if(esperaInicioRetangulo){
                p1.setCor(corAtual);
                p1.setX(e.getX());
                p1.setY(e.getY());
                esperaInicioRetangulo = false;
                esperaFimRetangulo = true;
                statusBar1.setText("Mensagem: clique o ponto final do retângulo");
                limparMensagem = false;
            }
            else if(esperaFimRetangulo){
                esperaInicioRetangulo = false;
                esperaFimRetangulo = false;
                Retangulo novoRetangulo = new Retangulo(p1.getX(), p1.getY(), e.getX(), e.getY(), corAtual);
                figuras.add(novoRetangulo);
                novoRetangulo.desenhar(novoRetangulo.getCor(), pnlDesenho.getGraphics());
                limparMensagem = true;
            }
        }

        public void mouseEntered(MouseEvent e)
        {

        }

        public void mouseExited(MouseEvent e)
        {

        }

        public void mouseReleased(MouseEvent e)
        {

        }

        public void paintComponent(Graphics g)
        {
            for(int atual = 0; atual < figuras.size(); atual++)
            {
                Ponto figuraAtual = (Ponto) figuras.get(atual);
                figuraAtual.desenhar(figuraAtual.getCor(), g);
            }
        }
    }

    public static void desenharObjetos(Graphics g)
    {
        pnlDesenho.paintComponent(g);
    }

    private static void limpaEsperas()
    {
        esperaPonto = false;

        esperaInicioReta = false;
        esperaFimReta = false;

        esperaInicioRaio = false;
        esperaFimRaio = false;

        esperaCentro = false;
        esperaInicioRaioA = false;
        esperaFimRaioA = false;
        esperaInicioRaioB = false;
        esperaFimRaioB = false;

        esperaInicioRetangulo = false;
        esperaFimRetangulo = false;
    }

    public Editor()
    {
        super("Editor Gráfico");
        Icon imgAbrir = new ImageIcon("./src/Imagens/abrir.jpg");
        btnAbrir = new JButton("Abrir", imgAbrir);
        btnSalvar = new JButton("Salvar", new ImageIcon("./src/Imagens/abrir.jpg"));
        btnPonto = new JButton("Ponto", new ImageIcon("./src/Imagens/ponto.jpg"));
        btnLinha = new JButton("Linha", new ImageIcon("./src/Imagens/linha.jpg"));
        btnCirculo = new JButton("Circulo", new ImageIcon("./src/Imagens/circulo.jpg"));
        btnElipse = new JButton("Elipse", new ImageIcon("./src/Imagens/elipse.jpg"));
        btnRetangulo = new JButton("Retangulo", new ImageIcon(""));
        btnCor = new JButton("Cores", new ImageIcon("./src/Imagens/cores.jpg"));
        btnApagar = new JButton("Apagar", new ImageIcon("./src/Imagens/apagar.jpg"));
        btnSair = new JButton("Sair", new ImageIcon("./src/Imagens/sair.jpg"));

        btnAbrir.setPreferredSize(new Dimension(100,26));
        btnSalvar.setPreferredSize(new Dimension(100,26));
        btnPonto.setPreferredSize(new Dimension(100,26));
        btnLinha.setPreferredSize(new Dimension(100,26));
        btnCirculo.setPreferredSize(new Dimension(100,26));
        btnElipse.setPreferredSize(new Dimension(100,26));
        btnRetangulo.setPreferredSize(new Dimension(100,26));
        btnCor.setPreferredSize(new Dimension(100,26));
        btnApagar.setPreferredSize(new Dimension(100,26));
        btnSair.setPreferredSize(new Dimension(100,26));

        pnlBotoes = new JPanel();
        FlowLayout flwBotoes = new FlowLayout();
        pnlBotoes.setLayout(flwBotoes);

        pnlBotoes.add(btnAbrir);
        pnlBotoes.add(btnSalvar);
        pnlBotoes.add(btnPonto);
        pnlBotoes.add(btnLinha);
        pnlBotoes.add(btnCirculo);
        pnlBotoes.add(btnElipse);
        pnlBotoes.add(btnRetangulo);
        pnlBotoes.add(btnCor);
        pnlBotoes.add(btnApagar);
        pnlBotoes.add(btnSair);

        setSize(1000,800);
        setVisible(true);

        Container cntForm = getContentPane();
        cntForm.setLayout(new BorderLayout());
        cntForm.add(pnlBotoes , BorderLayout.NORTH);

        JDesktopPane panDesenho = new JDesktopPane();
        cntForm.add(panDesenho);

        frame = new JInternalFrame("Nenhum arquivo aberto", true, true, true, true);
        panDesenho.add(frame);
        frame.setSize(this.getWidth() / 2,this.getHeight() / 2);
        frame.show();
        frame.setOpaque(true);

        pnlDesenho = new MeuJPanel();
        Container cntFrame = frame.getContentPane();
        cntFrame.add(pnlDesenho);

        btnAbrir.addActionListener(new FazAbertura());

        show();
    }
}
