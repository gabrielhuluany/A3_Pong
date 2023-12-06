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
    private GLU glu;
    private GL2 gl;
    private GLUT glut;

    private float aspecto;
    private float bolaX = 0;
    private float bolaY = 1f;
    private char direcaoX;
    private char direcaoY = 'd';
    public boolean pausado = false;
    public int fase = 0;
    private float velocidade = 0.02f;

    public float bastaoX = 0;
    private int pontuacao = 0;
    private int vidas = 5;

    private int iluminacao = GL2.GL_SMOOTH;
    private boolean iluminado = true;

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

        switch(fase) {
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

        if(iluminado) {
            esquematizarIluminacao();
            ligarIluminacao();
        }
        if(!iluminado) {
            desligarIluminacao();
        }

        gl.glFlush();
    }
    
    private void esquematizarIluminacao() {
        float[] luzAmbiente = { 0.7f, 0.7f, 0.7f, 1f };
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, luzAmbiente, 0);

        float luzDifusa[] = {0.8f, 0.8f, 0.8f, 1.0f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, luzDifusa, 0);

        float posicaoLuz[] = {-50.0f, 0.0f, 100.0f, 1.0f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, posicaoLuz, 0);
    }
    
    private void ligarIluminacao() {
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glShadeModel(iluminacao);
    }
    
    private void desligarIluminacao() {
        gl.glDisable(GL2.GL_LIGHT0);
        gl.glDisable(GL2.GL_LIGHTING);
    }

    private void iniciarMenu() {
        String tamanho = "grande";
        float esquerda = -0.650f;
        float inicio = 0.975f;

        escrever(esquerda, inicio -= 0.1f, tamanho, "Pong");
        escrever(esquerda, inicio -= 0.1f, tamanho, "O objetivo do jogo é mover o bastão para rebater a bola o máximo de vezes possível.");
        escrever(esquerda, inicio -= 0.1f, tamanho, "");
        escrever(esquerda, inicio -= 0.1f, tamanho, "Regras");
        escrever(esquerda, inicio -= 0.1f, tamanho, "- A bola surge aleatoriamente na esquerda ou na direita a partir do topo da tela.");
        escrever(esquerda, inicio -= 0.1f, tamanho, "- A trajetória da bola muda ao encostar no bastão, obstáculos e cantos da tela.");
        escrever(esquerda, inicio -= 0.1f, tamanho, "- A velocidade da bola aumenta de acordo com o tempo, pontuação e fase.");
        escrever(esquerda, inicio -= 0.1f, tamanho, "- O jogo possui 2 fases. Ao atingir 200 pontos a 2ª fase é iniciada.");
        escrever(esquerda, inicio -= 0.1f, tamanho, "- Você ganha 10 pontos a cada rebatida na bola com o bastão.");
        escrever(esquerda, inicio -= 0.1f, tamanho, "- Você possui 5 vidas e perde 1 vida quando deixa a bola cair.");
        escrever(esquerda, inicio -= 0.1f, tamanho, "- O jogo é finalizado quando você não possui mais vidas ou quando pressiona BACKSPACE.");
        escrever(esquerda, inicio -= 0.1f, tamanho, "");
        escrever(esquerda, inicio -= 0.1f, tamanho, "Comandos");
        escrever(esquerda, inicio -= 0.1f, tamanho, "- Mover o bastão < > ou Mouse");
        escrever(esquerda, inicio -= 0.1f, tamanho, "- Pausar e despausar o jogo P");
        escrever(esquerda, inicio -= 0.1f, tamanho, "- Finalizar o jogo BACKSPACE");
        escrever(esquerda, inicio -= 0.1f, tamanho, "- Fechar o programa ESC");
        escrever(esquerda, inicio -= 0.1f, tamanho, "");
        escrever(esquerda, inicio -= 0.1f, tamanho, "Pressione ENTER para jogar");
    }

    private void desenharVidas(float posicao, boolean preenchida) {
        desligarIluminacao();
        gl.glPushMatrix();
        
        if(preenchida) {
            gl.glColor3f(1, 0, 0);
        }
        else {
            gl.glColor3f(1, 1, 1);
        }

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
        
        for(i = 0 ; i < limite; i+= 0.01) {
            gl.glVertex2d(cX + rX * Math.cos(i),
                    cY + rY * Math.sin(i));
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

    private void desenharBastao() {
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

    private void desenharBola() {
        gl.glPushMatrix();
        gl.glTranslatef(bolaX, bolaY, 0);
        gl.glColor3f(1, 1, 1);

        double limite = 2 * Math.PI;
        double i;
        double cX = 0;
        double cY = 0;
        double rX = 0.1f / aspecto;
        double rY = 0.1f;

        gl.glBegin(GL2.GL_POLYGON);
        for(i = 0; i < limite; i += 0.01) {
            gl.glVertex2d(cX + rX * Math.cos(i), cY + rY * Math.sin(i));
        }
        gl.glEnd();

        gl.glPopMatrix();
    }

    private void movimentarBolaAleatoriamente() {
        double aleatorio = -0.8f + Math.random() * 1.6f;
        if(aleatorio > 0) {
            direcaoX = 'r';
        } else {
            direcaoX = 'l';
        }
        bolaX = Float.valueOf(String.format(Locale.US, "%.2f", aleatorio));
    }

    private void movimentarBola() {
        float xTransBallFixed = Float.valueOf(String.format(Locale.US, "%.1f", bolaX));
        float yTransBallFixed = Float.valueOf(String.format(Locale.US, "%.1f", bolaY));

        if(fase == 2 && direcaoX == 'l' && isObjectInYRange(xTransBallFixed, yTransBallFixed, -0.1f, 0.5f, 0.2f)) {
            direcaoX = 'r';
        }
        if(fase == 2 && direcaoX == 'r' && isObjectInYRange(xTransBallFixed, yTransBallFixed, -0.1f, 0.5f, -0.2f)) {
            direcaoX = 'l';
        } else if(xTransBallFixed > -1f && direcaoX == 'l') {
            bolaX -= velocidade/2;
        } else if(xTransBallFixed == -1f && direcaoX == 'l') {
            direcaoX = 'r';
        } else if(xTransBallFixed < 1f && direcaoX == 'r') {
            bolaX += velocidade/2;
        } else if(xTransBallFixed == 1f && direcaoX == 'r') {
            direcaoX = 'l';
        }

        if(fase == 2 && direcaoY == 'u' && isObjectInXRange(xTransBallFixed, yTransBallFixed, -0.2f, 0.2f, -0.2f)) {
            direcaoY = 'd';
        } else if(fase == 2 && direcaoY == 'd' && isObjectInXRange(xTransBallFixed, yTransBallFixed, -0.2f, 0.2f, 0.6f)) {
            direcaoY = 'u';
        } else if(yTransBallFixed == -0.7f && direcaoY == 'd' && isBallInRangeOfBar(xTransBallFixed)) {
            direcaoY = 'u';
            iluminado = false;
            iluminacao = iluminacao == GL2.GL_SMOOTH ? GL2.GL_FLAT : GL2.GL_SMOOTH;
            pontuacao += 20;
        } else if(yTransBallFixed < 0.9f && direcaoY == 'u') {
            bolaY += velocidade;
        } else if(yTransBallFixed == 0.9f && direcaoY == 'u') {
            direcaoY = 'd';
        } else if(yTransBallFixed < -1f) {
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

    private void iniciarFase1() {
        desenharBastao();
        desenharBola();

        if(!pausado) {
            movimentarBola();
        } else {
            //gl.glColor3f(1, 1, 0);
            escrever(-0.09f, 0.1f, "grande", "Jogo pausado");
            escrever(-0.2f, -0.1f, "grande", "Pressione P para despausar");
            //gl.glColor3f(1, 1, 1);
        }

        if(pontuacao == 200) {
            fase = 2;
        }

        if(vidas == 0) {
            fase = 3;
        }

        escrever(0.725f, -0.975f, "grande", "Pontuação: " + pontuacao);

        for(int i = 1; i <= 5; i++) {
            if(vidas >= i) {
                desenharVidas(0.05f * i, true);
            }
            else {
                desenharVidas(0.05f * i, false);
            }
        }
    }

    private void desenharObstaculoFase2() {
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

    private void iniciarFase2() {
        desenharBastao();
        desenharBola();
        desenharObstaculoFase2();
        escrever(-0.04f, 0.9f, "grande", "2ª fase");

        velocidade = 0.03f;

        if(!pausado) {
            movimentarBola();
        } else {
            //gl.glColor3f(1, 1, 0);
            escrever(-0.09f, 0.1f, "grande", "Jogo pausado");
            escrever(-0.2f, -0.1f, "grande", "Pressione P para despausar");
            //gl.glColor3f(1, 1, 1);
        }

        if(vidas == 0) {
            fase = 3;
        }

        escrever(0.725f, -0.975f, "grande", "Pontuação: " + pontuacao);

        for(int i = 1; i <= 5; i++) {
            if(vidas >= i) {
                desenharVidas(0.05f * i, true);
            }
            else {
                desenharVidas(0.05f * i, false);
            }
        }
    }

    private void finalizarJogo() {
        escrever(-0.075f, 0.3f, "grande", "Fim de jogo");
        escrever(-0.25f, 0.2f, "grande", "");
        escrever(-0.125f, 0.1f, "grande", "Pontuação final: " + pontuacao);
        escrever(-0.25f, 0, "grande", "");
        escrever(-0.25f, -0.1f, "grande", "- Jogar novamente ENTER");
        escrever(-0.25f, -0.2f, "grande", "- Voltar para o menu BACKSPACE");
        escrever(-0.25f, -0.3f, "grande", "- Fechar o programa ESC");
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

    public void escrever(float x, float y, String tamanho, String texto) {
        gl.glRasterPos2f(x, y);
        switch(tamanho) {
            case "pequena":
                glut.glutBitmapString(GLUT.BITMAP_8_BY_13, texto);
                break;
            case "grande":
                glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_24, texto);
                break;
        }
    }

    private boolean isBallInRangeOfBar(float xTranslatedBallFixed) {
        float leftBarLimit = Float.valueOf(String.format(Locale.US, "%.1f", bastaoX - 0.2f));
        float rightBarLimit = Float.valueOf(String.format(Locale.US, "%.1f", bastaoX + 0.2f));

        if(leftBarLimit <= xTranslatedBallFixed && rightBarLimit >= xTranslatedBallFixed) {
            return true;
        }

        return false;
    }

    private boolean isObjectInYRange(float xObj, float yObj, float bLimit, float tLimit, float xPoint) {
        if(tLimit >= yObj && bLimit <= yObj && xObj == xPoint) {
            return true;
        }

        return false;
    }

    private boolean isObjectInXRange(float xObj, float heightObj, float lLimit, float rLimit, float tLimit) {
        if(lLimit <= xObj && rLimit >= xObj && heightObj == tLimit) {
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
