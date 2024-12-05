import java.awt.*;
public class Ponto
{
    private int x, y;
    private Color cor;

    public Ponto() {
        x = 0;
        y = 0;
        cor = Color.black;
    }

    public Ponto(int cX, int cY, Color qualCor)
    {
        x = cX;
        y = cY;
        cor = qualCor;
    }

    public void setX(int novoX) {
        x = novoX;
    }

    public void setY(int novoY) {
        y = novoY;
    }

    public void setCor(Color novaCor) {
        cor = novaCor;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getCor() {
        return cor;
    }

    public void desenhar(Color cor, Graphics g) {
        g.setColor(cor);
        g.drawLine(getX(),getY(),getX(),getY());
    }

    public String transformaString(int valor, int quantasPosicoes) {
        String cadeia = new String(valor+"");
        while (cadeia.length() < quantasPosicoes)
            cadeia = "0"+cadeia;
        return cadeia.substring(0,quantasPosicoes);
    }

    public String transformaString(String valor, int quantasPosicoes)
    {
        String cadeia = new String(valor+"");
        while (cadeia.length() < quantasPosicoes)
            cadeia = cadeia+" ";
        return cadeia.substring(0,quantasPosicoes);
    }

    public String toString()
    {
        return transformaString("p",5)+
                transformaString(getX(),5)+
                transformaString(getY(),5)+

                transformaString(getCor().getRed(),5)+
                transformaString(getCor().getGreen(),5)+
                transformaString(getCor().getBlue(),5);
    }
}
