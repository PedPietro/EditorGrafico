import java.awt.*;

public class Retangulo extends Ponto
{
    private Ponto pontoFinal;

    public Retangulo()
    {
        super();
        pontoFinal = new Ponto();
        setCor(Color.black);
    }

    public Retangulo(int x1, int y1, int x2, int y2, Color novaCor)
    {
        super(x1,y1, novaCor);
        pontoFinal = new Ponto(x2,y2, novaCor);
    }

    public void setPontoFinal(Ponto novoPontoFinal) {
        pontoFinal = novoPontoFinal;
    }

    public Ponto getPontoFinal() {
        return pontoFinal;
    }

    public void desenhar(Color corDesenho, Graphics g)
    {
        g.setColor(corDesenho);
        g.drawRect(this.getX(), this.getY(), Math.abs(this.getX() - pontoFinal.getX()), Math.abs(this.getY() - pontoFinal.getY()));
    }

    public String toString()
    {
        return transformaString("r",5)+
                transformaString(super.getX(),5)+
                transformaString(super.getY(),5)+

                transformaString(getCor().getRed(),5)+
                transformaString(getCor().getGreen(),5)+
                transformaString(getCor().getBlue(),5)+

                transformaString(pontoFinal.getX(), 5)+
                transformaString(pontoFinal.getY(), 5);
    }
}
