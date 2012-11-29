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

int finalMatrix;
int vertexPosition;
int vertexColor;

JNIEXPORT void JNICALL Java_info_chenliang_tetris3d_OpenglRenderer_jniSurfaceCreated
  (JNIEnv* env, jobject object)
{
    printGLString("Version", GL_VERSION);
    printGLString("Vendor", GL_VENDOR);
    printGLString("Renderer", GL_RENDERER);
    printGLString("Extensions", GL_EXTENSIONS);


//    "attribute vec4 vertexColor;\n"
//    			  	  	  	  	  	  	 "varying vec4 interpolatedColor;\n"
//    "interpolatedColor = vertexColor;\n"
    const GLchar* vertexShaderCode = "uniform mat4 finalMatrix;\n"
    								 "attribute vec4 vertexPosition;\n"
			  	  	  	  	  	  	 "void main()\n"
			  	  	  	  	  	  	 "{\n"
			    					 	 "gl_Position = finalMatrix * vertexPosition;\n"
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

    //"//gl_FragColor = interpolatedColor;\n"
    //"varying vec4 interpolatedColor;\n"
    const GLchar* pixelShaderCode = "precision mediump float;\n"
    								"void main()\n"
    			  	  	  	  	  	"{\n"
    									"gl_FragColor = vec4(0.0, 1.0, 0.0, 1.0);\n"
    			  	  	  	  	  	"}\n";
    const GLchar** pixelShaderSource = (const GLchar**)malloc(sizeof(const GLchar*));
    *pixelShaderSource = pixelShaderCode;

    GLuint pixelShader = glCreateShader(GL_FRAGMENT_SHADER);
    length[0]= strlen(pixelShaderCode);

    glShaderSource(pixelShader, 1, pixelShaderSource, NULL);
    glCompileShader(pixelShader);

    glGetShaderiv(pixelShader, GL_COMPILE_STATUS, status);
    LOGE("pixel shader compile status %d", status[0]);

    int size2 = 4096;
        GLchar info2[size2];
        GLsizei logSize2 = 0;




    glGetShaderInfoLog(pixelShader, size2, &logSize2, info2);

    char log2[logSize2];
            strncpy(log2, info2, logSize2);
    LOGE("pixel shader %s\n", log2);

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

    finalMatrix = glGetUniformLocation(program, "finalMatrix");
    vertexPosition = glGetAttribLocation(program, "vertexPosition");
    vertexColor = glGetAttribLocation(program, "vertexColor");

    glUseProgram(program);
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

float clamp2(float min, float max, float v)
{
	if(v < min)
	{
		v = min;
	}
	else if(v > max)
	{
		v = max;
	}
	return v;
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

	jfieldID finalMatrixFieldId = (*env)->GetFieldID(env, gameClass, "finalMatrix", "[F");
	jfloatArray finalMatrixArray = (*env)->GetObjectField(env, game, finalMatrixFieldId);
	const jfloat* finalMatrixPointer  = (*env)->GetFloatArrayElements(env, finalMatrixArray, 0);

	glUniformMatrix4fv(finalMatrix, 1, 0, finalMatrixPointer);

//	LOGE("%.4f %.4f %.4f %.4f ", finalMatrixPointer[0], finalMatrixPointer[1], finalMatrixPointer[2], finalMatrixPointer[3]);
//	LOGE("%.4f %.4f %.4f %.4f ", finalMatrixPointer[4], finalMatrixPointer[5], finalMatrixPointer[6], finalMatrixPointer[7]);
//	LOGE("%.4f %.4f %.4f %.4f ", finalMatrixPointer[8], finalMatrixPointer[9], finalMatrixPointer[10], finalMatrixPointer[11]);
//	LOGE("%.4f %.4f %.4f %.4f ", finalMatrixPointer[12], finalMatrixPointer[13], finalMatrixPointer[14], finalMatrixPointer[15]);
	jclass blockClass = (*env)->GetObjectClass(env, block);
	jfieldID blockFramesFieldId = (*env)->GetFieldID(env, blockClass, "blockFrames", "[Linfo/chenliang/tetris3d/BlockFrame;");
	jobjectArray blockFrameArray = (*env)->GetObjectField(env, block, blockFramesFieldId);


	int size = (*env)->GetArrayLength(env, blockFrameArray);
//
//	float color[4][4] = {
//						{1.0, 0, 0, 1.0},
//						{0, 1.0, 0, 1.0},
//						{0, 0, 1.0, 1.0},
//						{1.0, 1.0, 0, 1.0},
//					};
//
	//random();

	static float red = 0.0, green=0.0, blue=0.0;

	red += 0.02;
	green += 0.01;
	blue += 0.005;

	red = clamp2(0.0f, 1.0f, red);
	green = clamp2(0.0f, 1.0f, green);
	blue = clamp2(0.0f, 1.0f, blue);

	if(red >= 1.0f)
	{
		red = 0.0f;
	}

	if(green >= 1.0f)
	{
		green = 0.0f;
	}

	if(blue >= 1.0f)
	{
		blue = 0.0f;
	}

	//glClearColor(red, green, blue, 1.0f);
	glClearColor(1.0, 1.0, 1.0, 1.0f);
	glClear(GL_COLOR_BUFFER_BIT);

	glFrontFace(GL_CW);

	int floatSize = 4;
	jclass blockFrameClass = (*env)->FindClass(env, "info/chenliang/tetris3d/BlockFrame");

	for(int i=0; i < 1; i ++)
	{
		jobject blockFrame = (*env)->GetObjectArrayElement(env, blockFrameArray, i);

		jfieldID verticesFieldId = (*env)->GetFieldID(env, blockFrameClass, "vertices", "[F");
		jfloatArray verticesArray = (*env)->GetObjectField(env, blockFrame, verticesFieldId);
		const jfloat* vertices  = (*env)->GetFloatArrayElements(env, verticesArray, 0);

		glVertexAttribPointer(vertexPosition, floatSize, GL_FLOAT, GL_FALSE, 3*floatSize, vertices);
		glEnableVertexAttribArray(vertexPosition);

		jfieldID indicesFieldId = (*env)->GetFieldID(env, blockFrameClass, "indices", "[B");
		jbyteArray indicesArray = (*env)->GetObjectField(env, blockFrame, indicesFieldId);
		const jbyte* indices  = (*env)->GetByteArrayElements(env, indicesArray, 0);

//		glVertexAttribPointer(vertexColor, 1, GL_BYTE, GL_FALSE, 1, indices);
//		glEnableVertexAttribArray(vertexColor);

//		glEnableVertexAttribArray(indices);
		glDrawElements(GL_TRIANGLES, 6*2, GL_UNSIGNED_BYTE, indices);
		GLenum error = glGetError();
		if(error != GL_NO_ERROR)
		{
			LOGE("erro code %d\n", error);
		}

		(*env)->ReleaseByteArrayElements(env, indicesArray, indices, JNI_ABORT);
		(*env)->ReleaseFloatArrayElements(env, verticesArray, vertices, JNI_ABORT);
	}

	(*env)->ReleaseFloatArrayElements(env, finalMatrixArray, finalMatrixPointer, JNI_ABORT);
}


