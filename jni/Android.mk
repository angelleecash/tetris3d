LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_CFLAGS    := -Werror
LOCAL_MODULE    := opengles
LOCAL_SRC_FILES := opengles.c
LOCAL_LDLIBS    := -llog -lGLESv2

include $(BUILD_SHARED_LIBRARY)