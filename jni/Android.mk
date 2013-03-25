LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_CFLAGS    := -Werror
LOCAL_CFLAGS 	:= -std=c99
LOCAL_MODULE    := game_renderer
LOCAL_SRC_FILES := game_renderer.c
LOCAL_LDLIBS    := -llog -lGLESv2

include $(BUILD_SHARED_LIBRARY)