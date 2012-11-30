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
    LOGE("GL %s = %s\n", name, v);
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

    glEnable(GL_DEPTH_TEST);
    glDepthMask(GL_TRUE);


//    "attribute vec4 vertexColor;\n"
//    			  	  	  	  	  	  	 "varying vec4 interpolatedColor;\n"
//    "interpolatedColor = vertexColor;\n"
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

    //"//gl_FragColor = interpolatedColor;\n"
    //"varying vec4 interpolatedColor;\n"
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

    glLinkProgram(program);

//    glBindAttribLocation(program, 0, "vertexPosition");
//    glBindAttribLocation(program, 1, "vertexColor");


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

	glUniformMatrix4fv(finalMatrix, 1, GL_FALSE, finalMatrixPointer);

//	LOGE("%.4f %.4f %.4f %.4f ", finalMatrixPointer[0], finalMatrixPointer[1], finalMatrixPointer[2], finalMatrixPointer[3]);
//	LOGE("%.4f %.4f %.4f %.4f ", finalMatrixPointer[4], finalMatrixPointer[5], finalMatrixPointer[6], finalMatrixPointer[7]);
//	LOGE("%.4f %.4f %.4f %.4f ", finalMatrixPointer[8], finalMatrixPointer[9], finalMatrixPointer[10], finalMatrixPointer[11]);
//	LOGE("%.4f %.4f %.4f %.4f ", finalMatrixPointer[12], finalMatrixPointer[13], finalMatrixPointer[14], finalMatrixPointer[15]);
//
//	if(1)
//	return;
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
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	glFrontFace(GL_CW);

	jclass blockFrameClass = (*env)->FindClass(env, "info/chenliang/tetris3d/BlockFrame");

	for(int i=0; i < 1; i ++)
	{
		jobject blockFrame = (*env)->GetObjectArrayElement(env, blockFrameArray, i);

		jfieldID verticesFieldId = (*env)->GetFieldID(env, blockFrameClass, "vertices", "[F");
		jfloatArray verticesArray = (*env)->GetObjectField(env, blockFrame, verticesFieldId);
		const jfloat* vertices  = (*env)->GetFloatArrayElements(env, verticesArray, 0);

		glVertexAttribPointer(vertexPosition, 3, GL_FLOAT, GL_FALSE, 0, vertices);
		glEnableVertexAttribArray(vertexPosition);

		jfieldID colorsFieldId = (*env)->GetFieldID(env, blockFrameClass, "colors", "[F");
		jfloatArray colorsArray = (*env)->GetObjectField(env, blockFrame, colorsFieldId);
		const jfloat* colors  = (*env)->GetFloatArrayElements(env, colorsArray, 0);

		glVertexAttribPointer(vertexColor, 3, GL_FLOAT, GL_FALSE, 0, colors);
		glEnableVertexAttribArray(vertexColor);

		jfieldID indicesFieldId = (*env)->GetFieldID(env, blockFrameClass, "indices", "[S");
		jshortArray indicesArray = (*env)->GetObjectField(env, blockFrame, indicesFieldId);
		const jshort* indices  = (*env)->GetShortArrayElements(env, indicesArray, 0);

//		LOGE("%d %d %d", indices[0], indices[1],indices[2]);
//		LOGE("%d %d %d", indices[3], indices[4],indices[5]);
//		LOGE("%d %d %d", indices[6], indices[7],indices[8]);
//		LOGE("%d %d %d", indices[9], indices[10],indices[11]);
//		LOGE("%d %d %d", indices[12], indices[13],indices[14]);
//		LOGE("%d %d %d", indices[15], indices[16],indices[17]);

//		glVertexAttribPointer(vertexColor, 1, GL_BYTE, GL_FALSE, 1, indices);
//		glEnableVertexAttribArray(vertexColor);

		//glEnableVertexAttribArray(indices);
		glDrawElements(GL_TRIANGLES, 4*6*2*3, GL_UNSIGNED_SHORT, indices);
//		glDrawArrays(GL_TRIANGLES, 0, 4*6*2);
		GLenum error = glGetError();
		if(error != GL_NO_ERROR)
		{
			LOGE("erro code %d\n", error);
		}

		(*env)->ReleaseShortArrayElements(env, indicesArray, indices, JNI_ABORT);
		(*env)->ReleaseFloatArrayElements(env, verticesArray, vertices, JNI_ABORT);
		(*env)->ReleaseFloatArrayElements(env, colorsArray, colors, JNI_ABORT);
	}

	(*env)->ReleaseFloatArrayElements(env, finalMatrixArray, finalMatrixPointer, JNI_ABORT);
}


