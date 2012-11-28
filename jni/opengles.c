#include <jni.h>
#include <android/log.h>

#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <unistd.h>
#include "info_chenliang_tetris3d_OpenglRenderer.h"

#define  LOG_TAG    "opengles"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

void printGLString(const char *name, GLenum s) {
    const char *v = (const char *) glGetString(s);
    LOGI("GL %s = %s\n", name, v);
}

JNIEXPORT void JNICALL Java_info_chenliang_tetris3d_OpenglRenderer_jniSurfaceCreated
  (JNIEnv* env, jobject object)
{
    printGLString("Version", GL_VERSION);
    printGLString("Vendor", GL_VENDOR);
    printGLString("Renderer", GL_RENDERER);
    printGLString("Extensions", GL_EXTENSIONS);

    const GLchar* vertexShaderCode = "uniform mat4 finalMatrix;\n"
    								 "attribute vec4 vertexPosition;\n"
			  	  	  	  	  	  	 "attribute vec4 vertexColor;\n"
			  	  	  	  	  	  	 "varying vec4 interpolatedColor;\n"
			  	  	  	  	  	  	 "void main()\n"
			  	  	  	  	  	  	 "{\n"
			    					 	 "gl_Position = finalMatrix * vertexPosition;\n"
    									 "interpolatedColor = vertexColor;\n"
			  	  	  	  	  	  	 "}\n";

    const GLchar** vertexShaderSource = (const GLchar**)malloc(sizeof(const GLchar*));
    *vertexShaderSource = vertexShaderCode;

    GLuint vertexShader = glCreateShader(GL_VERTEX_SHADER);
    int length[1]= {strlen(vertexShaderCode)};

    glShaderSource(vertexShader, 1, vertexShaderSource, NULL);
    glCompileShader(vertexShader);
    int status[4];
    glGetShaderiv(vertexShader, GL_COMPILE_STATUS, status);
    LOGE("vertex shader compile status %d", status[0]);

    const GLchar* pixelShaderCode = "precision mediump float;\n"
    								"varying vec4 interpolatedColor;\n"
    								"void main()\n"
    			  	  	  	  	  	"{\n"
    									"gl_FragColor = interpolatedColor;\n"
    			  	  	  	  	  	"}\n";
    const GLchar** pixelShaderSource = (const GLchar**)malloc(sizeof(const GLchar*));
    *pixelShaderSource = pixelShaderCode;

    GLuint pixelShader = glCreateShader(GL_FRAGMENT_SHADER);
    length[0]= strlen(pixelShaderCode);

    glShaderSource(pixelShader, 1, pixelShaderSource, NULL);
    glCompileShader(pixelShader);

    glGetShaderiv(pixelShader, GL_COMPILE_STATUS, status);
    LOGE("pixel shader compile status %d", status[0]);

    int program = glCreateProgram();
    glAttachShader(program, vertexShader);
    glAttachShader(program, pixelShader);

    glBindAttribLocation(program, 0, "vertexPosition");
    glBindAttribLocation(program, 1, "vertexColor");

    glLinkProgram(program);
    int size = 4096;
    GLchar info[size];
    GLsizei logSize = 0;
    glGetProgramInfoLog(program, size, &logSize, info);

    char log[logSize];
    strncpy(log, info, logSize);

    LOGE("program status\n %s\n", log);

}

/*
 * Class:     info_chenliang_tetris3d_OpenglRenderer
 * Method:    jniSurfaceChanged
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_info_chenliang_tetris3d_OpenglRenderer_jniSurfaceChanged
  (JNIEnv* env, jobject object, jint width, jint height)
{
	glViewport(0, 0, width, height);
	LOGE("setting view port %d %d.", width, height);

}

/*
 * Class:     info_chenliang_tetris3d_OpenglRenderer
 * Method:    jniDrawFrame
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_info_chenliang_tetris3d_OpenglRenderer_jniDrawFrame
  (JNIEnv* env, jobject object, jobject game)
{
	jclass gameClass = (*env)->GetObjectClass(env, game);

	jfieldID blockContainerFieldId = (*env)->GetFieldID(env, gameClass, "blockContainer", "Linfo/chenliang/tetris3d/BlockContainer;");
	jobject blockContainer = (*env)->GetObjectField(env, game, blockContainerFieldId);

	jfieldID blockFieldId = (*env)->GetFieldID(env, gameClass, "block", "Linfo/chenliang/tetris3d/Block;");
	jobject block = (*env)->GetObjectField(env, game, blockFieldId);

	jclass blockClass = (*env)->GetObjectClass(env, block);
	jfieldID blockFramesFieldId = (*env)->GetFieldID(env, blockClass, "blockFrames", "[Linfo/chenliang/tetris3d/BlockFrame;");
	jobjectArray blockFrameArray = (*env)->GetObjectField(env, block, blockFramesFieldId);

	int size = (*env)->GetArrayLength(env, blockFrameArray);

	float color[4][4] = {
						{1.0, 0, 0, 1.0},
						{0, 1.0, 0, 1.0},
						{0, 0, 1.0, 1.0},
						{1.0, 1.0, 0, 1.0},
					};

	for(int i=0; i < size; i ++)
	{
		jobject blockFrame = (*env)->GetObjectArrayElement(env, blockFrameArray, i);

		glClearColor(color[i][0], color[i][1], color[i][2], 1.0f);
		glClear(GL_COLOR_BUFFER_BIT);
	}



}
