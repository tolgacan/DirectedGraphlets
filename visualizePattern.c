#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#include <time.h>

#include <math.h>

#include <OpenGL/gl.h>
#include <GLUT/glut.h>

char pattern[25];
char s[5][200] = {"interacts-with","in-complex-with","catalysis-precedes","controls-expression-of","change-state-of"};
char ss[8][100] ={"interacts","complex","catalysis","expression","state"};

void init(void)
{
	glClearColor(1.0,1.0,1.0,1.0);
	glMatrixMode (GL_PROJECTION);
	gluOrtho2D(0.0,800.0,0.0,600.0);
}

void renderText(int x, int y, char *text)
{
	char *p;
	glRasterPos2f(x,y);
	for (p = text; *p; p++)
		glutBitmapCharacter(GLUT_BITMAP_HELVETICA_18,*p);
}

void drawCircle(int cx, int cy, float r, float g, float b,char *label)
{
    float radius = 100;
    float step;
    float x,y;
    float angle = 0;
    int i;
    
    glColor3f(r,g,b);
    step = 1.0;
    angle = 270-step/2;
    glBegin(GL_POLYGON);
    for (i=0;i<360;i++)
    {
        x = cx+cos(angle*M_PI/180)*radius;
        y = cy+sin(angle*M_PI/180)*radius;
        glVertex2f(x,y);
        angle+=step;
    }
    glEnd();
    glColor3f(0,0,0);
    renderText(cx-10,cy,label);

}
void drawLine(char *p,int x1, int y1,int x2, int y2, int cc)
{
    int i;
    float r[5] = {0.2,1.0,0.9,0.2,0.9};
    float g[5] = {0.2,1.0,0.2,0.9,0.5};
    float b[5] = {0.9,0.2,0.3,0.3,0.9};
    
    for (i=0;i<5;i++)
    {
        if (p[i]=='1')
        {
            glColor3f(r[i],g[i],b[i]);
            glBegin(GL_QUADS);
            glVertex2f(x1,y1+50-i*23);
            glVertex2f(x1,y1+42-i*23);
            glVertex2f(x2,y1+42-i*23);
            glVertex2f(x2,y1+50-i*23);
            glEnd();
            if (i>1)
            {
                glBegin(GL_TRIANGLES);
                glVertex2f(x2,y1+54-i*23);
                glVertex2f(x2,y1+38-i*23);
                glVertex2f(x2+20,y1+46-i*23);
                glEnd();
            }
            glColor3f( 0.0f, 0.0f, 0.0f );
            if (cc==1)
            renderText((x1+x2)/2-100,y1+54-i*23,s[i]);
            else if (cc==0)
            renderText((x1+x2)/2-10,y1+80-i*23,ss[i]);
            else
            renderText((x1+x2)/2+30,y1+10-i*23,ss[i]);

       }
    }
    for (i=5;i<8;i++)
    {
        if (p[i]=='1')
        {
                glColor3f(r[i-3],g[i-3],b[i-3]);
                glBegin(GL_QUADS);
                glVertex2f(x1,y1+50-(i-3)*23);
                glVertex2f(x1,y1+42-(i-3)*23);
                glVertex2f(x2,y1+42-(i-3)*23);
                glVertex2f(x2,y1+50-(i-3)*23);
                glEnd();
            
                glBegin(GL_TRIANGLES);
                glVertex2f(x1,y1+38-(i-3)*23);
                glVertex2f(x1,y1+54-(i-3)*23);
                glVertex2f(x1-20,y1+46-(i-3)*23);
                glEnd();
            glColor3f( 0.0f, 0.0f, 0.0f );
            if (cc==1)
            renderText((x1+x2)/2-100,y1+54-(i-3)*23,s[i-3]);
            else if (cc==0)
            renderText((x1+x2)/2-10,y1+80-(i-3)*23,ss[i-3]);
            else
            renderText((x1+x2)/2+30,y1+10-(i-3)*23,ss[i-3]);

        }
    }
}
void display(void)
{

    char s[200];
    glClear(GL_COLOR_BUFFER_BIT);

    if (strlen(pattern)==8)
    {
        drawCircle(150,500,1.0,0.0,0.0,"A");
        drawCircle(650,500,0.0,1.0,0.0,"B");
        drawLine(pattern,250,500,550,500,1);
    }
    else
    {
        drawCircle(150,500,1.0,0.0,0.0,"A");
        drawCircle(650,500,0.0,1.0,0.0,"B");
        drawLine(pattern,250,500,550,500,1);
        drawCircle(400,100,0.0,0.0,1.0,"C");
        glPushMatrix();
        glTranslatef(496,-4,0);
        glTranslatef(150,500,0);
        glScalef(0.9,0.9,0.9);
        glRotatef(240,0,0,1);
        glTranslatef(-150,-500,0);
        drawLine(pattern+8,250,500,550,500,2);
        glPopMatrix();
        glPushMatrix();
        glTranslatef(230,-400,0);
        glTranslatef(150,500,0);
        glScalef(0.9,0.9,0.9);
        glRotatef(120,0,0,1);
        glTranslatef(-150,-500,0);
        drawLine(pattern+16,250,500,550,500,0);
        glPopMatrix();

    }

    glColor3f(0,0,0);
    sprintf(s,"Pattern: %s",pattern);
    renderText(10,10,s);

	glutSwapBuffers();

}

/*-------------------
Keyboard and Mouse
-------------------*/


void processNormalKeys(unsigned char key, int x, int y) {
	
	if (key == 27) {
		exit(0);
	}
	glutPostRedisplay();
}

int validPattern(char *p)
{
    int i,k;
    k = strlen(p);
    if (k!=8 && k!=24)
        return 0;
    for (i=0;i<k;i++)
    {
        if (p[i]!='0' && p[i]!='1')
            return 0;
    }
    return 1;
}

int main(int argc, char** argv)
{
    if (argc!=2)
    {
        printf("Usage: visualizePattern <pattern>\n");
        return 1;
    }
    
    sscanf(argv[1],"%s",pattern);
    
    if (!validPattern(pattern))
    {
        printf("Not a valid pattern. Only 2 node and 3 node edge patterns are valid.\n");
        return 1;
    }
    
    printf("The pattern is: %s\n",pattern);
	glutInit(&argc, argv);

	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB);
	glutInitWindowPosition(0,0);
	glutInitWindowSize(800,600);
	glutCreateWindow("Pattern Visualizer");

	init();

	glutKeyboardFunc(processNormalKeys);
	glutDisplayFunc(display);
	glutMainLoop();
	return 0;
}
