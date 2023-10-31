import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import java.util.Locale;

/**
 *
 * @author Gabriel Bonifácio Huluany Gonzalez RA 125111372859
 */

public class Cena implements GLEventListener {
    GLU glu;
    GL2 gl;
    GLUT glut;

    float aspecto;
    float bolaX = 0;
    float bolaY = 1f;
    char direcaoX;
    char direcaoY = 'd';
    boolean pausado = false;
    int fase = 0;
    float velocidade = 0.02f;

    float bastaoX = 0;
    int pontuacao = 0;
    int vidas = 5;

    int iluminacao = GL2.GL_SMOOTH;
    boolean iluminado = true;

    @Override
    public void init(GLAutoDrawable drawable) {
        //Dados iniciais da cena
        glu = new GLU();
        gl = drawable.getGL().getGL2();

        //Habilita o buffer de profundidade
        gl.glEnable(GL2.GL_DEPTH_TEST);

        movimentarBolaAleatoriamente();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        //Obtém o contexto OpenGL
        gl = drawable.getGL().getGL2();
        //Objeto para desenho 3D
        glut = new GLUT();

        //Define a cor da janela (R, G, B, Alpha)
        gl.glClearColor(0, 0, 0, 1);
        //Limpa a janela com a cor especificada
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity(); //Lê a matriz identidade

        switch (fase) {
            case 0:
                iniciarMenu();
                break;
            case 1:
                iniciarFase1();
                break;
            case 2:
                iniciarFase2();
                break;
            case 3:
                finalizarJogo();
                break;
        }

        if (iluminado) {
            esquematizarIluminacao();
            ligarIluminacao();
        }
        if (!iluminado) {
            desligarIluminacao();
        }

        gl.glFlush();
    }
    
    public void esquematizarIluminacao() {
        float[] luzAmbiente = { 0.7f, 0.7f, 0.7f, 1f };
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, luzAmbiente, 0);

        float luzDifusa[] = {0.8f, 0.8f, 0.8f, 1.0f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, luzDifusa, 0);

