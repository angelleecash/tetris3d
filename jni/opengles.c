#include <jni.h>
#include <android/log.h>

#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
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
}

/*
 * Class:     info_chenliang_tetris3d_OpenglRenderer
 * Method:    jniSurfaceChanged
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_info_chenliang_tetris3d_OpenglRenderer_jniSurfaceChanged
  (JNIEnv* env, jobject object, jint width, jint height)
{

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
	jfieldID blockContainerId = (*env)->GetFieldID(env, gameClass, "blockContainer", "Linfo/chenliang/tetris3d/BlockContainer;");

	jobject blockContainer = (*env)->GetObjectField(env, game, blockContainerId);


	glClearColor(0.5f, 0, 0, 1.0f);
	glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
}
