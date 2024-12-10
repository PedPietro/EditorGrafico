package program;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class Editor extends JFrame{
    private JButton btnAbrir, btnSalvar, btnPonto, btnLinha, btnCirculo, btnElipse, btnRetangulo, btnPolilinha, btnSelecionar, btnDesselecionar, btnDeslocar, btnCores, btnApagar, btnLimpar;
    private JPanel pnlBotoes;
    private static JInternalFrame frame;
    private JDesktopPane pane;
    private static MeuJPanel desenhoPnl;

    private static int tamanhoInicial = 100, qtosDados = 0, qtosSelecionados = 0;
    private static Ponto[] figuras = new Ponto[tamanhoInicial];
    private static int[] destaques = new int[tamanhoInicial];
    private static Ponto pontoBuffer;
    private static Polilinha novaPolilinha;

    private JLabel statusBar1, statusBar2;

    private Color currentColor = Color.BLACK;
    private static boolean esperaPonto, esperaLinhaIni, esperaLinhaFim, esperaCirculoIni, esperaCirculoFim, esperaOvalIni, esperaOvalFim, esperaRetanguloIni, esperaRetanguloFim, esperaPolilinhaIni, esperaPolilinhaFim;

    public Editor(){
        super("Editor GRAPH");
        setSize(1400, 600);

        btnAbrir = new JButton("Abrir", new ImageIcon(getClass().getResource("../sprites/abrir.jpg")));
        btnSalvar = new JButton("Salvar", new ImageIcon(getClass().getResource("../sprites/salvar.jpg")));
        btnPonto = new JButton("Ponto", new ImageIcon(getClass().getResource("../sprites/ponto.jpg")));
        btnLinha = new JButton("Linha", new ImageIcon(getClass().getResource("../sprites/linha.jpg")));
        btnCirculo = new JButton("Circulo", new ImageIcon(getClass().getResource("../sprites/circulo.jpg")));
        btnElipse = new JButton("Elipse", new ImageIcon(getClass().getResource("../sprites/elipse.jpg")));
        btnRetangulo = new JButton("Retangulo", new ImageIcon(getClass().getResource("../sprites/retangulo.png")));
        btnPolilinha = new JButton("Polilinha", new ImageIcon(getClass().getResource("../sprites/linha.jpg")));
        btnSelecionar = new JButton("Selecionar", new ImageIcon(getClass().getResource("../sprites/selecionar.png")));
        btnDesselecionar = new JButton("Desselecionar", new ImageIcon(getClass().getResource("../sprites/desselecionar.png")));
        btnDeslocar = new JButton("Deslocar", new ImageIcon(getClass().getResource("../sprites/deslocar.png")));
        btnCores = new JButton("Cores", new ImageIcon(getClass().getResource("../sprites/cores.jpg")));
        btnApagar = new JButton("Apagar", new ImageIcon(getClass().getResource("../sprites/apagar.jpg")));
        btnLimpar = new JButton("Limpar", new ImageIcon(getClass().getResource("../sprites/sair.jpg")));

        pnlBotoes = new JPanel();

        pnlBotoes.setLayout(new FlowLayout());
        pnlBotoes.add(btnAbrir);
        pnlBotoes.add(btnSalvar);
        pnlBotoes.add(btnPonto);
        pnlBotoes.add(btnLinha);
        pnlBotoes.add(btnCirculo);
        pnlBotoes.add(btnElipse);
        pnlBotoes.add(btnRetangulo);
        pnlBotoes.add(btnPolilinha);
        pnlBotoes.add(btnSelecionar);
        pnlBotoes.add(btnDesselecionar);
        pnlBotoes.add(btnDeslocar);
        pnlBotoes.add(btnCores);
        pnlBotoes.add(btnApagar);
        pnlBotoes.add(btnLimpar);

        Container cont = getContentPane();
        cont.setLayout(new BorderLayout());
        cont.add(pnlBotoes, BorderLayout.NORTH);

        pane = new JDesktopPane();
        pane.setLayout(new BorderLayout());

        cont.add(pane, BorderLayout.CENTER);

        frame = new JInternalFrame("Nenhum Arquivo Aberto", true, false, false, false);

        pane.add(frame, BorderLayout.CENTER);

        frame.setVisible(true);
        frame.setOpaque(true);

        desenhoPnl = new MeuJPanel();

        frame.add(desenhoPnl);

        btnAbrir.addActionListener(new Opener());
        btnSalvar.addActionListener(new Saver());
        btnPonto.addActionListener(new desenhaPonto());
        btnLinha.addActionListener(new desenhaLinha());
        btnCirculo.addActionListener(new desenhaCirculo());
        btnElipse.addActionListener(new desenhaOval());
        btnRetangulo.addActionListener(new desenhaRetangulo());
        btnPolilinha.addActionListener(new desenhaPolilinha());
        btnSelecionar.addActionListener(new selecionaPoligonos());
        btnDesselecionar.addActionListener(new removerSelecao());
        btnDeslocar.addActionListener(new AcaoDeslocar());
        btnCores.addActionListener(new selecionaCor());
        btnPonto.addActionListener(new desenhaPonto());
        btnApagar.addActionListener(new limparSelecionados());
        btnLimpar.addActionListener(new limparTela());

        zeraEsperas();

        setVisible(true);
    }

    public static void main(String[] args) {
        Editor handle = new Editor();
        handle.addWindowListener(
                new WindowAdapter(){
                    public void windowClosing(WindowEvent e){
                        System.exit(0);
                    }
                }
        );
    }

    private void zeraEsperas() {
        pontoBuffer = new Ponto(0, 0, Color.BLACK);

        esperaPonto = false;
        esperaLinhaIni = false;
        esperaCirculoIni = false;
        esperaOvalIni = false;
        esperaRetanguloIni = false;
        esperaPolilinhaIni = false;

        esperaLinhaFim = false;
        esperaCirculoFim = false;
        esperaOvalFim = false;
        esperaRetanguloFim = false;
        esperaPolilinhaFim = false;
    }

    private void desenhar(){
        Graphics g = desenhoPnl.getGraphics();
        desenhoPnl.paintComponent(g);
    }

    private void limpar(){
        Graphics g = desenhoPnl.getGraphics();
        g.clearRect(0, 0, (int) frame.getSize().getWidth(), (int) frame.getSize().getHeight());
    }

    public void clearDesenhar(){
        statusBar1.setText("Mensagem: ");
        zeraEsperas();
    }

    public class Opener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            JFileChooser getfile = new JFileChooser();
            getfile.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int status = getfile.showOpenDialog(Editor.this);

            if(status == JFileChooser.APPROVE_OPTION){
                File arquivo = getfile.getSelectedFile();
                try{
                    qtosDados = 0;
                    figuras = new Ponto[tamanhoInicial];

                    BufferedReader handlerRead = new BufferedReader(new FileReader(arquivo));
                    String data = "";
                    while((data = handlerRead.readLine()) != null && qtosDados < tamanhoInicial){
                        String type = data.substring(0, 5).trim();
                        int posX = Integer.parseInt(data.substring(5, 10).trim());
                        int posY = Integer.parseInt(data.substring(10, 15).trim());
                        int corR = Integer.parseInt(data.substring(15, 20).trim());
                        int corG = Integer.parseInt(data.substring(20, 25).trim());
                        int corB = Integer.parseInt(data.substring(25, 30).trim());
                        Color cor = new Color(corR, corG, corB);

                        switch(type){
                            case "p" : figuras[qtosDados] = new Ponto(posX, posY, cor); break;

                            case "l" : int posXB = Integer.parseInt(data.substring(30, 35).trim());
                                       int posYB = Integer.parseInt(data.substring(35, 40).trim());
                                       figuras[qtosDados] = (Ponto) new Linha(posX, posY, posXB, posYB, cor); break;
                                       
                            case "c" : int raio = Integer.parseInt(data.substring(30, 35).trim());
                                       figuras[qtosDados] = (Ponto) new Circulo(posX, posY, raio, cor); break;

                            case "o" : int raioA = Integer.parseInt(data.substring(30, 35).trim());
                                       int raioB = Integer.parseInt(data.substring(35, 40).trim());
                                       figuras[qtosDados] = (Ponto) new Oval(posX, posY, raioA, raioB, cor); break;

                            case "r" : int pontoXB = Integer.parseInt(data.substring(30, 35).trim());
                                       int pontoYB = Integer.parseInt(data.substring(35, 40).trim());
                                       figuras[qtosDados] = (Ponto) new Retangulo(posX, posY, pontoXB, pontoYB, cor); break;

                            case "poli" : figuras[qtosDados] = (Ponto) new Polilinha(posX, posY, cor);
                                        boolean continuar = true;
                                        int i = 0;
                                        while(continuar){
                                            try{
                                                int d = (i * 10);
                                                int pontosXB = Integer.parseInt(data.substring(30 + d, 35 + d).trim());
                                                int pontosYB = Integer.parseInt(data.substring(35 + d, 40 + d).trim());

                                                ((Polilinha) figuras[qtosDados]).appendPonto(new Ponto(pontosXB, pontosYB, cor));
                                                i ++;
                                            }
                                            catch(Exception err){
                                                continuar = false;
                                            }
                                        }
                                        break;
                        }

                        qtosDados ++;
                    }
                    handlerRead.close();

                    frame.setTitle(arquivo.getName());
                    limpar();
                    desenhar();
                }
                catch(Exception err){
                    err.printStackTrace();
                }
            }
        }
    }

    public class Saver implements ActionListener{
        public void actionPerformed(ActionEvent e){
            JFileChooser getfile = new JFileChooser();
            getfile.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int status = getfile.showSaveDialog(Editor.this);

            if(status == JFileChooser.APPROVE_OPTION){
                File arquivo = getfile.getSelectedFile();
                try{
                    if(arquivo.createNewFile()){
                        arquivo.delete();
                        arquivo.createNewFile();
                    }

                    BufferedWriter w = new BufferedWriter(new FileWriter(arquivo));
                    for(int i = 0; i < qtosDados; i ++){
                        if(figuras[i] != null){
                            w.write(figuras[i].toString() + "\n");
                        }
                    }
                    w.close();
                }
                catch(Exception err){
                    err.printStackTrace();
                }
            }
        }
    }

    public class desenhaPonto implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent arg0) {
            statusBar1.setText("Mensagem: Clique onde quer criar o ponto!");
            zeraEsperas();
            esperaPonto = true;
        }
    }

    public class desenhaLinha implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent arg0) {
            statusBar1.setText("Mensagem: Clique onde quer criar o ponto inicial da reta!");
            zeraEsperas();
            esperaLinhaIni = true;
        }
    }

    public class desenhaCirculo implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent arg0) {
            statusBar1.setText("Mensagem: Clique onde quer criar o centro do circulo!");
            zeraEsperas();
            esperaCirculoIni = true;
        }
    }

    public class desenhaOval implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent arg0) {
            statusBar1.setText("Mensagem: Clique onde quer criar um dos focos da elipse!");
            zeraEsperas();
            esperaOvalIni = true;
        }
    }

    public class desenhaRetangulo implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent arg0) {
            statusBar1.setText("Mensagem: Clique onde quer criar um dos lados do retangulo!");
            zeraEsperas();
            esperaRetanguloIni = true;
        }
    }

    public class desenhaPolilinha implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent arg0) {
            if(!esperaPolilinhaFim){
                statusBar1.setText("Mensagem: Clique onde quer começar uma polilinha!");
                zeraEsperas();
                esperaPolilinhaIni = true;
            }
            else{
                zeraEsperas();

                desenhar();

                clearDesenhar();
            }
        }
    }

    public class selecionaPoligonos implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String dados = "Selecione poligonos!\n";
            for(int i = 0; i < qtosDados; i ++){
                if(figuras[i] != null)
                dados += (i + 1) + " - " + figuras[i].getClass().getSimpleName() + "\n";
            }
            dados += "Inserir um indice já selecionado desseleciona o poligono especifico!";

            String resultado = JOptionPane.showInputDialog(Editor.this, dados);
            if(resultado != null){
                try{
                    int index = Integer.parseInt(resultado) - 1;

                    int searchIndex = -1;
                    boolean achou = false;

                    for(int j = 0; j < qtosSelecionados; j ++){
                        if(figuras[index] != null && index == destaques[j]){
                            achou = true;
                            searchIndex = j;
                        }
                    }

                    if(!achou && figuras[index] != null){
                        destaques[qtosSelecionados] = index;
                        qtosSelecionados ++;
                    }
                    else if(achou && searchIndex >= 0){
                        destaques[searchIndex] = -1;
                    }
                    else{
                        JOptionPane.showMessageDialog(Editor.this, "Insira um valor exclusivamente numérico e positivo de acordo com os indices!", "Insira um valor válido!", JOptionPane.WARNING_MESSAGE);
                    }

                    limpar();
                    desenhar();
                }
                catch(Exception err){
                    JOptionPane.showMessageDialog(Editor.this, "Insira um valor exclusivamente numérico e positivo de acordo com os indices!", "Insira um valor válido!", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    public class selecionaCor implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(Editor.this, "Alterar a cor em uso também altera os poligonos selecionados!", "Aviso", JOptionPane.PLAIN_MESSAGE);
            Color backupColor = currentColor;
            currentColor = JColorChooser.showDialog(Editor.this, "Selecione uma cor para novos desenhos / desenhos selecionados!", currentColor);

            if(currentColor != null && backupColor != null){
                if(backupColor.getRGB() != currentColor.getRGB()){
                    for(int i = 0; i < qtosDados; i ++){
                        for(int j = 0; j < qtosSelecionados; j ++){
                            if(i == destaques[j] && figuras[i] != null){
                                figuras[i].setCor(currentColor);
                            }
                        }
                    }
                }
            }
            desenhar();
        }
    }

    public class removerSelecao implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent arg0) {
            destaques = new int[tamanhoInicial];
            qtosSelecionados = 0;

            limpar();
            desenhar();
        }
    }

    public class AcaoDeslocar implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent arg0) {
            try{
                String x = JOptionPane.showInputDialog(Editor.this, "Insira um valor para alterar a posição dos selecionados no eixo X!");
                int alteraX = Integer.parseInt(x);
                String y =JOptionPane.showInputDialog(Editor.this, "Insira um valor para alterar a posição dos selecionados no eixo Y!");
                int alteraY = Integer.parseInt(y);
                
                for(int i = 0; i < qtosDados; i ++){
                    for(int j = 0; j < qtosSelecionados; j ++){
                        if(figuras[i] != null && i == destaques[j]){
                            figuras[i].setX(figuras[i].getX() + alteraX);
                            figuras[i].setY(figuras[i].getY() + alteraY);

                            if(figuras[i].getClass().isInstance(new Linha(0, 0, 0, 0, Color.BLACK))){
                                ((Linha) figuras[i]).setXL(((Linha) figuras[i]).getXL() + alteraX);
                                ((Linha) figuras[i]).setYL(((Linha) figuras[i]).getYL() + alteraY);
                            }
                        }
                    }
                }

                limpar();
                desenhar();
            }
            catch(Exception err){
                JOptionPane.showMessageDialog(Editor.this, "Insira valores exclusivamente numéricos!");
            }
        }
    }

    public class limparSelecionados implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent arg0) {
            for(int i = 0; i < qtosDados; i ++){
                for(int j = 0; j < qtosSelecionados; j ++){
                    if(figuras[i] != null && i == destaques[j]){
                        figuras[i] = null;
                    }
                }
            }

            destaques = new int[tamanhoInicial];
            qtosSelecionados = 0;

            limpar();
            desenhar();
        }
    }

    public class limparTela implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent arg0) {
            statusBar1.setText("Mensagem: ");
            zeraEsperas();

            figuras = new Ponto[tamanhoInicial];
            qtosDados = 0;

            destaques = new int[tamanhoInicial];
            qtosSelecionados = 0;

            Graphics g = desenhoPnl.getGraphics();
            g.clearRect(0, 0, (int) frame.getSize().getWidth(), (int) frame.getSize().getHeight());

            frame.setTitle("Nenhum Arquivo Aberto");
        }
    }

    public class MeuJPanel extends JPanel implements MouseListener, MouseMotionListener{
        JPanel pnlStatus;

        public MeuJPanel(){
            super();

            pnlStatus = new JPanel();
            pnlStatus.setLayout(new GridLayout(1, 2));

            statusBar1 = new JLabel("Mensagem: ");
            statusBar2 = new JLabel("Coordenada: ");

            pnlStatus.add(statusBar1);
            pnlStatus.add(statusBar2);

            getContentPane().add(pnlStatus, BorderLayout.SOUTH);

            addMouseListener(this);
            addMouseMotionListener(this);
        }

        @Override
        public void paintComponent(Graphics g){
            for(int i = 0; i < qtosDados; i ++){
                Ponto pntAtual = (Ponto) figuras[i];
                if(pntAtual != null){
                    boolean achou = false;

                    for(int j = 0; j < qtosSelecionados; j ++){
                        if(figuras[i] != null && i == destaques[j]){
                            achou = true;
                        }
                    }

                    if(achou){
                        ((Graphics2D) g).setStroke(new BasicStroke(5));
                    }
                    else{
                        ((Graphics2D) g).setStroke(new BasicStroke(3));
                    }
                    pntAtual.draw(g);
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent arg0) {}

        @Override
        public void mouseClicked(MouseEvent arg0) {
            if(esperaPonto){
                if(qtosDados < tamanhoInicial){
                    Ponto novoPonto = new Ponto(arg0.getX(), arg0.getY(), currentColor);
                    figuras[qtosDados] = novoPonto;
                    qtosDados ++;

                    desenhar();

                    clearDesenhar();
                }
            }
            else if(esperaLinhaIni){
                if(qtosDados < tamanhoInicial){
                    statusBar1.setText("Mensagem: Clique onde quer criar o ponto final da reta!");

                    pontoBuffer = new Ponto(arg0.getX(), arg0.getY(), currentColor);
                    esperaLinhaIni = false;
                    esperaLinhaFim = true;
                }
            }
            else if(esperaLinhaFim){
                if(qtosDados < tamanhoInicial){
                    Linha novaLinha = new Linha(arg0.getX(), arg0.getY(), pontoBuffer.getX(), pontoBuffer.getY(), currentColor);
                    figuras[qtosDados] = novaLinha;
                    qtosDados ++;

                    desenhar();

                    clearDesenhar();
                }
            }
            else if(esperaCirculoIni){
                if(qtosDados < tamanhoInicial){
                    statusBar1.setText("Mensagem: Clique para formar o tamanho do raio!");

                    pontoBuffer = new Ponto(arg0.getX(), arg0.getY(), currentColor);
                    esperaCirculoIni = false;
                    esperaCirculoFim = true;
                }
            }
            else if(esperaCirculoFim){
                if(qtosDados < tamanhoInicial){
                    int x = pontoBuffer.getX() - arg0.getX();
                    int xPositivo = (x >= 0) ? x : -x;

                    int y = pontoBuffer.getY() - arg0.getY();
                    int yPositivo = (y >= 0) ? y : -y;

                    int raio = (int) Math.round(Math.sqrt(Math.pow(xPositivo, 2) + Math.pow(yPositivo, 2)));

                    Circulo novaLinha = new Circulo(pontoBuffer.getX(), pontoBuffer.getY(), raio, currentColor);
                    figuras[qtosDados] = novaLinha;
                    qtosDados ++;

                    desenhar();

                    clearDesenhar();
                }
            }
            else if(esperaOvalIni){
                if(qtosDados < tamanhoInicial){
                    statusBar1.setText("Mensagem: Clique para formar o tamanho do raio!");

                    pontoBuffer = new Ponto(arg0.getX(), arg0.getY(), currentColor);
                    esperaOvalIni = false;
                    esperaOvalFim = true;
                }
            }
            else if(esperaOvalFim){
                if(qtosDados < tamanhoInicial){
                    int x = arg0.getX() - pontoBuffer.getX();
                    int y = arg0.getY() - pontoBuffer.getY();

                    Oval novoOval = new Oval(pontoBuffer.getX(), pontoBuffer.getY(), (x >= 0) ? x : -x, (y >= 0) ? y : -y, currentColor);
                    figuras[qtosDados] = novoOval;
                    qtosDados ++;

                    desenhar();

                    clearDesenhar();
                }
            }
            else if(esperaRetanguloIni){
                if(qtosDados < tamanhoInicial){
                    statusBar1.setText("Mensagem: Clique para onde quer formar o retângulo!");

                    pontoBuffer = new Ponto(arg0.getX(), arg0.getY(), currentColor);
                    esperaRetanguloIni = false;
                    esperaRetanguloFim = true;
                }
            }
            else if(esperaRetanguloFim){
                if(qtosDados < tamanhoInicial){
                    int xA = arg0.getX();
                    int xB = pontoBuffer.getX();
                    int yA = arg0.getY();
                    int yB = pontoBuffer.getY();

                    Retangulo novoPonto = new Retangulo((xA >= xB) ? xA : xB, (yA >= yB) ? yA : yB, (xA >= xB) ? xB : xA, (yA >= yB) ? yB : yA, currentColor);
                    figuras[qtosDados] = novoPonto;
                    qtosDados ++;

                    desenhar();

                    clearDesenhar();
                }
            }
            else if(esperaPolilinhaIni){
                if(qtosDados < tamanhoInicial){
                    statusBar1.setText("Mensagem: Clique para onde quer formar a proxima linha! Clique no botão polilinha para parar!");

                    novaPolilinha = new Polilinha(arg0.getX(), arg0.getY(), currentColor);
                    figuras[qtosDados] = novaPolilinha;
                    qtosDados ++;

                    esperaPolilinhaIni = false;
                    esperaPolilinhaFim = true;
                }
            }
            else if(esperaPolilinhaFim){
                if(qtosDados < tamanhoInicial){
                    int xA = arg0.getX();
                    int yA = arg0.getY();

                    novaPolilinha.appendPonto(new Ponto(xA, yA, currentColor));

                    desenhar();
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent arg0) {}

        @Override
        public void mouseEntered(MouseEvent arg0) {}

        @Override
        public void mouseExited(MouseEvent arg0) {}

        @Override
        public void mouseMoved(MouseEvent arg0) {
            statusBar2.setText("Coordenada: " + arg0.getX() + ", " + arg0.getY());
        }

        @Override
        public void mouseReleased(MouseEvent arg0) {}
    }
}