        float posicaoLuz[] = {-50.0f, 0.0f, 100.0f, 1.0f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, posicaoLuz, 0);
    }
    
    public void ligarIluminacao() {
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glShadeModel(iluminacao);
    }
    
    public void desligarIluminacao() {
        gl.glDisable(GL2.GL_LIGHT0);
        gl.glDisable(GL2.GL_LIGHTING);
    }

    public void iniciarMenu() {
        String size = "big";
        float left = -0.650f;
        float begin = 0.975f;

        escrever(left, begin -= 0.1f, size, "Pong");
        escrever(left, begin -= 0.1f, size, "O objetivo do jogo é mover o bastão para rebater a bola o máximo de vezes possível.");
        escrever(left, begin -= 0.1f, size, "");
        escrever(left, begin -= 0.1f, size, "Regras");
        escrever(left, begin -= 0.1f, size, "- A bola surge aleatoriamente na esquerda ou na direita a partir do topo da tela.");
        escrever(left, begin -= 0.1f, size, "- A trajetória da bola muda ao encostar no bastão, obstáculos e cantos da tela.");
        escrever(left, begin -= 0.1f, size, "- A velocidade da bola aumenta de acordo com o tempo, pontuação e fase.");
        escrever(left, begin -= 0.1f, size, "- O jogo possui 2 fases. Ao atingir 200 pontos a 2ª fase é iniciada.");
        escrever(left, begin -= 0.1f, size, "- Você ganha 10 pontos a cada rebatida na bola com o bastão.");
        escrever(left, begin -= 0.1f, size, "- Você possui 5 vidas e perde 1 vida quando deixa a bola cair.");
        escrever(left, begin -= 0.1f, size, "- O jogo é finalizado quando você não possui mais vidas ou quando pressiona BACKSPACE.");
        escrever(left, begin -= 0.1f, size, "");
        escrever(left, begin -= 0.1f, size, "Comandos");
        escrever(left, begin -= 0.1f, size, "- Mover o bastão < > ou Mouse");
        escrever(left, begin -= 0.1f, size, "- Pausar e despausar o jogo P");
        escrever(left, begin -= 0.1f, size, "- Finalizar o jogo BACKSPACE");
        escrever(left, begin -= 0.1f, size, "- Fechar o programa ESC");
        escrever(left, begin -= 0.1f, size, "");
        escrever(left, begin -= 0.1f, size, "Pressione ENTER para jogar");
    }

    public void desenharVidas(float posicao, boolean preenchida) {
        desligarIluminacao();
        gl.glPushMatrix();
        if (preenchida)
            gl.glColor3f(1, 0, 0);
        else
            gl.glColor3f(1, 1, 1);

        gl.glTranslatef(0.4f + posicao, -0.945f, 0);

        double limite = Math.PI;
        double i, cX, cY, rX, rY;
        cX = 0;
        cY = 0;
        rX = 0.006f;
        rY = 0.009f;

        gl.glPushMatrix();
        gl.glTranslatef(-0.006f, 0, 0);
        gl.glBegin(GL2.GL_POLYGON);
        for(i=0 ; i < limite; i+= 0.01) {
            gl.glVertex2d(cX + rX * Math.cos(i),
                    cY + rY * Math.sin(i) );
        }
        gl.glEnd();
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.006f, 0, 0);
        gl.glBegin(GL2.GL_POLYGON);
        for(i=0 ; i < limite; i+= 0.01) {
            gl.glVertex2d(cX + rX * Math.cos(i),
                    cY + rY * Math.sin(i) );
        }
        gl.glEnd();
        gl.glPopMatrix();

        //gl.glScalef(0.015f, 0.03f, 0.015f);

        gl.glBegin(GL2.GL_TRIANGLES);
        gl.glVertex2f(0, -0.03f);
        gl.glVertex2f(0.015f, 0);
        gl.glVertex2f(-0.015f, 0);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glColor3f(1, 1, 1);
    }

    public void desenharBastao() {
        gl.glPushMatrix();
        gl.glTranslatef(bastaoX, 0, 0);
        gl.glBegin(GL2.GL_QUADS);
        //Black
        gl.glColor3f(0.0f, 0.0f, 0.0f);
        gl.glVertex2f(-0.2f, -0.8f);
        //Red
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glVertex2f(0.2f, -0.8f);
        //Green
        gl.glColor3f(0.0f, 1.0f, 0.0f);
        gl.glVertex2f(0.2f, -0.9f);
        //Blue
        gl.glColor3f(0.0f, 0.0f, 1.0f);
        gl.glVertex2f(-0.2f, -0.9f);
        gl.glEnd();
        gl.glPopMatrix();
    }

    public void desenharBola() {
        gl.glPushMatrix();
        gl.glTranslatef(bolaX, bolaY, 0);
        gl.glColor3f(1, 1, 1);

        double limit = 2 * Math.PI;
        double i;
        double cX = 0;
        double cY = 0;
        double rX = 0.1f / aspecto;
        double rY = 0.1f;

        gl.glBegin(GL2.GL_POLYGON);
        for (i = 0; i < limit; i += 0.01) {
            gl.glVertex2d(cX + rX * Math.cos(i), cY + rY * Math.sin(i));
        }
        gl.glEnd();

        gl.glPopMatrix();
    }

    public void movimentarBolaAleatoriamente() {
        double aleatorio = -0.8f + Math.random() * 1.6f;
        if (aleatorio > 0) {
            direcaoX = 'r';
        } else {
            direcaoX = 'l';
        }
        bolaX = Float.valueOf(String.format(Locale.US, "%.2f", aleatorio));
    }

    public void movimentarBola() {
        float xTransBallFixed = Float.valueOf(String.format(Locale.US, "%.1f", bolaX));
        float yTransBallFixed = Float.valueOf(String.format(Locale.US, "%.1f", bolaY));

        if (fase == 2 && direcaoX == 'l'
                && isObjectInYRange(xTransBallFixed, yTransBallFixed, -0.1f, 0.5f, 0.2f)) {
            direcaoX = 'r';
        }
        if (fase == 2 && direcaoX == 'r'
                && isObjectInYRange(xTransBallFixed, yTransBallFixed, -0.1f, 0.5f, -0.2f)) {
            direcaoX = 'l';
        } else if (xTransBallFixed > -1f && direcaoX == 'l') {
            bolaX -= velocidade/2;
        } else if (xTransBallFixed == -1f && direcaoX == 'l') {
            direcaoX = 'r';
        } else if (xTransBallFixed < 1f && direcaoX == 'r') {
            bolaX += velocidade/2;
        } else if (xTransBallFixed == 1f && direcaoX == 'r') {
            direcaoX = 'l';
        }

        if (fase == 2 && direcaoY == 'u'
                && isObjectInXRange(xTransBallFixed, yTransBallFixed, -0.2f, 0.2f, -0.2f)) {
            direcaoY = 'd';
        } else if (fase == 2 && direcaoY == 'd'
                && isObjectInXRange(xTransBallFixed, yTransBallFixed, -0.2f, 0.2f, 0.6f)) {
            direcaoY = 'u';
        } else if (yTransBallFixed == -0.7f && direcaoY == 'd'
                && isBallInRangeOfBar(xTransBallFixed)) {
            direcaoY = 'u';
            iluminado = false;
            iluminacao = iluminacao == GL2.GL_SMOOTH ? GL2.GL_FLAT : GL2.GL_SMOOTH;
            pontuacao += 10;
        } else if (yTransBallFixed < 0.9f && direcaoY == 'u') {
            bolaY += velocidade;
        } else if (yTransBallFixed == 0.9f && direcaoY == 'u') {
            direcaoY = 'd';
        } else if (yTransBallFixed < -1f) {
            bolaY = 1f;
            bolaX = 0;
            vidas--;
            movimentarBolaAleatoriamente();
        } else {
            bolaY -= velocidade;
            iluminado = true;
            iluminacao = iluminacao == GL2.GL_SMOOTH ? GL2.GL_FLAT : GL2.GL_SMOOTH;
        }
    }

    public void iniciarFase1() {
        desenharBastao();
        desenharBola();

        if (!pausado) {
            movimentarBola();
        } else {
            //gl.glColor3f(1, 1, 0);
            escrever(-0.09f, 0.1f, "big", "Jogo pausado");
            escrever(-0.2f, -0.1f, "big", "Pressione P para despausar");
            //gl.glColor3f(1, 1, 1);
        }

        if (pontuacao == 200) {
            fase = 2;
        }

        if (vidas == 0) {
            fase = 3;
        }

        escrever(0.725f, -0.975f, "big", "Pontuação: " + pontuacao);

        for (int i = 1; i <= 5; i++) {
            if (vidas >= i) {
                desenharVidas(0.05f * i, true);
            }
            else {
                desenharVidas(0.05f * i, false);
            }
        }
    }

    public void desenharArteFase2() {
        escrever(-0.04f, 0.9f, "big", "2ª fase");
    }

    public void desenharObstaculoFase2() {
        gl.glPushMatrix();
        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(1, 0, 1);
        gl.glVertex2f(-0.2f, 0.5f);
        gl.glVertex2f(0.2f, 0.5f);
        gl.glVertex2f(0.2f, -0.1f);
        gl.glVertex2f(-0.2f, -0.1f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glColor3f(1, 1, 1);
    }

    public void iniciarFase2() {
        desenharBastao();
        desenharBola();
        desenharArteFase2();
        desenharObstaculoFase2();

        velocidade = 0.03f;

        if (!pausado) {
            movimentarBola();
        } else {
            //gl.glColor3f(1, 1, 0);
            escrever(-0.09f, 0.1f, "big", "Jogo pausado");
            escrever(-0.2f, -0.1f, "big", "Pressione P para despausar");
            //gl.glColor3f(1, 1, 1);
        }

        if (vidas == 0) {
            fase = 3;
        }

        escrever(0.725f, -0.975f, "big", "Pontuação: " + pontuacao);

        for (int i = 1; i <= 5; i++) {
            if (vidas >= i) {
                desenharVidas(0.05f * i, true);
            }
            else {
                desenharVidas(0.05f * i, false);
            }
        }
    }

    public void finalizarJogo() {
        escrever(-0.075f, 0.3f, "big", "Fim de jogo");
        escrever(-0.25f, 0.2f, "big", "");
        escrever(-0.125f, 0.1f, "big", "Pontuação final: " + pontuacao);
        escrever(-0.25f, 0, "big", "");
        escrever(-0.25f, -0.1f, "big", "- Jogar novamente ENTER");
        escrever(-0.25f, -0.2f, "big", "- Voltar para o menu BACKSPACE");
        escrever(-0.25f, -0.3f, "big", "- Fechar o programa ESC");
    }

    public void resetar() {
        bolaX = 0;
        bolaY = 1f;

        //direcaoX;
        movimentarBolaAleatoriamente();
        direcaoY = 'd';

        pausado = false;
        fase = 0;
        velocidade = 0.02f;

        bastaoX = 0;
        pontuacao = 0;
        vidas = 5;
    }

    public void escrever(float x, float y, String size, String phrase) {
        gl.glRasterPos2f(x, y);
        switch (size) {
            case "small":
                glut.glutBitmapString(GLUT.BITMAP_8_BY_13, phrase);
                break;
            case "big":
                glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_24, phrase);
        }
    }

    public boolean isBallInRangeOfBar(float xTranslatedBallFixed) {
        float leftBarLimit = Float.valueOf(String.format(Locale.US, "%.1f", bastaoX - 0.2f));
        float rightBarLimit = Float.valueOf(String.format(Locale.US, "%.1f", bastaoX + 0.2f));

        if (leftBarLimit <= xTranslatedBallFixed && rightBarLimit >= xTranslatedBallFixed) {
            return true;
        }

        return false;
    }

    public boolean isObjectInYRange(float xObj, float yObj, float bLimit, float tLimit, float xPoint) {
        if (tLimit >= yObj && bLimit <= yObj && xObj == xPoint) {
            return true;
        }

        return false;
    }

    public boolean isObjectInXRange(float xObj, float heightObj, float lLimit, float rLimit, float tLimit) {
        if (lLimit <= xObj && rLimit >= xObj && heightObj == tLimit) {
            return true;
        }

        return false;
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        //Obtém o contexto gráfico OpenGL
        GL2 gl = drawable.getGL().getGL2();

        //Ativa a matriz de projeção
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity(); //Lê a matriz identidade

        //Evita a divisão por zero
        if(height == 0) {
            height = 1;
        }

        //Projeção ortogonal com a correção do aspecto
        aspecto = (float) width / height;
        gl.glOrtho(-1, 1, -1, 1, -1, 1);

        //Ativa a matriz de modelagem
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity(); //Lê a matriz identidade
        //System.out.println("Reshape: " + width + ", " + height);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

}
